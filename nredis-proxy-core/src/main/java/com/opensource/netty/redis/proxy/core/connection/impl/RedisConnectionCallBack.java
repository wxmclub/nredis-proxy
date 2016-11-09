/**
 * 
 */
package com.opensource.netty.redis.proxy.core.connection.impl;

import io.netty.channel.Channel;

import com.opensource.netty.redis.proxy.core.connection.IConnectionCallBack;
import com.opensource.netty.redis.proxy.core.reply.IRedisReply;

/**
 * 
 * @author liubing
 *
 */
public class RedisConnectionCallBack implements IConnectionCallBack {
	
	private Channel channel;
	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.connection.IConnectionCallBack#handleReply(com.wanda.ffan.redis.proxy.core.reply.IRedisReply)
	 */
	@Override
	public void handleReply(IRedisReply reply) {
	    channel.writeAndFlush(reply);
	}
	/**
	 * @param channel
	 */
	public RedisConnectionCallBack(Channel channel) {
		super();
		this.channel = channel;
	}
	
	
}
