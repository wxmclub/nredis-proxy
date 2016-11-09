
package com.opensource.netty.redis.proxy.zk.registry;


import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;

import com.opensource.netty.redis.proxy.core.enums.RedisProxyParamType;
import com.opensource.netty.redis.proxy.core.log.impl.LoggerUtils;
import com.opensource.netty.redis.proxy.core.registry.Registry;
import com.opensource.netty.redis.proxy.core.registry.impl.AbstractRegistryFactory;
import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;



/**
 * zk 工厂类
 * @author liubing
 *
 */

public class ZookeeperRegistryFactory extends AbstractRegistryFactory {

    @Override
    protected Registry createRegistry(RedisProxyURL redisProxyURL) {
        try {
            int timeout = redisProxyURL.getIntParameter(RedisProxyParamType.connectTimeout.getName(), RedisProxyParamType.connectTimeout.getIntValue());
            int sessionTimeout =
            		redisProxyURL.getIntParameter(RedisProxyParamType.registrySessionTimeout.getName(),
                    		RedisProxyParamType.registrySessionTimeout.getIntValue());
            ZkClient zkClient = new ZkClient(redisProxyURL.getParameter("address"), sessionTimeout, timeout);
            return new ZookeeperRegistry(redisProxyURL, zkClient);
        } catch (ZkException e) {
            LoggerUtils.error("[ZookeeperRegistry] fail to connect zookeeper, cause: " + e.getMessage());
            throw e;
        }
    }
}
