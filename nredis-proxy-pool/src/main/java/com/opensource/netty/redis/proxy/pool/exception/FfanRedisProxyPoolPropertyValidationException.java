/**
 * 
 */
package com.opensource.netty.redis.proxy.pool.exception;

/**
 * @author liubing
 *
 */
public class FfanRedisProxyPoolPropertyValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1420614214978891385L;
	
	public FfanRedisProxyPoolPropertyValidationException(String message) {
		super(message);
	}

	public FfanRedisProxyPoolPropertyValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
