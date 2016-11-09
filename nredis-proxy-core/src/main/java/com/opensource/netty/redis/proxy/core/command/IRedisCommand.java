/**
 * 
 */
package com.opensource.netty.redis.proxy.core.command;

import io.netty.buffer.ByteBuf;

/**
 * @author liubing
 *
 */
public interface IRedisCommand {
	/**
	 * 编码
	 * @param byteBuf
	 */
	void encode(ByteBuf byteBuf);

}
