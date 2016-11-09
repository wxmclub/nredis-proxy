/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.commons;

import com.opensource.netty.redis.proxy.pool.exception.LBRedisProxyPoolPropertyValidationException;


/**
 * @author liubing
 *
 */
public class LBRedisProxyPoolConfig {
	
	
	private static byte WAIT_UNLIMIT_ON_BORROW = 0;

	private String host;

	private int port;
	
	private int maxActiveEntries = 8;

	private int initialEntries = 0;

	private int maxIdleEntries = 8;

	private long maxWaitMillisOnBorrow = 500; // 0 is forever wait to borrow.

	private int invalidateThreads = 0;

	private long invalidateThreadInitialDelayMillis = 500;

	private long invalidateIntervalMillis = 500;

	private int minIdleEntries = 0;

	private int ensureThreads = 0;

	private long ensureIntervalMillis = 500;

	public void validatePropCorrelation() throws LBRedisProxyPoolPropertyValidationException {
		if (maxActiveEntries < initialEntries)
			throw new LBRedisProxyPoolPropertyValidationException(
					"maxActiveEntries < initialEntries");
		if (maxActiveEntries < maxIdleEntries)
			throw new LBRedisProxyPoolPropertyValidationException(
					"maxActiveEntries < maxIdleEntries");

		if (maxActiveEntries < minIdleEntries)
			throw new LBRedisProxyPoolPropertyValidationException(
					"maxActiveEntries < minIdleEntries");
		if (maxIdleEntries < minIdleEntries)
			throw new LBRedisProxyPoolPropertyValidationException(
					"maxIdleEntries < minIdleEntries");
	}

	public boolean isWaitUnlimitOnBorrow() {
		return maxWaitMillisOnBorrow == WAIT_UNLIMIT_ON_BORROW;
	}

	public boolean isInvalidateInBackground() {
		return (invalidateThreads > 0);
	}

	public boolean isEnsureInBackground() {
		return (ensureThreads > 0);
	}

	public int getMaxActiveEntries() {
		return maxActiveEntries;
	}

	public void setMaxActiveEntries(int maxActiveEntries) {
		this.maxActiveEntries = maxActiveEntries;
	}

	public int getInitialEntries() {
		return initialEntries;
	}

	public void setInitialEntries(int initialEntries) {
		this.initialEntries = initialEntries;
	}

	public long getMaxWaitMillisOnBorrow() {
		return maxWaitMillisOnBorrow;
	}

	public void setMaxWaitMillisOnBorrow(long maxWaitMillisOnBorrow) {
		this.maxWaitMillisOnBorrow = maxWaitMillisOnBorrow;
	}

	public int getMaxIdleEntries() {
		return maxIdleEntries;
	}

	public void setMaxIdleEntries(int maxIdleEntries) {
		this.maxIdleEntries = maxIdleEntries;
	}

	public int getMinIdleEntries() {
		return minIdleEntries;
	}

	public void setMinIdleEntries(int minIdleEntries) {
		this.minIdleEntries = minIdleEntries;
	}

	public int getInvalidateThreads() {
		return invalidateThreads;
	}

	public void setInvalidateThreads(int invalidateThreads) {
		this.invalidateThreads = invalidateThreads;
	}

	public long getInvalidateThreadInitialDelayMillis() {
		return invalidateThreadInitialDelayMillis;
	}

	public void setInvalidateThreadInitialDelayMillis(
			long invalidateThreadInitialDelayMillis) {
		this.invalidateThreadInitialDelayMillis = invalidateThreadInitialDelayMillis;
	}

	public long getInvalidateIntervalMillis() {
		return invalidateIntervalMillis;
	}

	public void setInvalidateIntervalMillis(long invalidateIntervalMillis) {
		this.invalidateIntervalMillis = invalidateIntervalMillis;
	}

	public int getEnsureThreads() {
		return ensureThreads;
	}

	public void setEnsureThreads(int ensureThreads) {
		this.ensureThreads = ensureThreads;
	}

	public long getEnsureIntervalMillis() {
		return ensureIntervalMillis;
	}

	public void setEnsureIntervalMillis(long ensureIntervalMillis) {
		this.ensureIntervalMillis = ensureIntervalMillis;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
