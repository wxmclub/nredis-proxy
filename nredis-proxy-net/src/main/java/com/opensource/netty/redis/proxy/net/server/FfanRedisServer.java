/**
 * 
 */
package com.opensource.netty.redis.proxy.net.server;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import com.opensource.netty.redis.proxy.core.config.FfanRedisServerMasterCluster;
import com.opensource.netty.redis.proxy.core.config.support.FfanRedisServerBean;
import com.opensource.netty.redis.proxy.core.config.support.FfanRedisServerClusterBean;
import com.opensource.netty.redis.proxy.core.enums.RedisProxyParamType;
import com.opensource.netty.redis.proxy.core.protocol.RedisReplyEncoder;
import com.opensource.netty.redis.proxy.core.protocol.RedisRequestDecoder;
import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;
import com.opensource.netty.redis.proxy.net.client.FfanRedisClient;
import com.opensource.netty.redis.proxy.net.server.support.FfanRedisServerHandler;

/**
 * @author liubing
 *
 */
public class FfanRedisServer {
	
	private Logger logger = LoggerFactory.getLogger(FfanRedisServer.class);

	private FfanRedisServerMasterCluster ffanRedisServerMasterCluster;

	// 线程组
	private static EventLoopGroup bossGroup = new NioEventLoopGroup(Runtime
			.getRuntime().availableProcessors());
	private static EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime
			.getRuntime().availableProcessors());

	//private Map<String, FfanRedisClient> ffanRedisServerBeanMap = new HashMap<String, FfanRedisClient>();

	/**
	 * @param conf
	 */
	public FfanRedisServer(
			FfanRedisServerMasterCluster ffanRedisServerMasterCluster) {
		super();
		this.ffanRedisServerMasterCluster = ffanRedisServerMasterCluster;
		init();
	}
	
	 /**
	   * 初始化客户端
	   */
	  private void init(){
		  if(ffanRedisServerMasterCluster!=null&&ffanRedisServerMasterCluster.getRedisServerClusterBeans()!=null&&ffanRedisServerMasterCluster.getRedisServerClusterBeans().size()>0){
			  for(FfanRedisServerClusterBean ffanRedisServerClusterBean:ffanRedisServerMasterCluster.getRedisServerClusterBeans()){
				  FfanRedisServerBean ffanRedisServerBean=ffanRedisServerClusterBean.getFfanMasterRedisServerBean();
				  if(ffanRedisServerBean!=null){//主
					  RedisProxyURL redisProxyURL=new RedisProxyURL(ffanRedisServerBean.getHost(), ffanRedisServerBean.getPort(),ffanRedisServerBean.getTimeout());
					  redisProxyURL.addParameterIfAbsent(RedisProxyParamType.maxServerConnection.getName(), String.valueOf(ffanRedisServerBean.getMaxActiveConnection()));
					  redisProxyURL.addParameterIfAbsent(RedisProxyParamType.maxClientConnection.getName(), String.valueOf(ffanRedisServerBean.getMaxIdleConnection()));
					  redisProxyURL.addParameterIfAbsent(RedisProxyParamType.minClientConnection.getName(), String.valueOf(ffanRedisServerBean.getMinConnection()));
					  
					  FfanRedisClient ffanRedisClient=new FfanRedisClient(redisProxyURL);
					  Boolean flag=ffanRedisClient.open();
					  if(flag){//master初始化成功
						  ffanRedisServerMasterCluster.getFfanRedisClientBeanMap().put(ffanRedisServerBean.getKey(), ffanRedisClient);
					  }
				  }
				  List<FfanRedisServerBean> ffanRedisServerClusterBeans=ffanRedisServerClusterBean.getFfanRedisServerClusterBeans();
				  if(ffanRedisServerClusterBeans!=null&&ffanRedisServerClusterBeans.size()>0){
					  for(FfanRedisServerBean ffanRedisServerCluster:ffanRedisServerClusterBeans){
						  RedisProxyURL redisProxyURL=new RedisProxyURL(ffanRedisServerCluster.getHost(), ffanRedisServerCluster.getPort(),ffanRedisServerCluster.getTimeout());
						  FfanRedisClient ffanRedisClient=new FfanRedisClient(redisProxyURL);
						  Boolean flag=ffanRedisClient.open();
						  if(flag){//cluster初始化成功
							  ffanRedisServerMasterCluster.getFfanRedisClientBeanMap().put(ffanRedisServerCluster.getKey(), ffanRedisClient);
						  }
					  }
				  }
				  
			  }
		  }
	  }
	
	  
	  
	/**
	 * 启动系统，开启接收连接，处理业务
	 */
	public void start() {

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
				.option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ch.pipeline().addLast("RedisRequestDecoder",
								new RedisRequestDecoder());
						ch.pipeline().addLast("RedisReplyEncoder",
								new RedisReplyEncoder());
						ch.pipeline().addLast(
								"FfanRedisServerHandler",
								new FfanRedisServerHandler(
										ffanRedisServerMasterCluster.getFfanRedisClientBeanMap(),ffanRedisServerMasterCluster));
					}
				});
		ChannelFuture channelFuture = bootstrap.bind(
				ffanRedisServerMasterCluster.getRedisProxyHost(),
				ffanRedisServerMasterCluster.getRedisProxyPort());
		channelFuture.syncUninterruptibly();
		logger.info("RedisProxy_Server 已经启动");
	}
}
