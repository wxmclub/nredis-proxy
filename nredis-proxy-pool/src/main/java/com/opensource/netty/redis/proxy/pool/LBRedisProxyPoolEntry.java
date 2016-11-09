/**
 * 
 */
package com.opensource.netty.redis.proxy.pool;

import com.opensource.netty.redis.proxy.pool.commons.LBRedisProxyPoolEntryState;
import com.opensource.netty.redis.proxy.pool.commons.Pool;
/**
 * @author liubing
 *
 */
public interface LBRedisProxyPoolEntry<T extends Pool> {
	
	T getObject();

	
	LBRedisProxyPoolEntryState getState();
}
