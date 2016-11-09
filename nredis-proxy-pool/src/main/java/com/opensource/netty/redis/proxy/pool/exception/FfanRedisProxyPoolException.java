/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.exception;

/**
 * @author liubing
 * 自定义连接池exception
 */
public class FfanRedisProxyPoolException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1172816022950399557L;

	public FfanRedisProxyPoolException() {
		super();
	}

	public FfanRedisProxyPoolException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FfanRedisProxyPoolException(String arg0) {
		super(arg0);
	}

	public FfanRedisProxyPoolException(Throwable arg0) {
		super(arg0);
	}
}
