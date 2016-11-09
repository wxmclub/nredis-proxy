/**
 * 
 */
package com.opensource.netty.redis.proxy.net.client;

import com.opensource.netty.redis.proxy.core.connection.IConnection;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPooledObjectFactory;
import com.opensource.netty.redis.proxy.pool.exception.LBRedisProxyPoolException;


/**
 * @author liubing
 *
 */
public class LBRedisConnectionFactory implements LBRedisProxyPooledObjectFactory<IConnection>{
	
	private LBRedisClient ffanRedisClient;
	
	
	/**
	 * @param ffanRedisClient
	 */
	public LBRedisConnectionFactory(LBRedisClient ffanRedisClient) {
		super();
		this.ffanRedisClient = ffanRedisClient;
	}



	@Override
	public IConnection createInstance() throws LBRedisProxyPoolException {
		LBRedisConnection ffanRedisConnection=new LBRedisConnection(ffanRedisClient);
		ffanRedisConnection.open();
		return ffanRedisConnection;
	}

}
