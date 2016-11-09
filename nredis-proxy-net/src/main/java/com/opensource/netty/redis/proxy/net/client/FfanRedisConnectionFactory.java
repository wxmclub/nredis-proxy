/**
 * 
 */
package com.opensource.netty.redis.proxy.net.client;

import com.opensource.netty.redis.proxy.core.connection.IConnection;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPooledObjectFactory;
import com.opensource.netty.redis.proxy.pool.exception.FfanRedisProxyPoolException;


/**
 * @author liubing
 *
 */
public class FfanRedisConnectionFactory implements FfanRedisProxyPooledObjectFactory<IConnection>{
	
	private FfanRedisClient ffanRedisClient;
	
	
	/**
	 * @param ffanRedisClient
	 */
	public FfanRedisConnectionFactory(FfanRedisClient ffanRedisClient) {
		super();
		this.ffanRedisClient = ffanRedisClient;
	}



	@Override
	public IConnection createInstance() throws FfanRedisProxyPoolException {
		FfanRedisConnection ffanRedisConnection=new FfanRedisConnection(ffanRedisClient);
		ffanRedisConnection.open();
		return ffanRedisConnection;
	}

}
