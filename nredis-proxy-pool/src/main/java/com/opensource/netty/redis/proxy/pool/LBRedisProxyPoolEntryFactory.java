/**
 * 
 */
package com.opensource.netty.redis.proxy.pool;

import com.opensource.netty.redis.proxy.pool.commons.Pool;
import com.opensource.netty.redis.proxy.pool.exception.LBRedisProxyPoolException;


/**
 * @author liubing
 *
 */
public interface LBRedisProxyPoolEntryFactory<T extends Pool> {
	
	LBRedisProxyPoolEntry<T> createPoolEntry() throws LBRedisProxyPoolException;
}
