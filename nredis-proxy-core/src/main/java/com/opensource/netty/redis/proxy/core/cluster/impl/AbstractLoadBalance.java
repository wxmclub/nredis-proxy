/**
 * 
 */
package com.opensource.netty.redis.proxy.core.cluster.impl;

import java.util.List;

import com.opensource.netty.redis.proxy.core.cluster.LoadBalance;
import com.opensource.netty.redis.proxy.core.cluster.impl.support.RedisQuestBean;
import com.opensource.netty.redis.proxy.core.config.LBRedisServerMasterCluster;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerBean;

/**
 * @author liubing
 *
 */
public abstract class AbstractLoadBalance implements LoadBalance {
	

	
	private LBRedisServerMasterCluster ffanRedisServerMasterCluster;
	
	
	/**
	 * @param ffanRedisServerMasterCluster
	 */
	public AbstractLoadBalance() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.cluster.LoadBalance#onRefresh(com.wanda.ffan.redis.proxy.core.config.FfanRedisServerMasterCluster)
	 */
	@Override
	public void onRefresh(
			LBRedisServerMasterCluster ffanRedisServerMasterCluster) {
		this.ffanRedisServerMasterCluster=ffanRedisServerMasterCluster;
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.cluster.LoadBalance#select(com.wanda.ffan.redis.proxy.core.cluster.impl.support.RedisQuestBean)
	 */
	@Override
	public LBRedisServerBean select(RedisQuestBean redisQuestBean,LBRedisServerBean ffanRedisMasterServer) {
		if(redisQuestBean.isWrite()&&ffanRedisMasterServer==null){//写
			List<LBRedisServerBean> ffanRedisServerBeans=ffanRedisServerMasterCluster.getMasters();
			if(ffanRedisServerBeans.size()>1){//默认第一个
				LBRedisServerBean ffanRedisServerBean=doSelect(redisQuestBean,ffanRedisServerBeans);
				return ffanRedisServerBean;
			}else if(ffanRedisServerBeans.size()==1){
				return ffanRedisServerBeans.get(0);
			}
		}else if(!redisQuestBean.isWrite()&&ffanRedisMasterServer!=null){//选取从
			List<LBRedisServerBean> ffanRedisClusterServerBeans=ffanRedisServerMasterCluster.getMasterFfanRedisServerBean(ffanRedisMasterServer.getKey());
			if(ffanRedisClusterServerBeans.size()>1){//默认第一个
				LBRedisServerBean ffanRedisServerBean=doSelect(redisQuestBean,ffanRedisClusterServerBeans);
				return ffanRedisServerBean;
			}else if(ffanRedisClusterServerBeans.size()==1){
				return ffanRedisClusterServerBeans.get(0);
			}
		}
		return null;
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
	
	/**
	 * 选取策略
	 * @param redisQuestBean
	 * @return
	 */
	protected abstract LBRedisServerBean doSelect(RedisQuestBean redisQuestBean,List<LBRedisServerBean> ffanRedisMasterServers);
	
}
