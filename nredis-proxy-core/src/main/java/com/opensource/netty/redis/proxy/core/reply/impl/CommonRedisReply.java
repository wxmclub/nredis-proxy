/**
 * 
 */
package com.opensource.netty.redis.proxy.core.reply.impl;

import com.opensource.netty.redis.proxy.core.enums.Type;

/**
 * @author liubing
 *
 */
public abstract class CommonRedisReply extends AbstractRedisReply {

	protected byte[] value;

	public CommonRedisReply(Type type) {
		super(type);
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

}
