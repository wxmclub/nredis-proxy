/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.commons;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
/**
 * @author liubing
 *
 */
public class LBRedisProxyPoolEntryState {
	
	private final long startAt = System.currentTimeMillis();

	private AtomicLong lastValidatedAt = new AtomicLong(
			System.currentTimeMillis());

	private AtomicBoolean valid = new AtomicBoolean(true);

	public long getStartAt() {
		return startAt;
	}

	/**
	 * Get the last time the validity {@link JdPoolEntry} has been confirmed.
	 * 
	 * @return lastValidatedAt
	 * */
	public long getLastValidatedAt() {
		return lastValidatedAt.longValue();
	}

	/**
	 * Set lastValidatedAt.
	 * 
	 * @param lastValidatedAt
	 * */
	public void setLastValidatedAt(long lastValidatedAt) {
		this.lastValidatedAt.set(lastValidatedAt);
	}

	/**
	 * Get validity of {@link JdPoolEntry}.
	 * */
	public boolean isValid() {
		return valid.get();
	}

	/**
	 * Compare and set valid
	 * 
	 * @param expect
	 * @param update
	 * @return true, if succeeded
	 * @see AtomicBoolean#compareAndSet(boolean, boolean)
	 * */
	public boolean compareAndSetValid(boolean expect, boolean update) {
		return valid.compareAndSet(expect, update);
	}
}
