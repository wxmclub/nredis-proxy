/**
 * 
 */
package com.opensource.netty.redis.proxy.core.reply.impl;

import com.opensource.netty.redis.proxy.core.enums.Type;

import io.netty.buffer.ByteBuf;

/**
 * @author liubing
 *
 */
public class IntegerRedisReply extends CommonRedisReply {

	public IntegerRedisReply() {
		super(Type.INTEGER);
	}

	public IntegerRedisReply(byte[] value) {
		this();
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wanda.ffan.redis.proxy.core.reply.impl.AbstractRedisReply#doEncode
	 * (io.netty.buffer.ByteBuf)
	 */
	@Override
	public void doEncode(ByteBuf out) {
		out.writeBytes(value);
		writeCRLF(out); 
	}

}
