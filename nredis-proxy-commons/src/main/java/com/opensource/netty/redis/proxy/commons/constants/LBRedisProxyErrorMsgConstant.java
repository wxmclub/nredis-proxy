/**
 * 
 */
package com.opensource.netty.redis.proxy.commons.constants;

import com.opensource.netty.redis.proxy.commons.exception.LBRedisProxyErrorMsg;


/**
 * @author liubing
 * 业务错误变量
 */
public class LBRedisProxyErrorMsgConstant {
	
	 // service error status 503
    public static final int SERVICE_DEFAULT_ERROR_CODE = 10001;
    public static final int SERVICE_REJECT_ERROR_CODE = 10002;
    public static final int SERVICE_TIMEOUT_ERROR_CODE = 10003;
    public static final int SERVICE_TASK_CANCEL_ERROR_CODE = 10004;
    // service error status 404
    public static final int SERVICE_UNFOUND_ERROR_CODE = 10101;
    // service error status 403
    public static final int SERVICE_REQUEST_LENGTH_OUT_OF_LIMIT_ERROR_CODE = 10201;
    // framework error
    public static final int FRAMEWORK_DEFAULT_ERROR_CODE = 20001;
    public static final int FRAMEWORK_ENCODE_ERROR_CODE = 20002;
    public static final int FRAMEWORK_DECODE_ERROR_CODE = 20003;
    public static final int FRAMEWORK_INIT_ERROR_CODE = 20004;
    public static final int FRAMEWORK_EXPORT_ERROR_CODE = 20005;
    public static final int FRAMEWORK_SERVER_ERROR_CODE = 20006;
    public static final int FRAMEWORK_REFER_ERROR_CODE = 20007;
    public static final int FRAMEWORK_REGISTER_ERROR_CODE = 20008;
    // biz exception
    public static final int BIZ_DEFAULT_ERROR_CODE = 30001;
    /**
     * service error start
     **/

    public static final LBRedisProxyErrorMsg SERVICE_DEFAULT_ERROR = new LBRedisProxyErrorMsg(503, SERVICE_DEFAULT_ERROR_CODE, "service error");
    public static final LBRedisProxyErrorMsg SERVICE_REJECT = new LBRedisProxyErrorMsg(503, SERVICE_REJECT_ERROR_CODE, "service reject");
    public static final LBRedisProxyErrorMsg SERVICE_UNFOUND = new LBRedisProxyErrorMsg(404, SERVICE_UNFOUND_ERROR_CODE, "service unfound");
    public static final LBRedisProxyErrorMsg SERVICE_TIMEOUT = new LBRedisProxyErrorMsg(503, SERVICE_TIMEOUT_ERROR_CODE, "service request timeout");
    public static final LBRedisProxyErrorMsg SERVICE_TASK_CANCEL = new LBRedisProxyErrorMsg(503, SERVICE_TASK_CANCEL_ERROR_CODE, "service task cancel");
    public static final LBRedisProxyErrorMsg SERVICE_REQUEST_LENGTH_OUT_OF_LIMIT = new LBRedisProxyErrorMsg(403,
            SERVICE_REQUEST_LENGTH_OUT_OF_LIMIT_ERROR_CODE, "servier requset data length over of limit");

    /**
     * framework error start
     **/
    public static final LBRedisProxyErrorMsg FRAMEWORK_DEFAULT_ERROR = new LBRedisProxyErrorMsg(503, FRAMEWORK_DEFAULT_ERROR_CODE,
            "framework default error");

    /** service error end **/
    public static final LBRedisProxyErrorMsg FRAMEWORK_ENCODE_ERROR =
            new LBRedisProxyErrorMsg(503, FRAMEWORK_ENCODE_ERROR_CODE, "framework encode error");
    public static final LBRedisProxyErrorMsg FRAMEWORK_DECODE_ERROR =
            new LBRedisProxyErrorMsg(503, FRAMEWORK_DECODE_ERROR_CODE, "framework decode error");
    public static final LBRedisProxyErrorMsg FRAMEWORK_INIT_ERROR = new LBRedisProxyErrorMsg(500, FRAMEWORK_INIT_ERROR_CODE, "framework init error");
    public static final LBRedisProxyErrorMsg FRAMEWORK_EXPORT_ERROR =
            new LBRedisProxyErrorMsg(503, FRAMEWORK_EXPORT_ERROR_CODE, "framework export error");
    public static final LBRedisProxyErrorMsg FRAMEWORK_REFER_ERROR = new LBRedisProxyErrorMsg(503, FRAMEWORK_REFER_ERROR_CODE, "framework refer error");
    /**
     * biz error start
     **/
    public static final LBRedisProxyErrorMsg BIZ_DEFAULT_EXCEPTION = new LBRedisProxyErrorMsg(503, BIZ_DEFAULT_ERROR_CODE, "provider error");
    /** framework error end **/

    private LBRedisProxyErrorMsgConstant() {
    }
    /** biz error end **/
}
