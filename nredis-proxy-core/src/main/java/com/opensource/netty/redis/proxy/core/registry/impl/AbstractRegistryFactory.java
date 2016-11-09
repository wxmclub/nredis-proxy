/**
 * 
 */
package com.opensource.netty.redis.proxy.core.registry.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.opensource.netty.redis.proxy.commons.constants.FfanRedisProxyErrorMsgConstant;
import com.opensource.netty.redis.proxy.commons.exception.FfanRedisProxyFrameworkException;
import com.opensource.netty.redis.proxy.core.registry.Registry;
import com.opensource.netty.redis.proxy.core.registry.RegistryFactory;
import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;

/**
 * @author liubing
 * 
 */
public abstract class AbstractRegistryFactory implements RegistryFactory {
	
	private static ConcurrentHashMap<String, Registry> registries = new ConcurrentHashMap<String, Registry>();

    private static final ReentrantLock lock = new ReentrantLock();

    protected String getRegistryUri(RedisProxyURL url) {
        String registryUri = url.getServerKey();
        return registryUri;
    }
	
	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.registry.RegistryFactory#getRegistry(com.wanda.ffan.redis.proxy.core.url.RedisProxyURL)
	 */
	@Override
	public Registry getRegistry(RedisProxyURL redisProxyURL) {
		String registryUri = getRegistryUri(redisProxyURL);
        try {
            lock.lock();
            Registry registry = registries.get(registryUri);
            if (registry != null) {
                return registry;
            }
            registry = createRegistry(redisProxyURL);
            if (registry == null) {
                throw new FfanRedisProxyFrameworkException("Create registry false for url:" + registryUri, FfanRedisProxyErrorMsgConstant.FRAMEWORK_INIT_ERROR);
            }
            registries.put(registryUri, registry);
            return registry;
        } catch (Exception e) {
            throw new FfanRedisProxyFrameworkException("Create registry false for url:" + registryUri, e, FfanRedisProxyErrorMsgConstant.FRAMEWORK_INIT_ERROR);
        } finally {
            lock.unlock();
        }
	}
	
	protected abstract Registry createRegistry(RedisProxyURL url);
}
