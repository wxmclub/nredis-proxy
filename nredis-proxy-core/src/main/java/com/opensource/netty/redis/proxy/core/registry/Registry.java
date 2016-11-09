/**
 * 
 */
package com.opensource.netty.redis.proxy.core.registry;

import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;

/**
 * @author liubing
 *
 */
public interface Registry extends RegistryService {
	
	/**
	 * 获取返回值
	 * @return
	 */
	RedisProxyURL getRedisProxyURL();
}	
