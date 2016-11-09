/**
 * 
 */
package com.opensource.netty.redis.proxy.core.cluster;

import com.opensource.netty.redis.proxy.core.cluster.impl.support.RedisQuestBean;
import com.opensource.netty.redis.proxy.core.config.LBRedisServerMasterCluster;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerBean;

/**
 * 均衡轮询接口
 * @author liubing
 *
 */
public interface LoadBalance {
	
	/**
	 * 刷新
	 */
	public void onRefresh(LBRedisServerMasterCluster ffanRedisServerMasterCluster);
	
	/**
	 * 选取策略
	 * @param redisQuestBean
	 * @return
	 */
	public LBRedisServerBean select(RedisQuestBean redisQuestBean,LBRedisServerBean ffanRedisMasterServer );
	
	/**
	 * 设置
	 * @param ffanRedisServerMasterCluster
	 */
	public void setFfanRedisServerMasterCluster(LBRedisServerMasterCluster ffanRedisServerMasterCluster); 
}
