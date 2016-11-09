/**
 * 
 */
package com.opensource.netty.redis.proxy.core.cluster.impl;

import java.util.List;

import com.opensource.netty.redis.proxy.commons.algorithm.Hashing;
import com.opensource.netty.redis.proxy.commons.algorithm.impl.ConsistentHash;
import com.opensource.netty.redis.proxy.commons.algorithm.impl.MurmurHash;
import com.opensource.netty.redis.proxy.core.cluster.impl.support.RedisQuestBean;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerBean;

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
	protected LBRedisServerBean doSelect(RedisQuestBean redisQuestBean,
			List<LBRedisServerBean> ffanRedisMasterServers) {
		Hashing hashFunction = new MurmurHash(); // hash函数实例
		ConsistentHash<LBRedisServerBean> consistentHash=new ConsistentHash<LBRedisServerBean>(hashFunction, ffanRedisMasterServers.size(), ffanRedisMasterServers);
		LBRedisServerBean ffanRedisServerBean=consistentHash.getBytes(redisQuestBean.getKey());
		return ffanRedisServerBean;
	}
	
}
