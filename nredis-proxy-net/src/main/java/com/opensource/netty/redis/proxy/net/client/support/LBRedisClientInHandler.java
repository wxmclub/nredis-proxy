/**
 * 
 */
package com.opensource.netty.redis.proxy.net.client.support;

import com.opensource.netty.redis.proxy.core.connection.IConnectionCallBack;
import com.opensource.netty.redis.proxy.core.log.impl.LoggerUtils;
import com.opensource.netty.redis.proxy.core.reply.IRedisReply;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 目标服务器与客户端通道写入
 * @author liubing
 *
 */
public class LBRedisClientInHandler extends SimpleChannelInboundHandler<IRedisReply> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IRedisReply msg)
			throws Exception {
			IConnectionCallBack callBack = ctx.channel().attr(LBRedisClientOutHandler.CALLBACK_KEY).get();
			if(callBack!=null){
				callBack.handleReply(msg);
			}else{
				LoggerUtils.error(ctx.channel().remoteAddress().toString()+" has no callBack");
			}
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		
		super.exceptionCaught(ctx, cause);
	}
}
