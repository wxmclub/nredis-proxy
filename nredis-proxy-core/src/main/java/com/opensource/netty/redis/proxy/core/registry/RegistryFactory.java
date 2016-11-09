/**
 * 
 */
package com.opensource.netty.redis.proxy.core.registry;

import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;


/**
 * @author liubing
 *
 */
public interface RegistryFactory {
	
	/**
	 * 
	 * @param redisProxyURL
	 * @return
	 */
	Registry getRegistry(RedisProxyURL redisProxyURL);
}
