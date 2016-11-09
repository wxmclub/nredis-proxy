/**
 * 
 */
package com.opensource.netty.redis.proxy.core.registry;

import java.util.List;

import com.opensource.netty.redis.proxy.core.listen.IRegistryListen;
import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;

/**
 * @author liubing
 *
 */
public interface RegistryService {
	
	/**
	 * 注册监听值的变化 master节点
	 * @param redisProxyURL
	 */
	void register(RedisProxyURL redisProxyURL,IRegistryListen registryListen);
	
	/**
	 * 不注册监听值的变化 master 节点
	 * 
	 * @param redisProxyURL
	 */
	void unregister(RedisProxyURL redisProxyURL);
	
	/**
	 * 创建一个节点 cluster节点
	 * @param redisProxyURL
	 * @param parentPath
	 * @param value
	 */
	void createPersistent(RedisProxyURL redisProxyURL,String value);
	
	/**
	 * 删除节点
	 * @param redisProxyURL
	 * @param parentPath
	 * @return
	 */
	boolean delete(RedisProxyURL redisProxyURL,String parentPath);
	
	/**
	 * 获取子段路径
	 * @param parentPath
	 * @return
	 */
	public List<String> getChildren(String parentPath);
	
	/**
	 * 
	 * @param path
	 * @param flag
	 * @return
	 */
	public String readData(String path,boolean flag);
}
