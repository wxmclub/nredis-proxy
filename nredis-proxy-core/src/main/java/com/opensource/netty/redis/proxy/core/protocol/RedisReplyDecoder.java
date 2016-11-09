/**
 * 
 */
package com.opensource.netty.redis.proxy.core.protocol;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.opensource.netty.redis.proxy.commons.constants.RedisConstants;
import com.opensource.netty.redis.proxy.core.enums.ReplyState;
import com.opensource.netty.redis.proxy.core.enums.Type;
import com.opensource.netty.redis.proxy.core.reply.IRedisReply;
import com.opensource.netty.redis.proxy.core.reply.impl.BulkRedisReply;
import com.opensource.netty.redis.proxy.core.reply.impl.ErrorRedisReply;
import com.opensource.netty.redis.proxy.core.reply.impl.IntegerRedisReply;
import com.opensource.netty.redis.proxy.core.reply.impl.MultyBulkRedisReply;
import com.opensource.netty.redis.proxy.core.reply.impl.StatusRedisReply;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * @author liubing
 *
 */
public class RedisReplyDecoder extends ReplayingDecoder<ReplyState>{
	
	private IRedisReply redisReply;
	
	public RedisReplyDecoder() {
	    super(ReplyState.READ_INIT);
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		switch (state()) {
	      case READ_INIT:
	        char ch = (char) in.readByte();
	        if (ch == RedisConstants.ASTERISK_BYTE) {
	          redisReply = new MultyBulkRedisReply();
	        } else if (ch == RedisConstants.DOLLAR_BYTE) {
	          redisReply = new BulkRedisReply();
	        } else if (ch == RedisConstants.COLON_BYTE) {
	          redisReply = new IntegerRedisReply();
	        } else if (ch == RedisConstants.OK_BYTE) {
	          redisReply = new StatusRedisReply();
	        } else if (ch == RedisConstants.ERROR_BYTE) {
	          redisReply = new ErrorRedisReply();
	        }
	        checkpoint(ReplyState.READ_REPLY);
	      case READ_REPLY:
	        Type type = redisReply.getType();
	        if (type == Type.INTEGER) {
	          byte[] value = readLine(in).getBytes(RedisConstants.DEFAULT_CHARACTER);
	          ((IntegerRedisReply) redisReply).setValue(value);
	        } else if (type == Type.STATUS) {
	          byte[] value = readLine(in).getBytes(RedisConstants.DEFAULT_CHARACTER);
	          ((StatusRedisReply) redisReply).setValue(value);
	        } else if (type ==Type.ERROR) {
	          byte[] value = readLine(in).getBytes(RedisConstants.DEFAULT_CHARACTER);
	          ((ErrorRedisReply) redisReply).setValue(value);
	        } else if (type == Type.BULK) {
	          readBulkReply(in, (BulkRedisReply) this.redisReply);
	        } else if (type == Type.MULTYBULK) {
	          readArrayReply(in, (MultyBulkRedisReply) this.redisReply);
	        }
	        out.add(this.redisReply);
	        this.redisReply = null;
	        checkpoint(ReplyState.READ_INIT);
	        return;
	      default:
	        throw new Error("can't reach there!");
	    }	
	}
	
	private void readArrayReply(ByteBuf buffer, MultyBulkRedisReply multyBulkRedisReply) throws UnsupportedEncodingException {
	    int count = readInt(buffer);
	    multyBulkRedisReply.setCount(count);
	    for (int i = 0; i < count; i++) {
	      char type = (char) buffer.readByte();
	      if (type == RedisConstants.COLON_BYTE) {
	        IntegerRedisReply reply = new IntegerRedisReply();
	        reply.setValue(readLine(buffer).getBytes(RedisConstants.DEFAULT_CHARACTER));
	        multyBulkRedisReply.addReply(reply);
	      } else if (type == RedisConstants.DOLLAR_BYTE) {
	    	BulkRedisReply bulkReply = new BulkRedisReply();
	        readBulkReply(buffer, bulkReply);
	        multyBulkRedisReply.addReply(bulkReply);
	      }
	    }
	  }

	  private void readBulkReply(ByteBuf buffer, BulkRedisReply bulkReply) {
	    int length = readInt(buffer);
	    bulkReply.setLength(length);
	    if (length == -1) {//read null
	    	
	    } else if (length == 0) {//read ""
	      buffer.skipBytes(2);
	    } else {
	      byte[] value = new byte[length];
	      buffer.readBytes(value);
	      bulkReply.setValue(value);
	      buffer.skipBytes(2);
	    }
	  }

	  private int readInt(ByteBuf buffer) {
	    return Integer.parseInt(readLine(buffer));
	  }

	  private String readLine(ByteBuf byteBuf) {
	    StringBuilder sb = new StringBuilder();
	    char ch = (char) byteBuf.readByte();
	    while (ch != RedisConstants.CR_BYTE) {
	      sb.append(ch);
	      ch = (char) byteBuf.readByte();
	    }
	    byteBuf.skipBytes(1);
	    return sb.toString();
	  }
}
