/**
 * 
 */
package com.opensource.netty.redis.proxy.core.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensource.netty.redis.proxy.core.client.impl.AbstractPoolClient;
import com.opensource.netty.redis.proxy.core.cluster.LoadBalance;
import com.opensource.netty.redis.proxy.core.config.support.FfanRedisServerBean;
import com.opensource.netty.redis.proxy.core.config.support.FfanRedisServerClusterBean;

/**
 * 多主
 * 集群模式配置
 * 
 * @author liubing
 *
 */
public class FfanRedisServerMasterCluster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8481358070088941073L;

	private List<FfanRedisServerClusterBean> redisServerClusterBeans;
	
	private Map<String, FfanRedisServerClusterBean> ffanRedisServerClusterBeanMap=new HashMap<String, FfanRedisServerClusterBean>();

	/** 一主多从模式 */
	private Map<String, List<FfanRedisServerBean>> masterClusters = new HashMap<String, List<FfanRedisServerBean>>();

	/** 主集合 ***/
	private List<FfanRedisServerBean> masters = new ArrayList<FfanRedisServerBean>();
	
	private String redisProxyHost;//主机名
	
	private int redisProxyPort;//端口号
	
	private LoadBalance loadMasterBalance;//主的一致性算法
	
	private Map<String, AbstractPoolClient> ffanRedisClientBeanMap =new HashMap<String, AbstractPoolClient>();
	
	public FfanRedisServerMasterCluster(
			List<FfanRedisServerClusterBean> redisServerClusterBeans) {
		this.redisServerClusterBeans = redisServerClusterBeans;
		init();
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		
		if(redisServerClusterBeans!=null&&redisServerClusterBeans.size()>0){
			for(FfanRedisServerClusterBean ffanRedisServerClusterBean:redisServerClusterBeans){
				masters.add(ffanRedisServerClusterBean.getFfanMasterRedisServerBean());
				ffanRedisServerClusterBeanMap.put(ffanRedisServerClusterBean.getFfanMasterRedisServerBean().getKey(), ffanRedisServerClusterBean);
				masterClusters.put(ffanRedisServerClusterBean.getFfanMasterRedisServerBean().getKey(), ffanRedisServerClusterBean.getFfanRedisServerClusterBeans());
			}
		}
		
	}
	
	/**
	 * 获取指定主的多从
	 * @param key
	 * @return
	 */
	public List<FfanRedisServerBean> getMasterFfanRedisServerBean(String key){
		if(masterClusters!=null&&masterClusters.containsKey(key)){
			return masterClusters.get(key);
		}
		return null;
	}
	
	/**
	 * 更新指定主的从
	 * 更改权重
	 * @param key
	 * @param ffanRedisClusterBeans
	 */
	public void updateFfanRedisClusterBeans(String key,List<FfanRedisServerBean> ffanRedisClusterBeans){
		
		List<FfanRedisServerBean> ffanRedisServerBeans=masterClusters.get(key);
		masterClusters.put(key, ffanRedisClusterBeans);
		if(ffanRedisServerBeans!=null){
			for(FfanRedisServerBean ffanRedisServerBean:ffanRedisServerBeans){//删除挂掉的从
				if(!ffanRedisClusterBeans.contains(ffanRedisServerBean)){
					if(ffanRedisClientBeanMap.containsKey(ffanRedisServerBean.getKey())){
						ffanRedisClientBeanMap.remove(ffanRedisServerBean.getKey());
					}
				}
			}

			
			
			if(ffanRedisServerClusterBeanMap.containsKey(key)){
				FfanRedisServerClusterBean ffanRedisServerClusterBean=ffanRedisServerClusterBeanMap.get(key);
				if(ffanRedisServerClusterBean.getFfanMasterRedisServerBean().getKey().equals(key)){
					ffanRedisServerClusterBean.setFfanRedisServerClusterBeans(ffanRedisClusterBeans);
				}
			}
			
			for(FfanRedisServerClusterBean ffanRedisServerClusterBean:redisServerClusterBeans){
				if(ffanRedisServerClusterBean.getFfanMasterRedisServerBean().getKey().equals(key)){
					ffanRedisServerClusterBean.setFfanRedisServerClusterBeans(ffanRedisClusterBeans);
					break;
				}
			}
		}
		
		
		
		
		
	}
	
	/**
	 * 更改指定的主
	 * @param key
	 * @param ffanRedisMasterBean
	 */
	public void updateFfanRedisMasterBean(String key,FfanRedisServerBean ffanRedisMasterBean){
		
		for(FfanRedisServerBean ffanRedisServerBean :masters){
			if(ffanRedisServerBean.getKey().equals(key)){
				ffanRedisServerBean=ffanRedisMasterBean;
				break;
			}
		}
		
		for(FfanRedisServerClusterBean ffanRedisServerClusterBean:redisServerClusterBeans){
			if(ffanRedisServerClusterBean.getFfanMasterRedisServerBean().getKey().equals(key)){
				ffanRedisServerClusterBean.setFfanMasterRedisServerBean(ffanRedisMasterBean);
				break;
			}
		}
		
		if(ffanRedisServerClusterBeanMap.containsKey(key)){
			FfanRedisServerClusterBean ffanRedisServerClusterBean=ffanRedisServerClusterBeanMap.get(key);
			if(ffanRedisServerClusterBean.getFfanMasterRedisServerBean().getKey().equals(key)){
				ffanRedisServerClusterBean.setFfanMasterRedisServerBean(ffanRedisMasterBean);
			}
		}
		if(ffanRedisClientBeanMap.containsKey(key)){
			AbstractPoolClient abstractPoolClient=ffanRedisClientBeanMap.get(ffanRedisMasterBean.getKey());
			ffanRedisClientBeanMap.put(key, abstractPoolClient);
		}
	}
	/**
	 * @return the masters
	 */
	public List<FfanRedisServerBean> getMasters() {
		return masters;
	}

	/**
	 * @return the redisServerClusterBeans
	 */
	public List<FfanRedisServerClusterBean> getRedisServerClusterBeans() {
		return redisServerClusterBeans;
	}
	
	/**
	 * 主挂了,移除元素
	 * @param key
	 */
	public void remove(String key){
		if(ffanRedisServerClusterBeanMap.containsKey(key)){
			ffanRedisServerClusterBeanMap.remove(key);
		}
		if(masterClusters.containsKey(key)){
			masterClusters.remove(key);
		}
		
		if(ffanRedisClientBeanMap.containsKey(key)){
			ffanRedisClientBeanMap.remove(key);
		}
		
		for(FfanRedisServerBean ffanRedisServerBean :masters){
			if(ffanRedisServerBean.getKey().equals(key)){
				masters.remove(ffanRedisServerBean);
				break;
			}
		}
		
		for(FfanRedisServerClusterBean ffanRedisServerClusterBean:redisServerClusterBeans){
			if(ffanRedisServerClusterBean.getFfanMasterRedisServerBean().getKey().equals(key)){
				redisServerClusterBeans.remove(ffanRedisServerClusterBean);
				break;
			}
		}
	}
	
	/**
	 * 有主，无从
	 * @param key
	 */
	public void removeSlavesByMaster(String key){
		if(ffanRedisServerClusterBeanMap.containsKey(key)){
			ffanRedisServerClusterBeanMap.remove(key);
		}
		List<FfanRedisServerBean> ffanRedisServerBeans=masterClusters.get(key);
		
		for(FfanRedisServerBean ffanRedisServerBean:ffanRedisServerBeans){//删除主对应的从连接客户端
			if(ffanRedisClientBeanMap.containsKey(ffanRedisServerBean.getKey())){
				ffanRedisClientBeanMap.remove(ffanRedisServerBean.getKey());
			}
		}
		if(masterClusters.containsKey(key)){//删除对应的主从
			masterClusters.remove(key);
		}
				
		for(FfanRedisServerClusterBean ffanRedisServerClusterBean:redisServerClusterBeans){//主存在，从不存在，删除对应的从
			if(ffanRedisServerClusterBean.getFfanMasterRedisServerBean().getKey().equals(key)){
				ffanRedisServerClusterBean.getFfanRedisServerClusterBeans().clear();
				break;
			}
		}
	}
	
	public FfanRedisServerClusterBean getFfanRedisServerClusterBean(String key){
		if(ffanRedisServerClusterBeanMap.containsKey(key)){
			return ffanRedisServerClusterBeanMap.get(key);
		}
		return null;
	}
	/**
	 * @param redisServerClusterBeans
	 *            the redisServerClusterBeans to set
	 */
	public void setRedisServerClusterBeans(
			List<FfanRedisServerClusterBean> redisServerClusterBeans) {
		this.redisServerClusterBeans = redisServerClusterBeans;
	}

	/**
	 * @return the redisProxyHost
	 */
	public String getRedisProxyHost() {
		return redisProxyHost;
	}

	/**
	 * @param redisProxyHost the redisProxyHost to set
	 */
	public void setRedisProxyHost(String redisProxyHost) {
		this.redisProxyHost = redisProxyHost;
	}

	/**
	 * @return the redisProxyPort
	 */
	public int getRedisProxyPort() {
		return redisProxyPort;
	}

	/**
	 * @param redisProxyPort the redisProxyPort to set
	 */
	public void setRedisProxyPort(int redisProxyPort) {
		this.redisProxyPort = redisProxyPort;
	}

	/**
	 * @return the loadMasterBalance
	 */
	public LoadBalance getLoadMasterBalance() {
		loadMasterBalance.setFfanRedisServerMasterCluster(this);
		return loadMasterBalance;
	}

	/**
	 * @param loadMasterBalance the loadMasterBalance to set
	 */
	public void setLoadMasterBalance(LoadBalance loadMasterBalance) {
		this.loadMasterBalance = loadMasterBalance;
	}

	/**
	 * @return the ffanRedisServerClusterBeanMap
	 */
	public Map<String, FfanRedisServerClusterBean> getFfanRedisServerClusterBeanMap() {
		return ffanRedisServerClusterBeanMap;
	}

	/**
	 * @param ffanRedisServerClusterBeanMap the ffanRedisServerClusterBeanMap to set
	 */
	public void setFfanRedisServerClusterBeanMap(
			Map<String, FfanRedisServerClusterBean> ffanRedisServerClusterBeanMap) {
		this.ffanRedisServerClusterBeanMap = ffanRedisServerClusterBeanMap;
	}

	/**
	 * @return the ffanRedisClientBeanMap
	 */
	public Map<String, AbstractPoolClient> getFfanRedisClientBeanMap() {
		return ffanRedisClientBeanMap;
	}

	/**
	 * @param ffanRedisClientBeanMap the ffanRedisClientBeanMap to set
	 */
	public void setFfanRedisClientBeanMap(Map<String, AbstractPoolClient> ffanRedisClientBeanMap) {
		this.ffanRedisClientBeanMap = ffanRedisClientBeanMap;
	}
	
	
}
