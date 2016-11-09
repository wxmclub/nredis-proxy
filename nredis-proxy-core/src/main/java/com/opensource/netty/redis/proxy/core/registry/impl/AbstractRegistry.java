/**
 * 
 */
package com.opensource.netty.redis.proxy.core.registry.impl;

import java.util.Set;

import com.opensource.netty.redis.proxy.commons.utils.ConcurrentHashSet;
import com.opensource.netty.redis.proxy.core.listen.IRegistryListen;
import com.opensource.netty.redis.proxy.core.log.impl.LoggerUtils;
import com.opensource.netty.redis.proxy.core.registry.Registry;
import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;

/**
 * @author liubing
 *
 */
public abstract class AbstractRegistry implements Registry {

	private Set<RedisProxyURL> registeredServiceUrls = new ConcurrentHashSet<RedisProxyURL>();
	protected String registryClassName = this.getClass().getSimpleName();
	private RedisProxyURL redisProxyURL;

	public AbstractRegistry(RedisProxyURL redisProxyURL) {
		this.redisProxyURL = redisProxyURL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wanda.ffan.redis.proxy.core.registry.RegistryService#register(com
	 * .wanda.ffan.redis.proxy.core.url.RedisProxyURL)
	 */
	@Override
	public void register(RedisProxyURL url,IRegistryListen registryListen) {
		 if (url == null) {
	            LoggerUtils.warn("[{}] register with malformed param, url is null", registryClassName);
	            return;
	        }
	        LoggerUtils.info("[{}] Url ({}) will register to Registry [{}]", registryClassName, url, redisProxyURL.getServerKey());
	        doRegister(url,registryListen);
	        registeredServiceUrls.add(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wanda.ffan.redis.proxy.core.registry.RegistryService#unregister(com
	 * .wanda.ffan.redis.proxy.core.url.RedisProxyURL)
	 */
	@Override
	public void unregister(RedisProxyURL url) {
		if (url == null) {
            LoggerUtils.warn("[{}] unregister with malformed param, url is null", registryClassName);
            return;
        }
        LoggerUtils.info("[{}] Url ({}) will unregister to Registry [{}]", registryClassName, url, redisProxyURL.getServerKey());
        doUnregister(url);
        registeredServiceUrls.remove(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wanda.ffan.redis.proxy.core.registry.RegistryService#createPersistent
	 * (com.wanda.ffan.redis.proxy.core.url.RedisProxyURL, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void createPersistent(RedisProxyURL redisProxyURL,String value) {
		if (redisProxyURL == null) {
            LoggerUtils.warn("[{}]  createPersistent param, url is null", registryClassName);
            return;
        }
        LoggerUtils.info("[{}] Url ({}) will createPersistent,parentPath {} ,value,{}", registryClassName, redisProxyURL,redisProxyURL.getParentServerPath(), value);
        doCreatePersistent(redisProxyURL, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wanda.ffan.redis.proxy.core.registry.RegistryService#delete(com.wanda
	 * .ffan.redis.proxy.core.url.RedisProxyURL, java.lang.String)
	 */
	@Override
	public boolean delete(RedisProxyURL redisProxyURL, String parentPath) {
		if (redisProxyURL == null) {
            LoggerUtils.warn("[{}] delete, url is null", registryClassName);
            return false;
        }
		return doDelete(redisProxyURL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wanda.ffan.redis.proxy.core.registry.Registry#getRedisProxyURL()
	 */
	@Override
	public RedisProxyURL getRedisProxyURL() {
		return redisProxyURL;
	}

	protected abstract void doRegister(RedisProxyURL url,IRegistryListen registryListen);

	protected abstract void doUnregister(RedisProxyURL url);

	protected abstract boolean doDelete(RedisProxyURL redisProxyURL);

	protected abstract void doCreatePersistent(RedisProxyURL redisProxyURL,String value);
}
