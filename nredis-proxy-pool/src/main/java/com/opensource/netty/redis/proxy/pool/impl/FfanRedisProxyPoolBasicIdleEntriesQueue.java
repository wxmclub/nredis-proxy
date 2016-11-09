/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.impl;

import java.util.concurrent.ArrayBlockingQueue;

import com.opensource.netty.redis.proxy.pool.FfanRedisProxyIdleEntriesQueue;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPoolEntry;
import com.opensource.netty.redis.proxy.pool.commons.FfanRedisProxyPoolConfig;
import com.opensource.netty.redis.proxy.pool.commons.Pool;



/**
 * 队列实现类
 * @author liubing
 *
 */
public class FfanRedisProxyPoolBasicIdleEntriesQueue<T extends Pool> implements FfanRedisProxyIdleEntriesQueue<T> {
	
	private final ArrayBlockingQueue<FfanRedisProxyPoolEntry<T>> idleEntries;
	
	public FfanRedisProxyPoolBasicIdleEntriesQueue(FfanRedisProxyPoolConfig config) {
		idleEntries = new ArrayBlockingQueue<FfanRedisProxyPoolEntry<T>>(
				config.getMaxIdleEntries());
	}
	
	@Override
	public FfanRedisProxyPoolEntry<T> poll() {
		
		// TODO Auto-generated method stub
		FfanRedisProxyPoolEntry<T> idle = idleEntries.poll();
		return idle;
	}

	@Override
	public boolean offer(FfanRedisProxyPoolEntry<T> entry) throws NullPointerException {
		// TODO Auto-generated method stub
		if (entry == null)
			throw new NullPointerException("entry is null.");
		if (!entry.getState().isValid())
			return false;

		boolean offerSuccessful = idleEntries.offer(entry);
		return offerSuccessful;
	}

	public ArrayBlockingQueue<FfanRedisProxyPoolEntry<T>> getIdleEntries() {
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
