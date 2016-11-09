/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.exception;

/**
 * @author liubing
 * 自定义连接池exception
 */
public class LBRedisProxyPoolException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1172816022950399557L;

	public LBRedisProxyPoolException() {
		super();
	}

	public LBRedisProxyPoolException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public LBRedisProxyPoolException(String arg0) {
		super(arg0);
	}

	public LBRedisProxyPoolException(Throwable arg0) {
		super(arg0);
	}
}
