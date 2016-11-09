/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.impl;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.opensource.netty.redis.proxy.pool.FfanRedisProxyIdleEntriesQueue;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPool;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPoolEntry;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPoolEntryFactory;
import com.opensource.netty.redis.proxy.pool.commons.FfanRedisProxyPoolConfig;
import com.opensource.netty.redis.proxy.pool.commons.Pool;
import com.opensource.netty.redis.proxy.pool.exception.FfanRedisProxyPoolException;


/**
 * 连接池实现类
 * @author liubing
 *
 */
public class FfanRedisProxyBasicPool<T extends Pool> implements FfanRedisProxyPool<T> {
	
	private final FfanRedisProxyPoolConfig config;
	private final FfanRedisProxyPoolEntryFactory<T> entryFactory;
	private final FfanRedisProxyIdleEntriesQueue<T> idleEntries;

	private final Semaphore borrowingSemaphore;
	
	public FfanRedisProxyBasicPool(FfanRedisProxyPoolConfig config, FfanRedisProxyIdleEntriesQueue<T> idleEntries,
			FfanRedisProxyPoolEntryFactory<T> entryFactory) throws Exception {
		this.config = config;
		this.idleEntries = idleEntries;
		this.entryFactory = entryFactory;

		borrowingSemaphore = new Semaphore(config.getMaxActiveEntries());
		
		for (int i = 0; i < config.getInitialEntries(); i++) {
			try {
				idleEntries.offer(createIdleEntry());
			} catch (Exception e) {
				throw new FfanRedisProxyPoolException(e);
			}
		}
	}
	
	private FfanRedisProxyPoolEntry<T> createIdleEntry() throws Exception {
		return entryFactory.createPoolEntry();
	}
	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#getPoolConfig()
	 */
	@Override
	public FfanRedisProxyPoolConfig getPoolConfig() {
		// TODO Auto-generated method stub
		return config;
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#borrowEntry()
	 */
	@Override
	public FfanRedisProxyPoolEntry<T> borrowEntry() throws InterruptedException,
			TimeoutException, FfanRedisProxyPoolException {
		// TODO Auto-generated method stub
		return borrowEntry(true);
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#borrowEntry(boolean)
	 */
	@Override
	public FfanRedisProxyPoolEntry<T> borrowEntry(boolean createNew)
			throws InterruptedException, TimeoutException, FfanRedisProxyPoolException {
		// TODO Auto-generated method stub
		boolean acquireSuccess = borrowingSemaphore.tryAcquire();
		if (!acquireSuccess) {
			return null;
		}

		return innerBorrowEntry(createNew);
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#borrowEntry(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public FfanRedisProxyPoolEntry<T> borrowEntry(long timeout, TimeUnit unit)
			throws InterruptedException, TimeoutException, FfanRedisProxyPoolException {
		// TODO Auto-generated method stub
		return borrowEntry(true, timeout, unit);
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#borrowEntry(boolean, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public FfanRedisProxyPoolEntry<T> borrowEntry(boolean createNew, long timeout,
			TimeUnit unit) throws InterruptedException, TimeoutException,
			FfanRedisProxyPoolException {
		// TODO Auto-generated method stub
		try {
			if (config.isWaitUnlimitOnBorrow()) {
				borrowingSemaphore.acquire();
			} else {
				boolean acquireSuccess = borrowingSemaphore.tryAcquire(timeout,
						unit);
				if (!acquireSuccess) {
					// pool entries all busy
					throw new TimeoutException("borrowEntry timed out.");
				}
			}
		} catch (InterruptedException e) {
			throw e;
		}

		return innerBorrowEntry(createNew);
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#tryBorrowEntry()
	 */
	@Override
	public FfanRedisProxyPoolEntry<T> tryBorrowEntry() throws FfanRedisProxyPoolException {
		// TODO Auto-generated method stub
		return tryBorrowEntry(true);
	}
	
	private FfanRedisProxyPoolEntry<T> innerBorrowEntry(boolean createNew) throws FfanRedisProxyPoolException {
		try {
			FfanRedisProxyPoolEntry<T> entry = idleEntries.poll();

			if (entry == null && createNew) {
				entry = createIdleEntry();
			}

			if (entry == null) {
				borrowingSemaphore.release();
			}
			return entry;
		} catch (Exception e) {
			borrowingSemaphore.release();
			throw new FfanRedisProxyPoolException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#tryBorrowEntry(boolean)
	 */
	@Override
	public FfanRedisProxyPoolEntry<T> tryBorrowEntry(boolean createNew)
			throws FfanRedisProxyPoolException {
		// TODO Auto-generated method stub
		try {
			FfanRedisProxyPoolEntry<T> entry = idleEntries.poll();
			if (entry == null && createNew) {
				entry = createIdleEntry();
			}

			if (entry == null) {
				borrowingSemaphore.release();
			}
			return entry;
		} catch (Exception e) {
			borrowingSemaphore.release();
			throw new FfanRedisProxyPoolException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#returnEntry(com.wanda.ffan.rpc.pool.FfanRpcPoolEntry)
	 */
	@Override
	public void returnEntry(FfanRedisProxyPoolEntry<T> entry)
			throws NullPointerException {
		// TODO Auto-generated method stub
		if (entry == null)
			throw new NullPointerException("entry is null");

		try {
			idleEntries.offer(entry);
		} finally {
			borrowingSemaphore.release();
		}
	}
	
	/*
	 * This method is typically used for debugging and testing purposes.
	 */
	public int availablePermits() {
		return borrowingSemaphore.availablePermits();
	}
	
	public FfanRedisProxyPoolConfig getConfig() {
		return config;
	}

	public FfanRedisProxyPoolEntryFactory<T> getEntryFactory() {
		return entryFactory;
	}

	public FfanRedisProxyIdleEntriesQueue<T> getIdleEntries() {
		return idleEntries;
	}

	public Semaphore getBorrowingSemaphore() {
		return borrowingSemaphore;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
		idleEntries.clear();
	}
	
	
}
