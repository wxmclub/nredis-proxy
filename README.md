nredis-proxy 是一个以redis 协议为主的高性能稳定的代理中间件服务，不侵入业务代码，与业务毫无联系，不需要改任何应用代码，天然支持分布式部署。

一：功能特点： 
    
     1：自带连接池，性能高效
     
     2：提供分片策略，扩展性强，可自定义分片算法
     
     3：提供读写分离，一主多从,从按照权重读取
     
     4：提供自动监听功能，主挂了，提供选举算法，从作为主

     5：可HA分布式部署，节点随意扩展

二：nredis-proxy 架构图
 
   ![输入图片说明](http://git.oschina.net/uploads/images/2016/1110/000750_ed0b1d9f_54128.png "在这里输入图片标题")

三：nredis-proxy 部署架构图

![输入图片说明](http://git.oschina.net/uploads/images/2016/1110/000104_44f5a5cb_54128.png "在这里输入图片标题")
     
 四：例子配置文件：

   <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:redisProxy="http://www.nredisproxy.com/redisProxy"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xsi:schemaLocation="http://www.springframework.org/schema/beans 
        http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
        http://www.nredisproxy.com/redisProxy
        http://www.nredisproxy.com/redisProxy/redisProxy.xsd" >
    <!--redis 主从配置  -->
    
    <redisProxy:redisProxyNode id="redisnode"  redisProxyHost="127.0.0.1" redisProxyPort="6379" algorithm-ref="loadMasterBalance" address="127.0.0.1:2181">
      <redisProxy:redisProxyMaster id="redismasters" host="127.0.0.1" port="6380" timeout="5000" maxActiveConnection="5000" maxIdleConnection="500" minConnection="50" algorithm-ref="loadClusterBalance">
      	  <redisProxy:redisProxyCluster id="rediscluster0" host="127.0.0.1" port="6381" timeout="5000" maxActiveConnection="5000" maxIdleConnection="500" minConnection="50" weight="1"></redisProxy:redisProxyCluster>
      </redisProxy:redisProxyMaster> 
    </redisProxy:redisProxyNode>
    

    <bean name="loadMasterBalance" class="com.opensource.netty.redis.proxy.core.cluster.impl.ConsistentHashLoadBalance"></bean>

 	<bean name="loadClusterBalance" class="com.opensource.netty.redis.proxy.core.cluster.impl.RoundRobinLoadBalance"></bean>
 	
 </beans>
     