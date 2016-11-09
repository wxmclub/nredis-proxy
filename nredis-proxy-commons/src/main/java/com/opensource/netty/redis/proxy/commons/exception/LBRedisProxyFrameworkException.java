
package com.opensource.netty.redis.proxy.commons.exception;

import com.opensource.netty.redis.proxy.commons.constants.LBRedisProxyErrorMsgConstant;


/**
 * 包装客户端异常
 * 
 * @author liubing
 * 
 */
public class LBRedisProxyFrameworkException extends AbstractLBRedisProxyException {
	
    private static final long serialVersionUID = -1638857395789735293L;

    public LBRedisProxyFrameworkException() {
        super(LBRedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public LBRedisProxyFrameworkException(LBRedisProxyErrorMsg ffanRpcErrorMsg) {
        super(ffanRpcErrorMsg);
    }

    public LBRedisProxyFrameworkException(String message) {
        super(message, LBRedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public LBRedisProxyFrameworkException(String message, LBRedisProxyErrorMsg ffanRpcErrorMsg) {
        super(message, ffanRpcErrorMsg);
    }

    public LBRedisProxyFrameworkException(String message, Throwable cause) {
        super(message, cause, LBRedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public LBRedisProxyFrameworkException(String message, Throwable cause, LBRedisProxyErrorMsg ffanRpcErrorMsg) {
        super(message, cause, ffanRpcErrorMsg);
    }

    public LBRedisProxyFrameworkException(Throwable cause) {
        super(cause, LBRedisProxyErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public LBRedisProxyFrameworkException(Throwable cause, LBRedisProxyErrorMsg ffanRpcErrorMsg) {
        super(cause, ffanRpcErrorMsg);
    }

}
