/**
 * 
 */
package com.opensource.netty.redis.proxy.core.registry.impl.support;

import com.opensource.netty.redis.proxy.commons.constants.RedisConstants;
import com.opensource.netty.redis.proxy.commons.exception.LBRedisProxyFrameworkException;
import com.opensource.netty.redis.proxy.commons.utils.StringUtils;
import com.opensource.netty.redis.proxy.core.enums.ZkNodeType;
import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;


/**
 * ZK 工具类
 * @author liubing
 *
 */
public class ZkUtils {
	
	public static String toServicePath(RedisProxyURL url) {
        return RedisConstants.ZOOKEEPER_REGISTRY_NAMESPACE  ;
    }
	


    public static String toCommandPath(RedisProxyURL url) {
        return toServicePath(url) + RedisConstants.ZOOKEEPER_REGISTRY_COMMAND;
    }

    public static String toNodeTypePath(RedisProxyURL url, ZkNodeType nodeType) {
        String type;
        if (nodeType == ZkNodeType.AVAILABLE_SERVER) {
            type = "server";
        } else if (nodeType == ZkNodeType.UNAVAILABLE_SERVER) {
            type = "unavailableServer";
        } else if (nodeType == ZkNodeType.CLIENT) {
            type = "client";
        } else {
            throw new LBRedisProxyFrameworkException(String.format("Failed to get nodeTypePath, url: %s type: %s", url, nodeType.toString()));
        }
        return toCommandPath(url) + RedisConstants.PATH_SEPARATOR + type;
    }

    public static String toNodePath(RedisProxyURL url,String parentPath, ZkNodeType nodeType) {
    	if(StringUtils.isNotBlank(parentPath)){
    		return toNodeTypePath(url, nodeType)+ RedisConstants.PATH_SEPARATOR+parentPath+RedisConstants.PATH_SEPARATOR +url.getServerKey();
    	}
        return toNodeTypePath(url, nodeType)+ RedisConstants.PATH_SEPARATOR +url.getServerKey();
    }
    
}
