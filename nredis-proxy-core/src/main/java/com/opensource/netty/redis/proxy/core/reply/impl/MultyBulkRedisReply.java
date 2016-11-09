/**
 * 
 */
package com.opensource.netty.redis.proxy.core.reply.impl;

import java.util.ArrayList;
import java.util.List;

import com.opensource.netty.redis.proxy.commons.constants.RedisConstants;
import com.opensource.netty.redis.proxy.commons.utils.ProtoUtils;
import com.opensource.netty.redis.proxy.core.enums.Type;
import com.opensource.netty.redis.proxy.core.reply.IRedisReply;

import io.netty.buffer.ByteBuf;

/**
 * @author liubing
 *
 */
public class MultyBulkRedisReply extends CommonRedisReply {

	protected List<IRedisReply> list = new ArrayList<IRedisReply>();

	private int count;

	public void setCount(int count) {
		this.count = count;
	}

	public MultyBulkRedisReply() {
		super(Type.MULTYBULK);
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
		out.writeBytes(ProtoUtils.convertIntToByteArray(count));
		writeCRLF(out);
		for (IRedisReply reply : list) {
			if (reply instanceof IntegerRedisReply) {
				if (value == null&&count==0) {
					out.writeByte(RedisConstants.COLON_BYTE);
					out.writeBytes(ProtoUtils.convertIntToByteArray(-1));
					writeCRLF(out);
				} else {
					out.writeByte(RedisConstants.COLON_BYTE);
					out.writeBytes(ProtoUtils
							.convertIntToByteArray(((IntegerRedisReply) reply).value.length));
					writeCRLF(out);
					out.writeBytes(((IntegerRedisReply) reply).value);
					writeCRLF(out);
				}

			} else if (reply instanceof BulkRedisReply) {
				if (value == null&&count==0) {
					out.writeByte(RedisConstants.DOLLAR_BYTE);
					out.writeBytes(ProtoUtils.convertIntToByteArray(-1));
					writeCRLF(out);
				} else {
					out.writeByte(RedisConstants.DOLLAR_BYTE);
					out.writeBytes(ProtoUtils
							.convertIntToByteArray(((BulkRedisReply) reply).value.length));
					writeCRLF(out);
					out.writeBytes(((BulkRedisReply) reply).value);
					writeCRLF(out);
				}

			}
		}
	}

	public void addReply(IRedisReply reply) {
		list.add(reply);
	}
}
