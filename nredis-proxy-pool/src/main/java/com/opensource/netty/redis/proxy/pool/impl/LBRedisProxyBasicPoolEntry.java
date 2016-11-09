/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.impl;

import com.opensource.netty.redis.proxy.pool.LBRedisProxyPoolEntry;
import com.opensource.netty.redis.proxy.pool.commons.LBRedisProxyPoolEntryState;
import com.opensource.netty.redis.proxy.pool.commons.Pool;


/**
 * @author liubing
 *
 */
public class LBRedisProxyBasicPoolEntry<T extends Pool> implements LBRedisProxyPoolEntry<T> {
	
	private final T object;
	
	private final LBRedisProxyPoolEntryState state;
	
	public LBRedisProxyBasicPoolEntry(T object) {
		super();
		this.object = object;
		this.state = new LBRedisProxyPoolEntryState();
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
	public LBRedisProxyPoolEntryState getState() {
		// TODO Auto-generated method stub
		return state;
	}

}
