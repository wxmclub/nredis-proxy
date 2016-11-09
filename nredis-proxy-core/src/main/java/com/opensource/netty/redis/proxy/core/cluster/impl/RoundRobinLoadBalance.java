/**
 * 
 */
package com.opensource.netty.redis.proxy.core.cluster.impl;

import java.util.List;

import com.opensource.netty.redis.proxy.commons.algorithm.impl.RoundRobinHash;
import com.opensource.netty.redis.proxy.core.cluster.impl.support.RedisQuestBean;
import com.opensource.netty.redis.proxy.core.config.support.FfanRedisServerBean;

/**
 * RoundRobin 权重算法
 * @author liubing
 *
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {

	public RoundRobinLoadBalance() {

	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.cluster.impl.AbstractLoadBalance#doSelect(com.wanda.ffan.redis.proxy.core.cluster.impl.support.RedisQuestBean, java.util.List)
	 */
	@Override
	protected FfanRedisServerBean doSelect(RedisQuestBean redisQuestBean,
			List<FfanRedisServerBean> ffanRedisMasterServers) {
		RoundRobinHash<FfanRedisServerBean> roundRobinHash=new RoundRobinHash<FfanRedisServerBean>(ffanRedisMasterServers);
		FfanRedisServerBean result=roundRobinHash.weightRandom();
		return result;
	}

}
