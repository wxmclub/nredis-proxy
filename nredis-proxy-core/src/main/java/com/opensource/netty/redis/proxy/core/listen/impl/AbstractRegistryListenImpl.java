/**
 * 
 */
package com.opensource.netty.redis.proxy.core.listen.impl;


import com.opensource.netty.redis.proxy.core.config.FfanRedisServerMasterCluster;
import com.opensource.netty.redis.proxy.core.listen.IRegistryListen;

/**
 * @author liubing
 *
 */
public abstract class AbstractRegistryListenImpl implements IRegistryListen {
	
	private FfanRedisServerMasterCluster ffanRedisServerMasterCluster;
	
	
	/**
	 * @param ffanRedisServerMasterCluster
	 */
	public AbstractRegistryListenImpl(
			FfanRedisServerMasterCluster ffanRedisServerMasterCluster) {
		super();
		this.ffanRedisServerMasterCluster = ffanRedisServerMasterCluster;
	}


	/**
	 * @return the ffanRedisServerMasterCluster
	 */
	public FfanRedisServerMasterCluster getFfanRedisServerMasterCluster() {
		return ffanRedisServerMasterCluster;
	}


	/**
	 * @param ffanRedisServerMasterCluster the ffanRedisServerMasterCluster to set
	 */
	public void setFfanRedisServerMasterCluster(
			FfanRedisServerMasterCluster ffanRedisServerMasterCluster) {
		this.ffanRedisServerMasterCluster = ffanRedisServerMasterCluster;
	}

	
	
}
