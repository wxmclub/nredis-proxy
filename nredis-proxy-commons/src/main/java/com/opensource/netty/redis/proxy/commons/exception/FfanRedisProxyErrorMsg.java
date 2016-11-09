/**
 * 
 */
package com.opensource.netty.redis.proxy.commons.exception;

import java.io.Serializable;

/**
 * @author liubing
 *
 */
public class FfanRedisProxyErrorMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4928071017793101657L;
	
	private int status;
    private int errorcode;
    private String message;
	public FfanRedisProxyErrorMsg(int status, int errorcode, String message) {
		super();
		this.status = status;
		this.errorcode = errorcode;
		this.message = message;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the errorcode
	 */
	public int getErrorcode() {
		return errorcode;
	}
	/**
	 * @param errorcode the errorcode to set
	 */
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
    
    

}
