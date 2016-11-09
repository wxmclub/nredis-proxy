/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.impl;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.opensource.netty.redis.proxy.pool.LBRedisProxyIdleEntriesQueue;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPool;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPoolEntry;
import com.opensource.netty.redis.proxy.pool.LBRedisProxyPoolEntryFactory;
import com.opensource.netty.redis.proxy.pool.commons.LBRedisProxyPoolConfig;
import com.opensource.netty.redis.proxy.pool.commons.Pool;
import com.opensource.netty.redis.proxy.pool.exception.LBRedisProxyPoolException;


/**
 * 连接池实现类
 * @author liubing
 *
 */
public class LBRedisProxyBasicPool<T extends Pool> implements LBRedisProxyPool<T> {
	
	private final LBRedisProxyPoolConfig config;
	private final LBRedisProxyPoolEntryFactory<T> entryFactory;
	private final LBRedisProxyIdleEntriesQueue<T> idleEntries;

	private final Semaphore borrowingSemaphore;
	
	public LBRedisProxyBasicPool(LBRedisProxyPoolConfig config, LBRedisProxyIdleEntriesQueue<T> idleEntries,
			LBRedisProxyPoolEntryFactory<T> entryFactory) throws Exception {
		this.config = config;
		this.idleEntries = idleEntries;
		this.entryFactory = entryFactory;

		borrowingSemaphore = new Semaphore(config.getMaxActiveEntries());
		
		for (int i = 0; i < config.getInitialEntries(); i++) {
			try {
				idleEntries.offer(createIdleEntry());
			} catch (Exception e) {
				throw new LBRedisProxyPoolException(e);
			}
		}
	}
	
	private LBRedisProxyPoolEntry<T> createIdleEntry() throws Exception {
		return entryFactory.createPoolEntry();
	}
	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#getPoolConfig()
	 */
	@Override
	public LBRedisProxyPoolConfig getPoolConfig() {
		// TODO Auto-generated method stub
		return config;
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#borrowEntry()
	 */
	@Override
	public LBRedisProxyPoolEntry<T> borrowEntry() throws InterruptedException,
			TimeoutException, LBRedisProxyPoolException {
		// TODO Auto-generated method stub
		return borrowEntry(true);
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#borrowEntry(boolean)
	 */
	@Override
	public LBRedisProxyPoolEntry<T> borrowEntry(boolean createNew)
			throws InterruptedException, TimeoutException, LBRedisProxyPoolException {
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
	public LBRedisProxyPoolEntry<T> borrowEntry(long timeout, TimeUnit unit)
			throws InterruptedException, TimeoutException, LBRedisProxyPoolException {
		// TODO Auto-generated method stub
		return borrowEntry(true, timeout, unit);
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#borrowEntry(boolean, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public LBRedisProxyPoolEntry<T> borrowEntry(boolean createNew, long timeout,
			TimeUnit unit) throws InterruptedException, TimeoutException,
			LBRedisProxyPoolException {
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
	public LBRedisProxyPoolEntry<T> tryBorrowEntry() throws LBRedisProxyPoolException {
		// TODO Auto-generated method stub
		return tryBorrowEntry(true);
	}
	
	private LBRedisProxyPoolEntry<T> innerBorrowEntry(boolean createNew) throws LBRedisProxyPoolException {
		try {
			LBRedisProxyPoolEntry<T> entry = idleEntries.poll();

			if (entry == null && createNew) {
				entry = createIdleEntry();
			}

			if (entry == null) {
				borrowingSemaphore.release();
			}
			return entry;
		} catch (Exception e) {
			borrowingSemaphore.release();
			throw new LBRedisProxyPoolException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#tryBorrowEntry(boolean)
	 */
	@Override
	public LBRedisProxyPoolEntry<T> tryBorrowEntry(boolean createNew)
			throws LBRedisProxyPoolException {
		// TODO Auto-generated method stub
		try {
			LBRedisProxyPoolEntry<T> entry = idleEntries.poll();
			if (entry == null && createNew) {
				entry = createIdleEntry();
			}

			if (entry == null) {
				borrowingSemaphore.release();
			}
			return entry;
		} catch (Exception e) {
			borrowingSemaphore.release();
			throw new LBRedisProxyPoolException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.rpc.pool.FfanRpcPool#returnEntry(com.wanda.ffan.rpc.pool.FfanRpcPoolEntry)
	 */
	@Override
	public void returnEntry(LBRedisProxyPoolEntry<T> entry)
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
	
	public LBRedisProxyPoolConfig getConfig() {
		return config;
	}

	public LBRedisProxyPoolEntryFactory<T> getEntryFactory() {
		return entryFactory;
	}

	public LBRedisProxyIdleEntriesQueue<T> getIdleEntries() {
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
