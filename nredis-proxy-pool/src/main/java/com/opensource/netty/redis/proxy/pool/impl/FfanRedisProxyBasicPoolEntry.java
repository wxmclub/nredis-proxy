/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.impl;

import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPoolEntry;
import com.opensource.netty.redis.proxy.pool.commons.FfanRedisProxyPoolEntryState;
import com.opensource.netty.redis.proxy.pool.commons.Pool;


/**
 * @author liubing
 *
 */
public class FfanRedisProxyBasicPoolEntry<T extends Pool> implements FfanRedisProxyPoolEntry<T> {
	
	private final T object;
	
	private final FfanRedisProxyPoolEntryState state;
	
	public FfanRedisProxyBasicPoolEntry(T object) {
		super();
		this.object = object;
		this.state = new FfanRedisProxyPoolEntryState();
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPoolEntry#getObject()
	 */
	@Override
	public T getObject() {
		// TODO Auto-generated method stub
		return object;
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPoolEntry#getState()
	 */
	@Override
	public FfanRedisProxyPoolEntryState getState() {
		// TODO Auto-generated method stub
		return state;
	}

}
