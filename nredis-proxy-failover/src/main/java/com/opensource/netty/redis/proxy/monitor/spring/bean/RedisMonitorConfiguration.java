/**
 * 
 */
package com.opensource.netty.redis.proxy.monitor.spring.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.alibaba.fastjson.JSONObject;
import com.opensource.netty.redis.proxy.commons.constants.RedisConstants;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerBean;
import com.opensource.netty.redis.proxy.core.registry.Registry;
import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;
import com.opensource.netty.redis.proxy.zk.registry.ZookeeperRegistryFactory;

/**
 * @author liubing
 * spring 注入配置文件
 */
public class RedisMonitorConfiguration implements InitializingBean{
	
	private String zkHost;//zk ip:port
	
	private  List<JedisPool> masterShardInfos=new ArrayList<JedisPool>();//主 线程池
	
	//主从 线程池
	private Map<String, ArrayList<JedisPool>> clusterShardInfos=new ConcurrentHashMap<String, ArrayList<JedisPool>>();
		
	private List<LBRedisServerBean> ffanRedisMasterServerBeans=new ArrayList<LBRedisServerBean>();//主 注册
	
	private Map<String, ArrayList<LBRedisServerBean>> ffanRedisServerClusterBeans=new ConcurrentHashMap<String, ArrayList<LBRedisServerBean>>();
	
	private JedisPoolConfig config = new JedisPoolConfig();
	/**
	 * 
	 */
	public RedisMonitorConfiguration() {
		super();
	}

	

	/**
	 * @return the zkHost
	 */
	public String getZkHost() {
		return zkHost;
	}

	/**
	 * @param zkHost the zkHost to set
	 */
	public void setZkHost(String zkHost) {
		this.zkHost = zkHost;
	}

	

	@Override
	public void afterPropertiesSet() throws Exception {
		ZookeeperRegistryFactory zookeeperRegistryFactory=new ZookeeperRegistryFactory();
		RedisProxyURL redisProxyURL=new RedisProxyURL();
		redisProxyURL.addParameter(RedisConstants.ADDRESS, zkHost);
		Registry registry=zookeeperRegistryFactory.getRegistry(redisProxyURL);
		List<String> redisMasterNodes=registry.getChildren(RedisConstants.TOP_PATH);
		if(redisMasterNodes!=null&&redisMasterNodes.size()>0){
			for(String redisMasterNode:redisMasterNodes){//主
				String masterBean=registry.readData(RedisConstants.TOP_PATH+RedisConstants.PATH_SEPARATOR+redisMasterNode, true);
				LBRedisServerBean ffanRedisServerBean=JSONObject.parseObject(masterBean, LBRedisServerBean.class);
				ffanRedisMasterServerBeans.add(ffanRedisServerBean);
				
				String dataPath=redisMasterNode;
				String[] dataPaths=dataPath.split(":");
				initMasterServer(dataPaths[0], Integer.parseInt(dataPaths[1]), RedisConstants.TIMEOUT);
				List<String> redisClutserNodes=registry.getChildren(RedisConstants.TOP_PATH+RedisConstants.PATH_SEPARATOR+redisMasterNode);
				StringBuilder key=new StringBuilder();
				key.append(dataPaths[0]).append(":").append(dataPaths[1]);
				for(String redisClusterNode:redisClutserNodes){//从
					String clusterBean=registry.readData(RedisConstants.TOP_PATH+RedisConstants.PATH_SEPARATOR+redisMasterNode+RedisConstants.PATH_SEPARATOR+redisClusterNode, true);
					if(ffanRedisServerClusterBeans.containsKey(key.toString())){
						ArrayList<LBRedisServerBean> ffanRedisClusterServerBeans=ffanRedisServerClusterBeans.get(key.toString());
						LBRedisServerBean ffanRedisClusterServerBean=JSONObject.parseObject(clusterBean, LBRedisServerBean.class);
						ffanRedisClusterServerBeans.add(ffanRedisClusterServerBean);
						ffanRedisServerClusterBeans.put(key.toString(), ffanRedisClusterServerBeans);
					}else{
						ArrayList<LBRedisServerBean> ffanRedisClusterServerBeans=new ArrayList<LBRedisServerBean>();					
						LBRedisServerBean ffanRedisClusterServerBean=JSONObject.parseObject(clusterBean, LBRedisServerBean.class);
						ffanRedisClusterServerBeans.add(ffanRedisClusterServerBean);
						ffanRedisServerClusterBeans.put(key.toString(), ffanRedisClusterServerBeans);
					}
					
					
					String dataClusterPath=redisClusterNode;
					String[] dataClusterPaths=dataClusterPath.split(":");
					initClusterServer(dataClusterPaths[0], Integer.parseInt(dataClusterPaths[1]), RedisConstants.TIMEOUT,key.toString());
				}
			}
		}
		
	}
	/**
	 * 初始化主
	 * @param host
	 * @param port
	 * @param timeout
	 */
	private  void initMasterServer(String host, int port, int timeout) {
		
		JedisPool jedis=new JedisPool(config,host, port,timeout);
		masterShardInfos.add(jedis);
	}
	
