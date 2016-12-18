package com.opensource.netty.redis.proxy.net.client;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import com.opensource.netty.redis.proxy.commons.constants.LBRedisProxyErrorMsgConstant;
import com.opensource.netty.redis.proxy.commons.exception.LBRedisProxyFrameworkException;
import com.opensource.netty.redis.proxy.core.command.impl.RedisCommand;
import com.opensource.netty.redis.proxy.core.connection.IConnection;
import com.opensource.netty.redis.proxy.core.connection.IConnectionCallBack;
import com.opensource.netty.redis.proxy.core.enums.ChannelState;
import com.opensource.netty.redis.proxy.core.enums.RedisProxyParamType;
import com.opensource.netty.redis.proxy.core.log.impl.LoggerUtils;
import com.opensource.netty.redis.proxy.net.client.support.LBRedisClientOutHandler;

/**
 * 
 * @author liubing
 *
 */
public class LBRedisConnection implements IConnection{
	
	private Logger logger = LoggerFactory.getLogger(LBRedisConnection.class);
	
	private volatile ChannelState state = ChannelState.UNINIT;

	private Channel channel = null;

	private LBRedisClient ffanRedisClient;
	
	/**
	 * @param ffanRedisClient
	 */
	public LBRedisConnection(LBRedisClient ffanRedisClient) {
		super();
		this.ffanRedisClient = ffanRedisClient;
	}

	public void write(RedisCommand request,IConnectionCallBack connectionCallBack) {
	   
		this.channel.attr(LBRedisClientOutHandler.CALLBACK_KEY).setIfAbsent(connectionCallBack);
		//LoggerUtils.error(channel.remoteAddress().toString()+" had been seted");
		this.channel.writeAndFlush(request);
		
	}
	
	public Channel channel() {
		return channel;
	}

	/**
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}

	
	@Override
	public boolean open() {
		if (isAvailable()) {
			return true;
		}

		try {
			ChannelFuture channelFuture = ffanRedisClient.getBootstrap().connect(
					new InetSocketAddress(ffanRedisClient.getRedisProxyURL().getHost(), ffanRedisClient.getRedisProxyURL().getPort()));

			long start = System.currentTimeMillis();

			int timeout = ffanRedisClient.getRedisProxyURL().getIntParameter(RedisProxyParamType.connectTimeout.getName(), RedisProxyParamType.connectTimeout.getIntValue());
			if (timeout <= 0) {
	            throw new LBRedisProxyFrameworkException("Netty4Client init Error: timeout(" + timeout + ") <= 0 is forbid.",
	                    LBRedisProxyErrorMsgConstant.FRAMEWORK_INIT_ERROR);
			}
			// 不去依赖于connectTimeout
			boolean result = channelFuture.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
            boolean success = channelFuture.isSuccess();

			if (result && success) {
				channel = channelFuture.channel();
				state = ChannelState.ALIVE;
				return true;
			}
            boolean connected = false;
            if(channelFuture.channel() != null){
                connected = channelFuture.channel().isOpen();
            }

			if (channelFuture.cause() != null) {
				channelFuture.cancel(true);
				throw new LBRedisProxyFrameworkException("NettyChannel failed to connect to server, url: "
						+ ffanRedisClient.getRedisProxyURL()+ ", result: " + result + ", success: " + success + ", connected: " + connected, channelFuture.cause());
			} else {
				channelFuture.cancel(true);
                throw new LBRedisProxyFrameworkException("NettyChannel connect to server timeout url: "
                        + ffanRedisClient.getRedisProxyURL() + ", cost: " + (System.currentTimeMillis() - start) + ", result: " + result + ", success: " + success + ", connected: " + connected);
            }
		} catch (LBRedisProxyFrameworkException e) {
			throw e;
		} catch (Exception e) {
			throw new LBRedisProxyFrameworkException("NettyChannel failed to connect to server, url: "
					+ ffanRedisClient.getRedisProxyURL(), e);
		} finally {
			if (!state.isAliveState()) {
				ffanRedisClient.incrErrorCount();
			}
		}
	}

	@Override
	public void close() {
		close(0);
	}

	@Override
	public void close(int timeout) {
		try {
			if (channel != null) {
				channel.close();
			}
			state = ChannelState.CLOSE;
		} catch (Exception e) {
			logger.error("NettyChannel close Error: " +ffanRedisClient.getRedisProxyURL() , e);
		}
	}

	@Override
	public boolean isClosed() {
		return state.isCloseState();
	}

	@Override
	public boolean isAvailable() {
		if(channel!=null){//判断通道状态，防止通道假活
			return state.isAliveState()&&channel.isActive();
		}
		return state.isAliveState();
	}


}
