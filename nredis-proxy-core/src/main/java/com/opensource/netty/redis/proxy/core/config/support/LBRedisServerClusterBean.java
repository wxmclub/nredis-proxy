/**
 * 
 */
package com.opensource.netty.redis.proxy.core.config.support;

import java.util.List;

import com.opensource.netty.redis.proxy.core.cluster.LoadBalance;

/**
 * 一主多从
 * @author liubing
 *
 */
public class LBRedisServerClusterBean {
	
	private LBRedisServerBean ffanMasterRedisServerBean;//主
	
	private List<LBRedisServerBean> ffanRedisServerClusterBeans;//从
	
	private LoadBalance loadClusterBalance;//从权重
	/**
	 * 
	 */
	public LBRedisServerClusterBean() {
		super();
	}

	/**
	 * @return the ffanMasterRedisServerBean
	 */
	public LBRedisServerBean getFfanMasterRedisServerBean() {
		return ffanMasterRedisServerBean;
	}

	/**
	 * @param ffanMasterRedisServerBean the ffanMasterRedisServerBean to set
	 */
	public void setFfanMasterRedisServerBean(
			LBRedisServerBean ffanMasterRedisServerBean) {
		this.ffanMasterRedisServerBean = ffanMasterRedisServerBean;
	}

	/**
	 * @return the ffanRedisServerClusterBeans
	 */
	public List<LBRedisServerBean> getFfanRedisServerClusterBeans() {
		return ffanRedisServerClusterBeans;
	}

	/**
	 * @param ffanRedisServerClusterBeans the ffanRedisServerClusterBeans to set
	 */
	public void setFfanRedisServerClusterBeans(
			List<LBRedisServerBean> ffanRedisServerClusterBeans) {
		this.ffanRedisServerClusterBeans = ffanRedisServerClusterBeans;
	}

	/**
	 * @return the loadClusterBalance
	 */
	public LoadBalance getLoadClusterBalance() {
		return loadClusterBalance;
	}

	/**
	 * @param loadClusterBalance the loadClusterBalance to set
	 */
	public void setLoadClusterBalance(LoadBalance loadClusterBalance) {
		this.loadClusterBalance = loadClusterBalance;
	}
	
	
	
}
