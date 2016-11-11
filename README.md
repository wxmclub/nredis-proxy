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
     
 详细文档地址 ：[https://my.oschina.net/liubingsmile/blog/786465](http://)