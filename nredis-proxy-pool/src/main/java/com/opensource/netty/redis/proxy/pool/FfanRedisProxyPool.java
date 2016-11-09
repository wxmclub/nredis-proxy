/**
 * 
 */
package com.opensource.netty.redis.proxy.pool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.opensource.netty.redis.proxy.pool.commons.FfanRedisProxyPoolConfig;
import com.opensource.netty.redis.proxy.pool.commons.Pool;
import com.opensource.netty.redis.proxy.pool.exception.FfanRedisProxyPoolException;


/**
 * @author liubing
 *
 */
public interface FfanRedisProxyPool<T  extends Pool> {

	FfanRedisProxyPoolConfig getPoolConfig();

	FfanRedisProxyPoolEntry<T> borrowEntry() throws InterruptedException, TimeoutException,FfanRedisProxyPoolException;

	FfanRedisProxyPoolEntry<T> borrowEntry(boolean createNew) throws InterruptedException,
			TimeoutException, FfanRedisProxyPoolException;

	FfanRedisProxyPoolEntry<T> borrowEntry(long timeout, TimeUnit unit)
			throws InterruptedException, TimeoutException, FfanRedisProxyPoolException;

	FfanRedisProxyPoolEntry<T> borrowEntry(boolean createNew, long timeout, TimeUnit unit)
			throws InterruptedException, TimeoutException, FfanRedisProxyPoolException;

	FfanRedisProxyPoolEntry<T> tryBorrowEntry() throws FfanRedisProxyPoolException;
	
	FfanRedisProxyPoolEntry<T> tryBorrowEntry(boolean createNew) throws FfanRedisProxyPoolException;

	void returnEntry(FfanRedisProxyPoolEntry<T> entry) throws NullPointerException;
	
	void clear();
}
