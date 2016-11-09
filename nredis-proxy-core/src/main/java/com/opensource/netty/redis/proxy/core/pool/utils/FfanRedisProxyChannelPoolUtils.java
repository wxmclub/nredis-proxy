/**
 * 
 */
package com.opensource.netty.redis.proxy.core.pool.utils;

import com.opensource.netty.redis.proxy.core.connection.IConnection;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyIdleEntriesQueue;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPoolEntryFactory;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPooledObjectFactory;
import com.opensource.netty.redis.proxy.pool.commons.FfanRedisProxyPoolConfig;
import com.opensource.netty.redis.proxy.pool.impl.FfanRedisProxyBasicPool;
import com.opensource.netty.redis.proxy.pool.impl.FfanRedisProxyPoolBasicEntryFactory;
import com.opensource.netty.redis.proxy.pool.impl.FfanRedisProxyPoolBasicIdleEntriesQueue;



/**
 * @author liubing
 *
 */
public class FfanRedisProxyChannelPoolUtils {
	
	public static  FfanRedisProxyBasicPool<IConnection> createPool(FfanRedisProxyPoolConfig config, FfanRedisProxyPooledObjectFactory<IConnection> factory) throws Exception {
		return createPool( config, createQueue(config),createPoolEntryFactory(factory));
	}
	
	private static  FfanRedisProxyPoolEntryFactory<IConnection> createPoolEntryFactory(FfanRedisProxyPooledObjectFactory<IConnection> objectFactory) {

		return new FfanRedisProxyPoolBasicEntryFactory<IConnection>(objectFactory);
	}
	
	private static  FfanRedisProxyIdleEntriesQueue<IConnection> createQueue( FfanRedisProxyPoolConfig config) {
		return new FfanRedisProxyPoolBasicIdleEntriesQueue<IConnection>(config);
	}

	
	public static  FfanRedisProxyBasicPool<IConnection> createPool(FfanRedisProxyPoolConfig config, FfanRedisProxyIdleEntriesQueue<IConnection> queue, FfanRedisProxyPoolEntryFactory<IConnection> factory) throws Exception {
		return new FfanRedisProxyBasicPool<IConnection>(config, queue, factory);
	}
}
