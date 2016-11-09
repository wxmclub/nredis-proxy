/**
 * 
 */
package com.opensource.netty.redis.proxy.sample;

import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;

/**
 * @author liubing
 *
 */
public class RedisClientDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Jedis  redis = new Jedis ("127.0.0.1",6379);
		System.out.println(redis.get("classRoom"));
		ClassRoom classRoom=new ClassRoom("liubing", "168", 120);
		redis.set("classRoom", JSONObject.toJSONString(classRoom));
		//redis.expire("classRoom", 100);
		//redis.close();
//		redis.slaveofNoOne();
		//System.out.println(redis.get("name"));
		
		
		//redis.set("name", "liubing1");
		//redis.expire("name", 10);
		
//		for(int z=0;z<5;z++){
//			long startTime=System.currentTimeMillis();
//			for(int j=0;j<1000;j++){
//				for(int i=0;i<100;i++){
//					//System.out.println(i);
//					redis.get("name");
//					//redis.set("name", "liubing1");
//				}
//			}
//			long endTime=System.currentTimeMillis();
//			System.out.println("完成时间:"+(endTime-startTime));
//		}
		
	}

}
