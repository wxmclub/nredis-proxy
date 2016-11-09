/**
 * 
 */
package com.opensource.netty.redis.proxy.commons.algorithm.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.opensource.netty.redis.proxy.commons.algorithm.impl.support.RedisWeight;

/**
 * @author liubing 权重随机算法
 */
public class RoundRobinHash<T extends RedisWeight> {

	public Map<T, Integer> serverMap = new HashMap<T, Integer>();

	/**
	 * 构造方法
	 */
	public RoundRobinHash(List<T> list) {
		init(list);
	}

	/**
	 * 初始化
	 * 
	 */
	private void init(List<T> list) {
		for (T t : list) {
			RedisWeight redisWeight = (RedisWeight) t;
			serverMap.put(t, redisWeight.getWeight());
		}
	}

	public T weightRandom() {// 随机权重算法
		Set<T> keySet = serverMap.keySet();
		Iterator<T> iterator = keySet.iterator();
		List<T> serverList = new ArrayList<T>();
		while (iterator.hasNext()) {
			T server = iterator.next();
			Integer weight = serverMap.get(server);
			for (int i = 0; i < weight; i++) {
				serverList.add(server);
			}
		}
		if(serverList!=null&&serverList.size()>0){
			Random random = new Random();
			int randomPos = random.nextInt(serverList.size());
			
			T server = serverList.get(randomPos);
			return server;
		}
		return null;

	}
}
