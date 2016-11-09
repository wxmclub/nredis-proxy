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
public interface FfanRedisProxyIdleEntriesQueue<T extends Pool> {
	
	/**
	 * 
	 * @return
	 */
	FfanRedisProxyPoolEntry<T> poll();

	/**
	 * 
	 * @param entry
	 * @return
	 * @throws NullPointerException
	 */
	boolean offer(FfanRedisProxyPoolEntry<T> entry) throws NullPointerException;
	
	
	public int getIdleEntriesCount();
	
	
	public void clear();
}
