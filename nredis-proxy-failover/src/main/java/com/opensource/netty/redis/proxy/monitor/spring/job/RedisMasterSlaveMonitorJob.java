/**
 * 
 */
package com.opensource.netty.redis.proxy.monitor.spring.job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerBean;
import com.opensource.netty.redis.proxy.core.enums.ZkNodeType;
import com.opensource.netty.redis.proxy.core.registry.impl.support.ZkUtils;
import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;
import com.opensource.netty.redis.proxy.monitor.spring.bean.RedisMonitorConfiguration;
import com.opensource.netty.redis.proxy.monitor.spring.helper.RedisMonitorConfigurationHelper;
import com.opensource.netty.redis.proxy.monitor.spring.helper.SuperClassReflectionUtils;

/**
 * @author liubing
 * 监控从
 */
public class RedisMasterSlaveMonitorJob implements InitializingBean {
	
	private Logger logger = Logger.getLogger("RedisMasterSlaveMonitorJob");

    private String zkHost;
    
    public static final int TRY_TIMES = 1;
    
    private int connectTimeout;
    
    private ZkClient zkClient;
	
    
    /**
     * 监控主从
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void monitorMasterSlaves()throws NoSuchFieldException, IllegalAccessException {
    	updateSlaveRedisConfiguration();
    	updateMasterRedisConfiguration();
    }
    /**
     * 监测从redis连接池
     *
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void updateSlaveRedisConfiguration() throws NoSuchFieldException, IllegalAccessException {
        logger.info("monitor  slave start...");
        RedisMonitorConfiguration redisMonitorConfiguration = RedisMonitorConfigurationHelper.getInstance().getRedisMonitorConfigurationInstance();
        List<JedisPool> masterShardInfos = redisMonitorConfiguration.getMasterShardInfos();
        
        logger.info("masterShardInfos size is:" + masterShardInfos.size());
        if (CollectionUtils.isEmpty(masterShardInfos)) {
            return;
        }
        Map<String, ArrayList<JedisPool>> clusterShardInfos = redisMonitorConfiguration.getClusterShardInfos();
        for (JedisPool masterJedisPool : masterShardInfos) {
                	
        			AtomicReference<HostAndPort> hostAndPort= getHostAndPort(masterJedisPool);
        			if(hostAndPort==null){
        				continue;
        			}
                    String masterHostAndPort=String.valueOf(hostAndPort.get());
                    ArrayList<JedisPool> clusterJedisPools = clusterShardInfos.get(masterHostAndPort);
                    if(clusterJedisPools==null||clusterJedisPools.size()==0){
                    	continue;
                    }
                    Iterator<JedisPool> clusterJedisPoolIterator = clusterJedisPools.iterator();
                    if (!CollectionUtils.isEmpty(clusterJedisPools)) {
                        while (clusterJedisPoolIterator.hasNext()) {
                            JedisPool clusterJedisPool = clusterJedisPoolIterator.next();
                            boolean isClusterCrash = getRedisCrashFlag(clusterJedisPool, TRY_TIMES);
                            AtomicReference<HostAndPort> slaveHostAndPort= getHostAndPort(clusterJedisPool);
                			if(slaveHostAndPort==null){
                				continue;
                			}
                            RedisProxyURL parentRedisProxyURL = new RedisProxyURL(hostAndPort.get().getHost(), Integer.valueOf(hostAndPort.get().getPort()), connectTimeout);
                            String parentPath = ZkUtils.toNodePath(parentRedisProxyURL, null, ZkNodeType.AVAILABLE_SERVER);
                            String childPath = new StringBuilder(parentPath).append("/").append(slaveHostAndPort.get().getHost()).append(":").append(slaveHostAndPort.get().getPort()).toString();
                            if (isClusterCrash) {
                                logger.info("cluster crash, delete child path...");
                                //删除map中的从（RedisMonitorConfiguration）
                                zkClient.delete(childPath);
                                logger.info("cluster crash, remove child shard info from clusterShardInfos...");
                                clusterJedisPoolIterator.remove();
                                continue;
                            }
                        }
                    }
                }
    }
    
    /**
     * 监测主redis连接池
     *
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void updateMasterRedisConfiguration() throws NoSuchFieldException, IllegalAccessException {
        logger.info("monitor  start...");
        RedisMonitorConfiguration redisMonitorConfiguration = RedisMonitorConfigurationHelper.getInstance().getRedisMonitorConfigurationInstance();
        List<JedisPool> masterShardInfos = redisMonitorConfiguration.getMasterShardInfos();
        logger.info("masterShardInfos size is:" + masterShardInfos.size());
        if (CollectionUtils.isEmpty(masterShardInfos)) {
            return;
        }
        Map<String, ArrayList<JedisPool>> clusterShardInfos = redisMonitorConfiguration.getClusterShardInfos();
        for (JedisPool masterJedisPool : masterShardInfos) {
            boolean isMasterCrash = getRedisCrashFlag(masterJedisPool, TRY_TIMES);
            logger.info("is master crash?,flag:" + isMasterCrash);
            if (isMasterCrash) {//主挂了
            	    AtomicReference<HostAndPort> hostAndPort= getHostAndPort(masterJedisPool);
            	    if(hostAndPort==null){
        				continue;
        			}
                    String masterHostAndPort = String.valueOf(hostAndPort.get());
                    ArrayList<JedisPool> clusterJedisPools = clusterShardInfos.get(masterHostAndPort);
                    Iterator<JedisPool> clusterJedisPoolIterator = clusterJedisPools.iterator();
                    if (!CollectionUtils.isEmpty(clusterJedisPools)) {
                    	Jedis clusterSelectedClient =null;
                        while (clusterJedisPoolIterator.hasNext()) {
                            JedisPool clusterJedisPool = clusterJedisPoolIterator.next();
                            boolean isClusterCrash = getRedisCrashFlag(clusterJedisPool, TRY_TIMES);//检查是否完好
                            if(isClusterCrash){//从挂了，下次循环
                            	continue;
                            }
                            AtomicReference<HostAndPort> slaveHostAndPort= getHostAndPort(clusterJedisPool);
                			if(slaveHostAndPort==null){
                				continue;
                			}
                            RedisProxyURL parentRedisProxyURL = new RedisProxyURL(hostAndPort.get().getHost(), Integer.valueOf(hostAndPort.get().getPort()), connectTimeout);
                            String parentPath = ZkUtils.toNodePath(parentRedisProxyURL, null, ZkNodeType.AVAILABLE_SERVER);
                            String childPath = new StringBuilder(parentPath).append("/").append(slaveHostAndPort.get().getHost()).append(":").append(slaveHostAndPort.get().getPort()).toString();
                            if (zkClient.exists(parentPath)) {
                                    String childData = zkClient.readData(childPath, true);
                                    LBRedisServerBean childFfanRedisServerBean = JSONObject.parseObject(childData, LBRedisServerBean.class);
                                    //将从的data设置的主的parentPath
                                    logger.info("write child data to parent path...");
                                    zkClient.writeData(parentPath, JSON.toJSONString(childFfanRedisServerBean));
                                    //删除从的childPath
                                    logger.info("delete child path...");
                                    zkClient.delete(childPath);
                                    //将从设置为主（RedisMonitorConfiguration）
                                    logger.info("update masterShardInfos...");
                                    logger.info("remove master jedis pool from masterShardInfos");
                                    if (masterShardInfos.contains(masterJedisPool)) {
                                        masterShardInfos.remove(masterJedisPool);
                                    }
                                    masterShardInfos.add(clusterJedisPool);
                                    //删除map中的从（RedisMonitorConfiguration）
                                    logger.info("remove child shard info from clusterShardInfos...");
                                    clusterJedisPoolIterator.remove();
                                    clusterSelectedClient=clusterJedisPool.getResource();
                                    break;
                                }
                            }
                        	changeMasterSlave(clusterSelectedClient, clusterJedisPools);
                        }
                    }
                }
    }
    
    /**
     * 改变主从server
     * @param clusterSelectedClient
     * @param clusterJedisPools
     */
    private void changeMasterSlave(Jedis clusterSelectedClient,ArrayList<JedisPool> clusterJedisPools){
    	clusterSelectedClient.slaveofNoOne();//成为主
    	if(clusterJedisPools!=null&&clusterJedisPools.size()>0){
    		for(JedisPool jedisPool:clusterJedisPools){//设置主从关系
    			jedisPool.getResource().slaveof(clusterSelectedClient.getClient().getHost(), clusterSelectedClient.getClient().getPort());
            } 
    	}
    	
    	
    }
    /**
     * 获取IP与端口号
     * @param masterJedisPool
     * @return
     */
    private AtomicReference<HostAndPort> getHostAndPort(JedisPool jedisPool){
    	Object field = getInternalPool(jedisPool);
        if (field != null) {
            Object hostAndPortField = getHostAndPortField((GenericObjectPool) field);
            AtomicReference<HostAndPort> hostAndPort = (AtomicReference<HostAndPort>) hostAndPortField;
            return hostAndPort;
        }
        return null;
    }
    /**
     * 反射获取JedisFactory属性hostAndPort的值
     *
     * @param field
     * @return
     */
    private Object getHostAndPortField(GenericObjectPool field) {
        GenericObjectPool internalPool = field;
        PooledObjectFactory poolFactory = internalPool.getFactory();
        return SuperClassReflectionUtils.getFieldValue(poolFactory, "hostAndPort");
    }

    /**
     * 反射获取JedisPool属性internalPool值
     *
     * @param masterJedisPool
     * @return
     */
    private Object getInternalPool(JedisPool masterJedisPool) {
        return SuperClassReflectionUtils.getFieldValue(masterJedisPool, "internalPool");
    }

    /**
     * redis是否连通
     *
     * @param jedisPool
     * @param tryTimes
     * @return
     */
    private boolean getRedisCrashFlag(JedisPool jedisPool, int tryTimes) {
        boolean isRedisCrash = false;
        //循环10次获取Redis主是否挂掉
        try {
            jedisPool.getResource().getClient();
        } catch (Exception e) {
            if (tryTimes <=5) {
                tryTimes = tryTimes + 1;
                return getRedisCrashFlag(jedisPool, tryTimes);
            } else {
                return true;
            }
        }
        return isRedisCrash;
    }
    
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		zkClient = new ZkClient(zkHost);
	}
	
	public String getZkHost() {
        return zkHost;
    }

    public void setZkHost(String zkHost) {
        this.zkHost = zkHost;
    }
	/**
	 * @return the connectTimeout
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}
	/**
	 * @param connectTimeout the connectTimeout to set
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
    
}
