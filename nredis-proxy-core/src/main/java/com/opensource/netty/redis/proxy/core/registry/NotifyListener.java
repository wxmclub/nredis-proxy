/**
 * 
 */
package com.opensource.netty.redis.proxy.core.registry;

import java.util.List;

import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;

/**
 * @author liubing
 *
 */
public interface NotifyListener {
	
	/**
	 * 提醒
	 * @param redisProxyURL
	 * @param redisProxyURLClusters
	 */
	void notify(RedisProxyURL redisProxyURL,List<RedisProxyURL> redisProxyURLClusters);
}
