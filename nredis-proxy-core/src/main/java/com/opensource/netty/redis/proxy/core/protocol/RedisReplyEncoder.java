/**
 * 
 */
package com.opensource.netty.redis.proxy.core.protocol;

import com.opensource.netty.redis.proxy.core.reply.IRedisReply;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author liubing
 *
 */
public class RedisReplyEncoder extends MessageToByteEncoder<IRedisReply> {

	@Override
	protected void encode(ChannelHandlerContext ctx, IRedisReply msg,
			ByteBuf out) throws Exception {
    	msg.encode(out);
	}

}
