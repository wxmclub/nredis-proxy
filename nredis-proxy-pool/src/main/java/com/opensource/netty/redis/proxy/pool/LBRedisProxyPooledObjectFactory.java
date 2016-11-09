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
public interface LBRedisProxyPooledObjectFactory<T extends Pool> {
	
	T createInstance() throws LBRedisProxyPoolException;

}
