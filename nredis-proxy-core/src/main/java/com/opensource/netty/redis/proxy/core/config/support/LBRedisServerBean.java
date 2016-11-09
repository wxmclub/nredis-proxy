/**
 * 
 */
package com.opensource.netty.redis.proxy.core.config.support;

import com.opensource.netty.redis.proxy.commons.algorithm.impl.support.RedisWeight;
import com.opensource.netty.redis.proxy.commons.constants.RedisConstants;

/**
 * @author liubing
 *
 */
public class LBRedisServerBean implements RedisWeight{
	
	private String host;//主机名
	
	private int port;//端口号
	
	private int timeout;//超时时间
	
	private int maxActiveConnection;//最大活动连接数
	
	private int maxIdleConnection;//最大 空闲连接数
	
	private int minConnection;//最小连接数
	
	private int weight=1;//默认权重比例为1
	/**
	 * @param host
	 * @param port
	 * @param timeout
	 * @param maxActiveConnection
	 * @param maxIdleConnection
	 * @param minConnection
	 */
	public LBRedisServerBean(String host, int port, int timeout,
			int maxActiveConnection, int maxIdleConnection, int minConnection) {
		super();
		this.host = host;
		this.port = port;
		this.timeout = timeout;
		this.maxActiveConnection = maxActiveConnection;
		this.maxIdleConnection = maxIdleConnection;
		this.minConnection = minConnection;
	}

	/**
	 * 
	 */
	public LBRedisServerBean() {
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
	 * 关键key
	 * @return
	 */
	public String getKey(){
		StringBuffer sbBuffer=new StringBuffer();
		sbBuffer.append(RedisConstants.REDIS_PROXY).append(host).append(RedisConstants.SEPERATOR_ACCESS_LOG).append(port);
		return sbBuffer.toString();
	}
	
	public String getServerKey(){
		StringBuffer sbBuffer=new StringBuffer();
		sbBuffer.append(host).append(RedisConstants.PROTOCOL_SEPARATOR).append(port);
		return sbBuffer.toString();
	}
	
	@Override
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + maxActiveConnection;
		result = prime * result + maxIdleConnection;
		result = prime * result + minConnection;
		result = prime * result + port;
		result = prime * result + timeout;
		result = prime * result + weight;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LBRedisServerBean other = (LBRedisServerBean) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (maxActiveConnection != other.maxActiveConnection)
			return false;
		if (maxIdleConnection != other.maxIdleConnection)
			return false;
		if (minConnection != other.minConnection)
			return false;
		if (port != other.port)
			return false;
		if (timeout != other.timeout)
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}
	
	
}
