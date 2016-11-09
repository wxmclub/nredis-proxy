/**
 * 
 */
package com.opensource.netty.redis.proxy.core.cluster;

import com.opensource.netty.redis.proxy.core.cluster.impl.support.RedisQuestBean;
import com.opensource.netty.redis.proxy.core.config.FfanRedisServerMasterCluster;
import com.opensource.netty.redis.proxy.core.config.support.FfanRedisServerBean;

/**
 * 均衡轮询接口
 * @author liubing
 *
 */
public interface LoadBalance {
	
	/**
	 * 刷新
	 */
	public void onRefresh(FfanRedisServerMasterCluster ffanRedisServerMasterCluster);
	
	/**
	 * 选取策略
	 * @param redisQuestBean
	 * @return
	 */
	public FfanRedisServerBean select(RedisQuestBean redisQuestBean,FfanRedisServerBean ffanRedisMasterServer );
	
	/**
	 * 设置
	 * @param ffanRedisServerMasterCluster
	 */
	public void setFfanRedisServerMasterCluster(FfanRedisServerMasterCluster ffanRedisServerMasterCluster); 
}
