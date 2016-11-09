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
public interface FfanRedisProxyPooledObjectFactory<T extends Pool> {
	
	T createInstance() throws FfanRedisProxyPoolException;

}
