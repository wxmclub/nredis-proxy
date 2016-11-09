/**
 * 
 */
package com.opensource.netty.redis.proxy.core.client.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensource.netty.redis.proxy.commons.exception.RedisException;
import com.opensource.netty.redis.proxy.core.command.impl.RedisCommand;
import com.opensource.netty.redis.proxy.core.connection.IConnection;
import com.opensource.netty.redis.proxy.core.connection.IConnectionCallBack;
import com.opensource.netty.redis.proxy.core.enums.RedisProxyParamType;
import com.opensource.netty.redis.proxy.core.pool.utils.FfanRedisProxyChannelPoolUtils;
import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPoolEntry;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPooledObjectFactory;
import com.opensource.netty.redis.proxy.pool.commons.FfanRedisProxyPoolConfig;
import com.opensource.netty.redis.proxy.pool.impl.FfanRedisProxyBasicPool;



/**
 * @author liubing
 *
 */
public abstract class AbstractPoolClient extends AbstractClient{
	
	protected FfanRedisProxyBasicPool<IConnection>  pool;
    protected FfanRedisProxyPoolConfig ffanRedisProxyPoolConfig;
    protected FfanRedisProxyPooledObjectFactory<IConnection> factory;
    protected RedisProxyURL redisProxyURL;
	private Logger logger = LoggerFactory.getLogger(AbstractPoolClient.class);

	/**
	 * 
	 */
	public AbstractPoolClient(RedisProxyURL redisProxyURL) {
		super();
		this.redisProxyURL=redisProxyURL;
	}
    
	protected void initPool() {
		try{
			ffanRedisProxyPoolConfig=new FfanRedisProxyPoolConfig();
			ffanRedisProxyPoolConfig.setMaxActiveEntries(redisProxyURL.getIntParameter(RedisProxyParamType.maxServerConnection.getName(), RedisProxyParamType.maxServerConnection.getIntValue()));
			ffanRedisProxyPoolConfig.setMaxIdleEntries(redisProxyURL.getIntParameter(RedisProxyParamType.maxClientConnection.getName(), RedisProxyParamType.maxClientConnection.getIntValue()));
			ffanRedisProxyPoolConfig.setInitialEntries(redisProxyURL.getIntParameter(RedisProxyParamType.minClientConnection.getName(), RedisProxyParamType.minClientConnection.getIntValue()));
			ffanRedisProxyPoolConfig.setMinIdleEntries(redisProxyURL.getIntParameter(RedisProxyParamType.minClientConnection.getName(), RedisProxyParamType.minClientConnection.getIntValue()));
			
            factory = createChannelFactory();
            pool = FfanRedisProxyChannelPoolUtils.createPool(ffanRedisProxyPoolConfig, factory);
		}catch(Exception e){
			logger.error("initPool fail,reason:"+e.getCause()+",message:"+e.getMessage(), e);
		}
	}
	
	/**
	 * 创建一个工厂类
	 * @return
	 */
    protected abstract FfanRedisProxyPooledObjectFactory<IConnection> createChannelFactory();
    
    public abstract void write(RedisCommand request,IConnectionCallBack connectionCallBack);
    
    protected FfanRedisProxyPoolEntry<IConnection> borrowObject() throws Exception {
    	FfanRedisProxyPoolEntry<IConnection> nettyChannelEntry=pool.borrowEntry();
        if (nettyChannelEntry != null&&nettyChannelEntry.getObject()!=null) {
            return nettyChannelEntry;
        }
        
        String errorMsg = this.getClass().getSimpleName() + " borrowObject Error";
        logger.error(errorMsg);
        throw new RedisException(errorMsg);
    }


    protected void returnObject(FfanRedisProxyPoolEntry<IConnection> entry) {
        if (entry == null) {
            return;
        }
        try {
        	pool.returnEntry(entry);
        } catch (Exception ie) {
        	logger.error(this.getClass().getSimpleName() + " return client Error" , ie);
        }
    }

	/**
	 * @return the redisProxyURL
	 */
	public RedisProxyURL getRedisProxyURL() {
		return redisProxyURL;
	}
    
    
}
