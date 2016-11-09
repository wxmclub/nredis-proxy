/**
 * 
 */
package com.opensource.netty.redis.proxy.core.reply.impl;

import com.opensource.netty.redis.proxy.commons.utils.ProtoUtils;
import com.opensource.netty.redis.proxy.core.enums.Type;

import io.netty.buffer.ByteBuf;

/**
 * 
 * @author liubing
 *
 */
public class BulkRedisReply extends CommonRedisReply {

	private int length;

	public BulkRedisReply(byte[] value) {
		this();
		this.value = value;
	}

	public BulkRedisReply() {
		super(Type.BULK);
	}

	public void setLength(int length) {
		this.length = length;
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
		out.writeBytes(ProtoUtils.convertIntToByteArray(length));
		writeCRLF(out);
		if (length > -1 && value != null) {
			out.writeBytes(value);
			writeCRLF(out);
		}
	}

}
