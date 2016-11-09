/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.exception;

/**
 * @author liubing
 *
 */
public class LBRedisProxyPoolPropertyValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1420614214978891385L;
	
	public LBRedisProxyPoolPropertyValidationException(String message) {
		super(message);
	}

	public LBRedisProxyPoolPropertyValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
