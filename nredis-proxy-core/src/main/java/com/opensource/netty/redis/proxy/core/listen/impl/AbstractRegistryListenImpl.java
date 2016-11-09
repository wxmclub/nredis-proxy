/**
 * 
 */
package com.opensource.netty.redis.proxy.core.listen.impl;


import com.opensource.netty.redis.proxy.core.config.LBRedisServerMasterCluster;
import com.opensource.netty.redis.proxy.core.listen.IRegistryListen;

/**
 * @author liubing
 *
 */
public abstract class AbstractRegistryListenImpl implements IRegistryListen {
	
	private LBRedisServerMasterCluster ffanRedisServerMasterCluster;
	
	
	/**
	 * @param ffanRedisServerMasterCluster
	 */
	public AbstractRegistryListenImpl(
			LBRedisServerMasterCluster ffanRedisServerMasterCluster) {
		super();
		this.ffanRedisServerMasterCluster = ffanRedisServerMasterCluster;
	}


	/**
	 * @return the ffanRedisServerMasterCluster
	 */
	public LBRedisServerMasterCluster getFfanRedisServerMasterCluster() {
		return ffanRedisServerMasterCluster;
	}


	/**
	 * @param ffanRedisServerMasterCluster the ffanRedisServerMasterCluster to set
	 */
	public void setFfanRedisServerMasterCluster(
			LBRedisServerMasterCluster ffanRedisServerMasterCluster) {
		this.ffanRedisServerMasterCluster = ffanRedisServerMasterCluster;
	}

	
	
}
