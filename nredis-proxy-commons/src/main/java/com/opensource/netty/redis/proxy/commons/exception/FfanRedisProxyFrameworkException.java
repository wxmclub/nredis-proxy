
package com.opensource.netty.redis.proxy.commons.exception;

import com.opensource.netty.redis.proxy.commons.constants.FfanRedisProxyErrorMsgConstant;


/**
 * 包装客户端异常
 * 
 * @author liubing
 * 
 */
public class FfanRedisProxyFrameworkException extends AbstractFfanRedisProxyException {
	
    private static final long serialVersionUID = -1638857395789735293L;

    public FfanRedisProxyFrameworkException() {
        super(FfanRedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public FfanRedisProxyFrameworkException(FfanRedisProxyErrorMsg ffanRpcErrorMsg) {
        super(ffanRpcErrorMsg);
    }

    public FfanRedisProxyFrameworkException(String message) {
        super(message, FfanRedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public FfanRedisProxyFrameworkException(String message, FfanRedisProxyErrorMsg ffanRpcErrorMsg) {
        super(message, ffanRpcErrorMsg);
    }

    public FfanRedisProxyFrameworkException(String message, Throwable cause) {
        super(message, cause, FfanRedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public FfanRedisProxyFrameworkException(String message, Throwable cause, FfanRedisProxyErrorMsg ffanRpcErrorMsg) {
        super(message, cause, ffanRpcErrorMsg);
    }

    public FfanRedisProxyFrameworkException(Throwable cause) {
        super(cause, FfanRedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public FfanRedisProxyFrameworkException(Throwable cause, FfanRedisProxyErrorMsg ffanRpcErrorMsg) {
        super(cause, ffanRpcErrorMsg);
    }

}
