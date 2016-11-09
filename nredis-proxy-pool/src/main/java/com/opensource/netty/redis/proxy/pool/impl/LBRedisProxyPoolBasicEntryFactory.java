/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.impl;

import com.opensource.netty.redis.proxy.pool.LBRedisProxyPoolEntry;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPoolEntryFactory;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPooledObjectFactory;
import com.opensource.netty.redis.proxy.pool.commons.Pool;
import com.opensource.netty.redis.proxy.pool.exception.LBRedisProxyPoolException;



/**
 * @author liubing
 *
 */
public class LBRedisProxyPoolBasicEntryFactory<T extends Pool> implements LBRedisProxyPoolEntryFactory<T> {
	
	private final LBRedisProxyPooledObjectFactory<T> ffanRpcPooledObjectFactory;
	
	
	public LBRedisProxyPoolBasicEntryFactory(
			LBRedisProxyPooledObjectFactory<T> ffanRpcPoolBasicEntryFactory) {
		super();
		this.ffanRpcPooledObjectFactory = ffanRpcPoolBasicEntryFactory;
	}


	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPoolEntryFactory#createPoolEntry()
	 */
	@Override
	public LBRedisProxyPoolEntry<T> createPoolEntry() throws LBRedisProxyPoolException {
		// TODO Auto-generated method stub
		T object = null;
		try {
			object = ffanRpcPooledObjectFactory.createInstance();
			return new LBRedisProxyBasicPoolEntry<T>(object);
		} catch (LBRedisProxyPoolException e) {
			throw e;
		}
	}

}
