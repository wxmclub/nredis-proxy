/**
 * 
 */
package com.opensource.netty.redis.proxy.commons.exception;

import com.opensource.netty.redis.proxy.commons.constants.LBRedisProxyErrorMsgConstant;



/**
 * @author liubing
 *  抽象业务异常
 */
public abstract class AbstractLBRedisProxyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5237904941832693524L;
	
	protected LBRedisProxyErrorMsg ffanRpcErrorMsg=LBRedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR;
	
	protected String errorMsg = null;
	
	public AbstractLBRedisProxyException() {
        super();
    }

    public AbstractLBRedisProxyException(LBRedisProxyErrorMsg ffanRpcErrorMsg) {
        super();
        this.ffanRpcErrorMsg = ffanRpcErrorMsg;
    }

    public AbstractLBRedisProxyException(String message) {
        super(message);
        this.errorMsg = message;
    }

    public AbstractLBRedisProxyException(String message, LBRedisProxyErrorMsg ffanRpcErrorMsg) {
        super(message);
        this.ffanRpcErrorMsg = ffanRpcErrorMsg;
        this.errorMsg = message;
    }

    public AbstractLBRedisProxyException(String message, Throwable cause) {
        super(message, cause);
        this.errorMsg = message;
    }

    public AbstractLBRedisProxyException(String message, Throwable cause, LBRedisProxyErrorMsg ffanRpcErrorMsg) {
        super(message, cause);
        this.ffanRpcErrorMsg = ffanRpcErrorMsg;
        this.errorMsg = message;
    }

    public AbstractLBRedisProxyException(Throwable cause) {
        super(cause);
    }

    public AbstractLBRedisProxyException(Throwable cause, LBRedisProxyErrorMsg ffanRpcErrorMsg) {
        super(cause);
        this.ffanRpcErrorMsg = ffanRpcErrorMsg;
    }

    @Override
    public String getMessage() {
        if (ffanRpcErrorMsg == null) {
            return super.getMessage();
        }

        String message;

        if (errorMsg != null && !"".equals(errorMsg)) {
            message = errorMsg;
        } else {
            message = ffanRpcErrorMsg.getMessage();
        }

        // TODO 统一上下文 requestid
        return "error_message: " + message + ", status: " + ffanRpcErrorMsg.getStatus() + ", error_code: " + ffanRpcErrorMsg.getErrorcode()
                + ",r=";
    }

    public int getStatus() {
        return ffanRpcErrorMsg != null ? ffanRpcErrorMsg.getStatus() : 0;
    }

    public int getErrorCode() {
        return ffanRpcErrorMsg != null ? ffanRpcErrorMsg.getErrorcode() : 0;
    }

    public LBRedisProxyErrorMsg getMotanErrorMsg() {
        return ffanRpcErrorMsg;
    }
}
