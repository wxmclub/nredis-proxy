/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.impl;

import java.util.concurrent.ArrayBlockingQueue;

import com.opensource.netty.redis.proxy.pool.LBRedisProxyIdleEntriesQueue;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPoolEntry;
import com.opensource.netty.redis.proxy.pool.commons.LBRedisProxyPoolConfig;
import com.opensource.netty.redis.proxy.pool.commons.Pool;



/**
 * 队列实现类
 * @author liubing
 *
 */
public class LBRedisProxyPoolBasicIdleEntriesQueue<T extends Pool> implements LBRedisProxyIdleEntriesQueue<T> {
	
	private final ArrayBlockingQueue<LBRedisProxyPoolEntry<T>> idleEntries;
	
	public LBRedisProxyPoolBasicIdleEntriesQueue(LBRedisProxyPoolConfig config) {
		idleEntries = new ArrayBlockingQueue<LBRedisProxyPoolEntry<T>>(
				config.getMaxIdleEntries());
	}
	
	@Override
	public LBRedisProxyPoolEntry<T> poll() {
		
		// TODO Auto-generated method stub
		LBRedisProxyPoolEntry<T> idle = idleEntries.poll();
		return idle;
	}

	@Override
	public boolean offer(LBRedisProxyPoolEntry<T> entry) throws NullPointerException {
		// TODO Auto-generated method stub
		if (entry == null)
			throw new NullPointerException("entry is null.");
		if (!entry.getState().isValid())
			return false;

		boolean offerSuccessful = idleEntries.offer(entry);
		return offerSuccessful;
	}

	public ArrayBlockingQueue<LBRedisProxyPoolEntry<T>> getIdleEntries() {
		return idleEntries;
	}

	@Override
	public int getIdleEntriesCount() {
		// TODO Auto-generated method stub
		return idleEntries.size();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
		idleEntries.clear();
	}
	
	
}
