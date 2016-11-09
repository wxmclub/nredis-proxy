/**
 * 
 */
package com.opensource.netty.redis.proxy.pool;

import com.opensource.netty.redis.proxy.pool.commons.FfanRedisProxyPoolEntryState;
import com.opensource.netty.redis.proxy.pool.commons.Pool;
/**
 * @author liubing
 *
 */
public interface FfanRedisProxyPoolEntry<T extends Pool> {
	
	T getObject();

	
	FfanRedisProxyPoolEntryState getState();
}
