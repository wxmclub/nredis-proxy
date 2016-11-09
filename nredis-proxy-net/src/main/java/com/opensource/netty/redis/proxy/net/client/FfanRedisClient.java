/**
 * 
 */
package com.opensource.netty.redis.proxy.net.client;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensource.netty.redis.proxy.commons.constants.FfanRedisProxyErrorMsgConstant;
import com.opensource.netty.redis.proxy.commons.exception.FfanRedisProxyFrameworkException;
import com.opensource.netty.redis.proxy.core.client.impl.AbstractPoolClient;
import com.opensource.netty.redis.proxy.core.command.impl.RedisCommand;
import com.opensource.netty.redis.proxy.core.connection.IConnection;
import com.opensource.netty.redis.proxy.core.connection.IConnectionCallBack;
import com.opensource.netty.redis.proxy.core.enums.ChannelState;
import com.opensource.netty.redis.proxy.core.enums.RedisProxyParamType;
import com.opensource.netty.redis.proxy.core.protocol.RedisReplyDecoder;
import com.opensource.netty.redis.proxy.core.protocol.RedisRequestEncoder;
import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;
import com.opensource.netty.redis.proxy.net.client.support.FfanRedisClientInHandler;
import com.opensource.netty.redis.proxy.net.client.support.FfanRedisClientOutHandler;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPoolEntry;
import com.opensource.netty.redis.proxy.pool.FfanRedisProxyPooledObjectFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author liubing
 *
 */
public class FfanRedisClient extends AbstractPoolClient{

	private Logger logger = LoggerFactory.getLogger(FfanRedisClient.class);

	 // 连续失败次数
    private AtomicLong errorCount = new AtomicLong(0);
    // 最大连接数
    private int maxClientConnection = 0;

    private Bootstrap bootstrap;
    
    private Semaphore semaphore = new Semaphore(1);//保证同时只有一个线程使用这个channel发送请求，write(request)是获取permit,收到响应释放permit

	/**
	 * @param conf
	 */
	public FfanRedisClient(RedisProxyURL redisProxyURL) {
		 super(redisProxyURL);
	     maxClientConnection = redisProxyURL.getIntParameter(RedisProxyParamType.maxClientConnection.getName(),
	    		 RedisProxyParamType.maxClientConnection.getIntValue());
	}

	@Override
	public boolean open() {
		if (isAvailable()) {
            logger.warn("NettyServer ServerChannel already Opened: url=" + redisProxyURL);
            return true;
        }
		// 初始化netty client bootstrap
        initClientBootstrap();
        // 初始化连接池
        initPool();
        
        // 设置可用状态
        state = ChannelState.ALIVE;
        return state.isAliveState();
	}

	@Override
	public synchronized void close() {
		close(0);
	}

	@Override
	public synchronized void close(int timeout) {
		if (state.isCloseState()) {
            logger.info("NettyClient close fail: already close, url={}", redisProxyURL);
            return;
        }

        // 如果当前nettyClient还没有初始化，那么就没有close的理由。
        if (state.isUnInitState()) {
        	logger.info("NettyClient close Fail: don't need to close because node is unInit state: url={}",
                    redisProxyURL);
            return;
        }

        try {
        	errorCount.set(0);
            //连接池关闭
            pool.clear();
            // 设置close状态
            state = ChannelState.CLOSE;
            
            logger.info("NettyClient close Success: url={}", redisProxyURL);
        } catch (Exception e) {
        	logger.error("NettyClient close Error: url=" +redisProxyURL, e);
        }

	}

	@Override
	public boolean isClosed() {
        return state.isCloseState();
	}

	@Override
	public boolean isAvailable() {
        return state.isAliveState();
	}

	@Override
	protected FfanRedisProxyPooledObjectFactory<IConnection> createChannelFactory() {
		
		return new FfanRedisConnectionFactory(this);
	}

	
	/**
     * 初始化 netty clientBootstrap
     */
    private void initClientBootstrap() {
        bootstrap = new Bootstrap();

        NioEventLoopGroup group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new  ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {

				ch.pipeline().addLast("RedisReplyDecoder",new RedisReplyDecoder());
				ch.pipeline().addLast("RedisRequestEncoder",new RedisRequestEncoder());
				ch.pipeline().addLast("ClientInHandler",new FfanRedisClientInHandler());
				ch.pipeline().addLast("ClientOutHandler",new FfanRedisClientOutHandler());
			}
        	
        });

        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

		/* 实际上，极端情况下，connectTimeout会达到500ms，因为netty nio的实现中，是依赖BossThread来控制超时，
         如果为了严格意义的timeout，那么需要应用端进行控制。
		 */
        int timeout = getRedisProxyURL().getIntParameter(RedisProxyParamType.requestTimeout.getName(), RedisProxyParamType.requestTimeout.getIntValue());
        if (timeout <= 0) {
            throw new FfanRedisProxyFrameworkException("NettyClient init Error: timeout(" + timeout + ") <= 0 is forbid.",
                    FfanRedisProxyErrorMsgConstant.FRAMEWORK_INIT_ERROR);
        }
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, redisProxyURL.getTimeout());
    }
    
   
    /**
     * 增加调用失败的次数：
     * <p>
     * <pre>
     * 	 	如果连续失败的次数 >= maxClientConnection, 那么把client设置成不可用状态
     * </pre>
     */
   public void incrErrorCount() {
        long count = errorCount.incrementAndGet();

        // 如果节点是可用状态，同时当前连续失败的次数超过限制maxClientConnection次，那么把该节点标示为不可用
        if (count >= maxClientConnection && state.isAliveState()) {
            synchronized (this) {
                count = errorCount.longValue();

                if (count >= maxClientConnection && state.isAliveState()) {
                    logger.error("NettyClient unavailable Error: url=" + redisProxyURL);
                    state = ChannelState.UNALIVE;
                }
            }
        }
    }

    /**
     * 重置调用失败的计数 ：
     * <p>
     * <pre>
     * 把节点设置成可用
     * </pre>
     */
   public void resetErrorCount() {
        errorCount.set(0);

        if (state.isAliveState()) {
            return;
        }

        synchronized (this) {
            if (state.isAliveState()) {
                return;
            }

            // 如果节点是unalive才进行设置，而如果是 close 或者 uninit，那么直接忽略
            if (state.isUnAliveState()) {
                long count = errorCount.longValue();

                // 过程中有其他并发更新errorCount的，因此这里需要进行一次判断
                if (count < maxClientConnection) {
                    state = ChannelState.ALIVE;
                    logger.info("NettyClient recover available: url=" + redisProxyURL);
                }
            }
        }
    }

	/**
	 * @return the bootstrap
	 */
	public Bootstrap getBootstrap() {
		return bootstrap;
	}

	@Override
	public void write(RedisCommand request,IConnectionCallBack connectionCallBack) {
		IConnection connection=null;
		FfanRedisProxyPoolEntry<IConnection> entry=null;
		try{
			entry  = borrowObject();
        	if(entry==null||entry.getObject()==null){
        		logger.error("NettyClient borrowObject null");
                return ;
        	}
        	connection=entry.getObject();
            if(!connection.isAvailable()){
            	connection.open();
            }
            connection.write(request,connectionCallBack);
            
		}catch(Exception e){
			logger.error("NettyClient write request Error :" , e);
		}finally{
			returnObject(entry);
		}
		
	}
   
   
}
