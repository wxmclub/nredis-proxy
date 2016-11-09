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

import com.opensource.netty.redis.proxy.core.config.LBRedisServerMasterCluster;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerBean;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerClusterBean;
import com.opensource.netty.redis.proxy.core.enums.RedisProxyParamType;
import com.opensource.netty.redis.proxy.core.protocol.RedisReplyEncoder;
import com.opensource.netty.redis.proxy.core.protocol.RedisRequestDecoder;
import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;
import com.opensource.netty.redis.proxy.net.client.LBRedisClient;
import com.opensource.netty.redis.proxy.net.server.support.LBRedisServerHandler;

/**
 * @author liubing
 *
 */
public class LBRedisServer {
	
	private Logger logger = LoggerFactory.getLogger(LBRedisServer.class);

	private LBRedisServerMasterCluster ffanRedisServerMasterCluster;

	// 线程组
	private static EventLoopGroup bossGroup = new NioEventLoopGroup(Runtime
			.getRuntime().availableProcessors());
	private static EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime
			.getRuntime().availableProcessors());

	//private Map<String, FfanRedisClient> ffanRedisServerBeanMap = new HashMap<String, FfanRedisClient>();

	/**
	 * @param conf
	 */
	public LBRedisServer(
			LBRedisServerMasterCluster ffanRedisServerMasterCluster) {
		super();
		this.ffanRedisServerMasterCluster = ffanRedisServerMasterCluster;
		init();
	}
	
	 /**
	   * 初始化客户端
	   */
	  private void init(){
		  if(ffanRedisServerMasterCluster!=null&&ffanRedisServerMasterCluster.getRedisServerClusterBeans()!=null&&ffanRedisServerMasterCluster.getRedisServerClusterBeans().size()>0){
			  for(LBRedisServerClusterBean ffanRedisServerClusterBean:ffanRedisServerMasterCluster.getRedisServerClusterBeans()){
				  LBRedisServerBean ffanRedisServerBean=ffanRedisServerClusterBean.getFfanMasterRedisServerBean();
				  if(ffanRedisServerBean!=null){//主
					  RedisProxyURL redisProxyURL=new RedisProxyURL(ffanRedisServerBean.getHost(), ffanRedisServerBean.getPort(),ffanRedisServerBean.getTimeout());
					  redisProxyURL.addParameterIfAbsent(RedisProxyParamType.maxServerConnection.getName(), String.valueOf(ffanRedisServerBean.getMaxActiveConnection()));
					  redisProxyURL.addParameterIfAbsent(RedisProxyParamType.maxClientConnection.getName(), String.valueOf(ffanRedisServerBean.getMaxIdleConnection()));
					  redisProxyURL.addParameterIfAbsent(RedisProxyParamType.minClientConnection.getName(), String.valueOf(ffanRedisServerBean.getMinConnection()));
					  
					  LBRedisClient ffanRedisClient=new LBRedisClient(redisProxyURL);
					  Boolean flag=ffanRedisClient.open();
					  if(flag){//master初始化成功
						  ffanRedisServerMasterCluster.getFfanRedisClientBeanMap().put(ffanRedisServerBean.getKey(), ffanRedisClient);
					  }
				  }
				  List<LBRedisServerBean> ffanRedisServerClusterBeans=ffanRedisServerClusterBean.getFfanRedisServerClusterBeans();
				  if(ffanRedisServerClusterBeans!=null&&ffanRedisServerClusterBeans.size()>0){
					  for(LBRedisServerBean ffanRedisServerCluster:ffanRedisServerClusterBeans){
						  RedisProxyURL redisProxyURL=new RedisProxyURL(ffanRedisServerCluster.getHost(), ffanRedisServerCluster.getPort(),ffanRedisServerCluster.getTimeout());
						  LBRedisClient ffanRedisClient=new LBRedisClient(redisProxyURL);
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
								new LBRedisServerHandler(
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
