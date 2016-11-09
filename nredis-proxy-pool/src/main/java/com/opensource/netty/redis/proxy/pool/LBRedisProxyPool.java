/**
 * 
 */
package com.opensource.netty.redis.proxy.pool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.opensource.netty.redis.proxy.pool.commons.LBRedisProxyPoolConfig;
import com.opensource.netty.redis.proxy.pool.commons.Pool;
import com.opensource.netty.redis.proxy.pool.exception.LBRedisProxyPoolException;


/**
 * @author liubing
 *
 */
public interface LBRedisProxyPool<T  extends Pool> {

	LBRedisProxyPoolConfig getPoolConfig();

	LBRedisProxyPoolEntry<T> borrowEntry() throws InterruptedException, TimeoutException,LBRedisProxyPoolException;

	LBRedisProxyPoolEntry<T> borrowEntry(boolean createNew) throws InterruptedException,
			TimeoutException, LBRedisProxyPoolException;

	LBRedisProxyPoolEntry<T> borrowEntry(long timeout, TimeUnit unit)
			throws InterruptedException, TimeoutException, LBRedisProxyPoolException;

	LBRedisProxyPoolEntry<T> borrowEntry(boolean createNew, long timeout, TimeUnit unit)
			throws InterruptedException, TimeoutException, LBRedisProxyPoolException;

	LBRedisProxyPoolEntry<T> tryBorrowEntry() throws LBRedisProxyPoolException;
	
	LBRedisProxyPoolEntry<T> tryBorrowEntry(boolean createNew) throws LBRedisProxyPoolException;

	void returnEntry(LBRedisProxyPoolEntry<T> entry) throws NullPointerException;
	
	void clear();
}
