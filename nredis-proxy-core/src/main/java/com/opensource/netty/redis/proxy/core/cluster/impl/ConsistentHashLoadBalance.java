/**
 * 
 */
package com.opensource.netty.redis.proxy.core.cluster.impl;

import java.util.List;

import com.opensource.netty.redis.proxy.commons.algorithm.Hashing;
import com.opensource.netty.redis.proxy.commons.algorithm.impl.ConsistentHash;
import com.opensource.netty.redis.proxy.commons.algorithm.impl.MurmurHash;
import com.opensource.netty.redis.proxy.core.cluster.impl.support.RedisQuestBean;
import com.opensource.netty.redis.proxy.core.config.support.FfanRedisServerBean;

/**
 * 一致性hash
 * @author liubing
 *
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance {
	
	
	public ConsistentHashLoadBalance() {
		
	}
	
	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.cluster.impl.AbstractLoadBalance#doSelect(com.wanda.ffan.redis.proxy.core.cluster.impl.support.RedisQuestBean, java.util.List)
	 */
	@Override
	protected FfanRedisServerBean doSelect(RedisQuestBean redisQuestBean,
			List<FfanRedisServerBean> ffanRedisMasterServers) {
		Hashing hashFunction = new MurmurHash(); // hash函数实例
		ConsistentHash<FfanRedisServerBean> consistentHash=new ConsistentHash<FfanRedisServerBean>(hashFunction, ffanRedisMasterServers.size(), ffanRedisMasterServers);
		FfanRedisServerBean ffanRedisServerBean=consistentHash.getBytes(redisQuestBean.getKey());
		return ffanRedisServerBean;
	}
	
}
