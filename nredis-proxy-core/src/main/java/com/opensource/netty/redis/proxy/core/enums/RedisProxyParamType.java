/**
 * 
 */
package com.opensource.netty.redis.proxy.core.enums;

import com.opensource.netty.redis.proxy.commons.constants.RedisConstants;

/**
 * @author liubing
 *
 */
public enum RedisProxyParamType {
	
	/** request timeout **/
    requestTimeout("requestTimeout", 200),
    /** request id from http interface **/
    requestIdFromClient("requestIdFromClient", 0),
    /** connect timeout **/
    connectTimeout("connectTimeout", 1000),
    /** service min worker threads **/
    minWorkerThread("minWorkerThread", 20),
    /** service max worker threads **/
    maxWorkerThread("maxWorkerThread", 200),
    /** pool min conn number **/
    minClientConnection("minClientConnection", 2),
    /** pool max conn number **/
    maxClientConnection("maxClientConnection", 50),
    /** pool max conn number **/
    maxContentLength("maxContentLength", 10 * 1024 * 1024),
    /** max server conn (all clients conn) **/
    maxServerConnection("maxServerConnection", 100000),
    /** pool conn manger stragy **/
    poolLifo("poolLifo", true),
    check("check", "true"), 
    registryRetryPeriod("registryRetryPeriod", 30 * RedisConstants.SECOND_MILLS),
    registrySessionTimeout("registrySessionTimeout", 1 * RedisConstants.MINUTE_MILLS),

    
    /** multi referer share the same channel **/
    shareChannel("shareChannel", false),
    REDISSERVER("REDISSERVER", "{}");
    
    private String name;
    private String value;
    private long longValue;
    private int intValue;
    private boolean boolValue;

    private RedisProxyParamType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private RedisProxyParamType(String name, long longValue) {
        this.name = name;
        this.value = String.valueOf(longValue);
        this.longValue = longValue;
    }

    private RedisProxyParamType(String name, int intValue) {
        this.name = name;
        this.value = String.valueOf(intValue);
        this.intValue = intValue;
    }

    private RedisProxyParamType(String name, boolean boolValue) {
        this.name = name;
        this.value = String.valueOf(boolValue);
        this.boolValue = boolValue;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public int getIntValue() {
        return intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public boolean getBooleanValue() {
        return boolValue;
    }
}