	/**
	 * 初始化从
	 * @param host
	 * @param port
	 * @param timeout
	 * @param server
	 */
	private void initClusterServer(String host, int port, int timeout,String server) {
		if(clusterShardInfos.containsKey(server)){//包含
			ArrayList<JedisPool> masterJedisPools=clusterShardInfos.get(server);
			JedisPool masterJedis=new JedisPool(config,host, port, timeout);
			
			masterJedisPools.add(masterJedis);
			clusterShardInfos.put(server, masterJedisPools);
		}else{//不包含
			ArrayList<JedisPool> jedisShardInfos=new ArrayList<JedisPool>();
			JedisPool clusterJedisPools=new JedisPool(config,host, port, timeout);
			jedisShardInfos.add(clusterJedisPools);
			clusterShardInfos.put(server, jedisShardInfos);
		}
	}



	/**
	 * @return the masterShardInfos
	 */
	public List<JedisPool> getMasterShardInfos() {
		return masterShardInfos;
	}



	/**
	 * @param masterShardInfos the masterShardInfos to set
	 */
	public void setMasterShardInfos(List<JedisPool> masterShardInfos) {
		this.masterShardInfos = masterShardInfos;
	}



	/**
	 * @return the clusterShardInfos
	 */
	public Map<String, ArrayList<JedisPool>> getClusterShardInfos() {
		return clusterShardInfos;
	}



	/**
	 * @param clusterShardInfos the clusterShardInfos to set
	 */
	public void setClusterShardInfos(
			Map<String, ArrayList<JedisPool>> clusterShardInfos) {
		this.clusterShardInfos = clusterShardInfos;
	}



	/**
	 * @return the ffanRedisMasterServerBeans
	 */
	public List<LBRedisServerBean> getFfanRedisMasterServerBeans() {
		return ffanRedisMasterServerBeans;
	}



	/**
	 * @param ffanRedisMasterServerBeans the ffanRedisMasterServerBeans to set
	 */
	public void setFfanRedisMasterServerBeans(
			List<LBRedisServerBean> ffanRedisMasterServerBeans) {
		this.ffanRedisMasterServerBeans = ffanRedisMasterServerBeans;
	}



	/**
	 * @return the ffanRedisServerClusterBeans
	 */
	public Map<String, ArrayList<LBRedisServerBean>> getFfanRedisServerClusterBeans() {
		return ffanRedisServerClusterBeans;
	}



	/**
	 * @param ffanRedisServerClusterBeans the ffanRedisServerClusterBeans to set
	 */
	public void setFfanRedisServerClusterBeans(
			Map<String, ArrayList<LBRedisServerBean>> ffanRedisServerClusterBeans) {
		this.ffanRedisServerClusterBeans = ffanRedisServerClusterBeans;
	}

	
}
