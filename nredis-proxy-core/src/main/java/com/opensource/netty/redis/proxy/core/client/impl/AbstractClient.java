/**
 * 
 */
package com.opensource.netty.redis.proxy.core.client.impl;

import com.opensource.netty.redis.proxy.core.client.Client;
import com.opensource.netty.redis.proxy.core.enums.ChannelState;

/**
 * @author liubing
 *
 */
public abstract class AbstractClient implements Client {

    protected volatile ChannelState state = ChannelState.UNINIT;
}
