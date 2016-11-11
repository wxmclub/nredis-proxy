package com.opensource.netty.redis.proxy.monitor.spring.helper;

import com.opensource.netty.redis.proxy.monitor.spring.bean.RedisMonitorConfiguration;

/**
 * RedisMonitorConfigurationHelper获取RedisMonitorConfiguration实例
 *
 * @author wangyang
 */
public class RedisMonitorConfigurationHelper {
    private static class InstanceHolder {
        private static final RedisMonitorConfigurationHelper instance = new RedisMonitorConfigurationHelper();
    }

    public static RedisMonitorConfigurationHelper getInstance() {
        return InstanceHolder.instance;
    }


    private static final RedisMonitorConfiguration redisMonitorConfiguration = SpringFactory.getBean("redisMonitorConfiguration");


    public RedisMonitorConfiguration getRedisMonitorConfigurationInstance() {
        return redisMonitorConfiguration;
    }


}
