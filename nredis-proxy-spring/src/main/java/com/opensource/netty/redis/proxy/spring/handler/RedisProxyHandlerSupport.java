/**
 * 
 */
package com.opensource.netty.redis.proxy.spring.handler;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.opensource.netty.redis.proxy.spring.schema.RedisProxyNodeParser;
import com.opensource.netty.redis.proxy.spring.schema.support.RedisProxyConstant;


/**
 * @author liubing
 *
 */
public class RedisProxyHandlerSupport extends NamespaceHandlerSupport {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
	 */
	@Override
	public void init() {
//		registerBeanDefinitionParser(RedisProxyConstant.REDISPROXYCLUSTER, new RedisProxyClusterParser());
//		registerBeanDefinitionParser(RedisProxyConstant.REDISPROXYMASTER, new RedisProxyMasterParser());
		registerBeanDefinitionParser(RedisProxyConstant.REDISPROXYNODE, new RedisProxyNodeParser());
	}

}
