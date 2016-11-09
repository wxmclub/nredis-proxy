/**
 * 
 */
package com.opensource.netty.redis.proxy.commons.conf;

import java.util.ResourceBundle;
/**
 * @author liubing
 *
 */
public class Configuration {
	
	  private String hedisHost;

	  private String hedisPort;

	  private String redisHost;

	  private String redisPort;

	  public Configuration() {
	    ResourceBundle hedis = ResourceBundle.getBundle("ffanredis");
	    this.hedisHost = hedis.getString("host");
	    this.hedisPort = hedis.getString("port");
	    ResourceBundle redis = ResourceBundle.getBundle("redis");
	    this.redisHost = redis.getString("host");
	    this.redisPort = redis.getString("port");
	  }

	  public String getHedisHost() {
	    return hedisHost;
	  }

	  public String getHedisPort() {
	    return hedisPort;
	  }

	  public String getRedisHost() {
	    return redisHost;
	  }

	  public String getRedisPort() {
	    return redisPort;
	  }

	  public static void main(String[] args) {
	    Configuration configuration = new Configuration();
	    System.out.println(configuration.getHedisHost());
	    System.out.println(configuration.getHedisPort());
	    System.out.println(configuration.getRedisHost());
	    System.out.println(configuration.getRedisPort());
	  }
}
