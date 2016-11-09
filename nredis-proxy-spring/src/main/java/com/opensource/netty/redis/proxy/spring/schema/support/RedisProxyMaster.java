/**
 * 
 */
package com.opensource.netty.redis.proxy.spring.schema.support;

import java.io.Serializable;
import java.util.List;

import com.opensource.netty.redis.proxy.core.cluster.LoadBalance;
/**
 * 主节点
 * @author liubing
 *
 */
public class RedisProxyMaster implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6861660674247475024L;
	
	private String host;//主机名
	
	private int port;//端口号
	
	private int timeout;//超时时间
	
	private int maxActiveConnection;//最大活动连接数
	
	private int maxIdleConnection;//最大 空闲连接数
	
	private int minConnection;//最小连接数
	
	private List<RedisProxyCluster> redisProxyClusters;//多个从
	
	private LoadBalance loadClusterBalance;//从权重
	
	/**
	 * 
	 */
	public RedisProxyMaster() {
		super();
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the maxActiveConnection
	 */
	public int getMaxActiveConnection() {
		return maxActiveConnection;
	}

	/**
	 * @param maxActiveConnection the maxActiveConnection to set
	 */
	public void setMaxActiveConnection(int maxActiveConnection) {
		this.maxActiveConnection = maxActiveConnection;
	}

	/**
	 * @return the maxIdleConnection
	 */
	public int getMaxIdleConnection() {
		return maxIdleConnection;
	}

	/**
	 * @param maxIdleConnection the maxIdleConnection to set
	 */
	public void setMaxIdleConnection(int maxIdleConnection) {
		this.maxIdleConnection = maxIdleConnection;
	}

	/**
	 * @return the minConnection
	 */
	public int getMinConnection() {
		return minConnection;
	}

	/**
	 * @param minConnection the minConnection to set
	 */
	public void setMinConnection(int minConnection) {
		this.minConnection = minConnection;
	}

	/**
	 * @return the redisProxyClusters
	 */
	public List<RedisProxyCluster> getRedisProxyClusters() {
		return redisProxyClusters;
	}

	/**
	 * @param redisProxyClusters the redisProxyClusters to set
	 */
	public void setRedisProxyClusters(List<RedisProxyCluster> redisProxyClusters) {
		this.redisProxyClusters = redisProxyClusters;
	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append(host).append("-").append(port);
		return stringBuilder.toString();
	}

	/**
	 * @return the loadClusterBalance
	 */
	public LoadBalance getLoadClusterBalance() {
		return loadClusterBalance;
	}

	/**
	 * @param loadClusterBalance the loadClusterBalance to set
	 */
	public void setLoadClusterBalance(LoadBalance loadClusterBalance) {
		this.loadClusterBalance = loadClusterBalance;
	}
	
	
}
