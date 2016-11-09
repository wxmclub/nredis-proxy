/**
 * 
 */
package com.opensource.netty.redis.proxy.core.enums;

/**
 * @author liubing
 *
 */
public enum RequestState {
	READ_SKIP,//健壮性考虑，如果第一个字符不是*则skip直到遇到*
    READ_INIT,
    READ_ARGC,//参数数量
    READ_ARG, //参数
    READ_END
}
