/**
 * 
 */
package com.opensource.netty.redis.proxy.sample;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author liubing
 *
 */
public class RedisProxyServerBootStrap {
	
	public static void main(String[] args) {
		 new ClassPathXmlApplicationContext(new String[] {"classpath*:redisProxy.xml"});
	}
}
