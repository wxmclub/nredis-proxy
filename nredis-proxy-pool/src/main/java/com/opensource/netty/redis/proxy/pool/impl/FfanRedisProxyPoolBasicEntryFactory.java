/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.impl;

import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPoolEntry;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPoolEntryFactory;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPooledObjectFactory;
import com.opensource.netty.redis.proxy.pool.commons.Pool;
import com.opensource.netty.redis.proxy.pool.exception.FfanRedisProxyPoolException;



/**
 * @author liubing
 *
 */
public class FfanRedisProxyPoolBasicEntryFactory<T extends Pool> implements FfanRedisProxyPoolEntryFactory<T> {
	
	private final FfanRedisProxyPooledObjectFactory<T> ffanRpcPooledObjectFactory;
	
	
	public FfanRedisProxyPoolBasicEntryFactory(
			FfanRedisProxyPooledObjectFactory<T> ffanRpcPoolBasicEntryFactory) {
		super();
		this.ffanRpcPooledObjectFactory = ffanRpcPoolBasicEntryFactory;
	}


	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPoolEntryFactory#createPoolEntry()
	 */
	@Override
	public FfanRedisProxyPoolEntry<T> createPoolEntry() throws FfanRedisProxyPoolException {
		// TODO Auto-generated method stub
		T object = null;
		try {
			object = ffanRpcPooledObjectFactory.createInstance();
			return new FfanRedisProxyBasicPoolEntry<T>(object);
		} catch (FfanRedisProxyPoolException e) {
			throw e;
		}
	}

}
