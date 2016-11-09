/**
 * 
 */
package com.opensource.netty.redis.proxy.pool;

import com.opensource.netty.redis.proxy.pool.commons.Pool;


/**
 * 空闲队列接口
 * @author liubing
 *
 */
public interface LBRedisProxyIdleEntriesQueue<T extends Pool> {
	
	/**
	 * 
	 * @return
	 */
	LBRedisProxyPoolEntry<T> poll();

	/**
	 * 
	 * @param entry
	 * @return
	 * @throws NullPointerException
	 */
	boolean offer(LBRedisProxyPoolEntry<T> entry) throws NullPointerException;
	
	
	public int getIdleEntriesCount();
	
	
	public void clear();
}
