/**
 * 
 */
package com.opensource.netty.redis.proxy.pool;

import com.opensource.netty.redis.proxy.pool.commons.Pool;
import com.opensource.netty.redis.proxy.pool.exception.FfanRedisProxyPoolException;


/**
 * @author liubing
 *
 */
public interface FfanRedisProxyPoolEntryFactory<T extends Pool> {
	
	FfanRedisProxyPoolEntry<T> createPoolEntry() throws FfanRedisProxyPoolException;
}
