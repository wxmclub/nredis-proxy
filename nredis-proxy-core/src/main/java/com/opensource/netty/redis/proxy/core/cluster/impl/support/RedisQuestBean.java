/**
 * 
 */
package com.opensource.netty.redis.proxy.core.cluster.impl.support;

/**
 * redisproxy 接受请求中间类
 * @author liubing
 *
 */
public class RedisQuestBean {
	
	private String command;//命令
	
	private byte[] key;//关键字
	
	private boolean isWrite;//是否写
	
	/**
	 * @param command
	 * @param key
	 * @param isWrite
	 */
	public RedisQuestBean(String command, byte[] key, boolean isWrite) {
		super();
		this.command = command;
		this.key = key;
		this.isWrite = isWrite;
	}

	/**
	 * @return the isWrite
	 */
	public boolean isWrite() {
		return isWrite;
	}



	/**
	 * @param isWrite the isWrite to set
	 */
	public void setWrite(boolean isWrite) {
		this.isWrite = isWrite;
	}



	/**
	 * 
	 */
	public RedisQuestBean() {
		super();
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the key
	 */
	public byte[] getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(byte[] key) {
		this.key = key;
	}
	
	
}
