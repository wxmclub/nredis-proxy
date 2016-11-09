/**
 * 
 */
package com.opensource.netty.redis.proxy.core.protocol;

import java.util.ArrayList;
import java.util.List;

import com.opensource.netty.redis.proxy.commons.constants.RedisConstants;
import com.opensource.netty.redis.proxy.core.command.impl.RedisCommand;
import com.opensource.netty.redis.proxy.core.command.impl.ShutdownCommand;
import com.opensource.netty.redis.proxy.core.enums.RequestState;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * 
 * request 解码
 * @author liubing
 *
 */
public class RedisRequestDecoder extends ReplayingDecoder<RequestState> {

	private RedisCommand requestCommand;

	public RedisRequestDecoder() {
		super(RequestState.READ_SKIP);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
			List<Object> out) throws Exception {
		switch (state()) {
	      case READ_SKIP: {
	        try {
	          skipChar(buffer);
	          checkpoint(RequestState.READ_INIT);
	        } finally {
	          checkpoint();
	        }
	      }
	      case READ_INIT: {
	        char ch = (char) buffer.readByte();
	        if (ch == RedisConstants.ASTERISK_BYTE) {
	          ch = (char) buffer.readByte();
	          if (ch == RedisConstants.CR_BYTE) {//shutdown命令
	            buffer.readByte();
	            checkpoint(RequestState.READ_SKIP);
	            out.add(new ShutdownCommand());
	          } else {
	            buffer.readerIndex(buffer.readerIndex() - 1);
	            requestCommand = new RedisCommand();
	            checkpoint(RequestState.READ_ARGC);
	          }
	        }
	      }
	      case READ_ARGC: {
	        requestCommand.setArgCount(readInt(buffer));
	        checkpoint(RequestState.READ_ARG);
	      }
	      case READ_ARG: {
	        List<byte[]> args = new ArrayList<>(requestCommand.getArgCount());
	        while (args.size() < requestCommand.getArgCount()) {
	          buffer.readByte();//读出$
	          int length = readInt(buffer);
	          byte[] argByte = new byte[length];
	          buffer.readBytes(argByte);
	          buffer.skipBytes(2);//skip \r\n
	          args.add(argByte);
	        }
	        requestCommand.setArgs(args);
	        checkpoint(RequestState.READ_END);
	      }
	      case READ_END: {
	        RedisCommand command = this.requestCommand;
	        this.requestCommand = null;
	        checkpoint(RequestState.READ_INIT);
	        out.add(command);
	        return;
	      }
	      default:
	        throw new Error("can't reach here!");
	    }
	}

	private int readInt(ByteBuf buffer) {
		StringBuilder sb = new StringBuilder();
		char ch = (char) buffer.readByte();
		while (ch != RedisConstants.CR_BYTE) {
			sb.append(ch);
			ch = (char) buffer.readByte();
		}
		buffer.readByte();
		return Integer.parseInt(sb.toString());
	}

	private void skipChar(ByteBuf buffer) {
		for (;;) {
			char ch = (char) buffer.readByte();
			if (ch == RedisConstants.ASTERISK_BYTE) {
				buffer.readerIndex(buffer.readerIndex() - 1);
				break;
			}
		}
	}
}
