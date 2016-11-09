/**
 * 
 */
package com.opensource.netty.redis.proxy.core.enums;

/**
 * redis 命令集合
 * @author liubing
 *
 */
public enum RedisCommandEnums {
	
	SET("SET",true),
	DEL("DEL",true),
	MGET("MGET",false),
	KEYS("KEYS",false),
	EXPIRE("EXPIRE",true),
	EXPIREAT("EXPIREAT",true),
	MIGRATE("MIGRATE",true),
	MOVE("MOVE",true),
	OBJECT("OBJECT",false),
	PERSIST("PERSIST",true),
	PEXPIRE("PEXPIRE",true),
	PEXPIREAT("PEXPIREAT",true),
	PTTL("PTTL",true),
	RANDOMKEY("RANDOMKEY",false),
	RENAME("RENAME",true),
	RENAMENX("RENAMENX",true),
	RESTORE("RESTORE",true),
	SORT("SORT",false),
	TTL("TTL",true),
	TYPE("TYPE",false),
	SCAN("SCAN",false),
	APPEND("APPEND",true),
	BITCOUNT("BITCOUNT",true),
	BITOP("BITOP",true),
	DECR("DECR",true),
	DECRBY("DECRBY",true),
	GETBIT("GETBIT",false),
	GETRANGE("GETRANGE",false),
	GETSET("GETSET",true),
	INCR("INCR",true),
	INCRBY("INCRBY",true),
	INCRBYFLOAT("INCRBYFLOAT",true),
	MSET("MSET",true),
	MSETNX("MSETNX",true),
	PSETEX("PSETEX",true),
	SETBIT("SETBIT",true),
	SETEX("SETEX",true),
	SETNX("SETNX",true),
	SETRANGE("SETRANGE",true),
	STRLEN("STRLEN",true),
	HDEL("HDEL",true),
	HEXISTS("HEXISTS",false),
	HGET("HGET",false),
	HGETALL("HGETALL",false),
	HINCRBY("HINCRBY",true),
	HINCRBYFLOAT("HINCRBYFLOAT",true),
	HKEYS("HKEYS",false),
	HLEN("HLEN",false),
	HMGET("HMGET",false),
	HMSET("HMSET",true),
	HSET("HSET",true),
	HSETNX("HSETNX",true),
	HVALS("HVALS",false),
	HSCAN("HSCAN",false),
	BLPOP("BLPOP",true),
	BRPOP("BRPOP",true),
	BRPOPLPUSH("BRPOPLPUSH",true),
	LINDEX("LINDEX",false),
	LINSERT("LINSERT",true),
	LLEN("LLEN",false),
	LPOP("LPOP",true),
	LPUSH("LPUSH",true),
	LPUSHX("LPUSHX",true),
	LRANGE("LRANGE",false),
	LREM("LREM",true),
	LSET("LSET",true),
	LTRIM("LTRIM",true),
	RPOP("RPOP",true),
	RPOPLPUSH("RPOPLPUSH",true),
	RPUSH("RPUSH",true),
	RPUSHX("RPUSHX",true),
	SADD("SADD",true),
	SCARD("SCARD",false),
	SDIFF("SDIFF",true),
	SDIFFSTORE("SDIFFSTORE",true),
	SINTER("SINTER",true),
	
	SINTERSTORE("SINTERSTORE",true),
	SISMEMBER("SISMEMBER",false),
	SMEMBERS("SMEMBERS",false),
	SMOVE("SMOVE",true),
	SPOP("SPOP",true),
	SRANDMEMBER("SRANDMEMBER",false),
	SREM("SREM",true),
	SUNION("SUNION",false),
	SUNIONSTORE("SUNIONSTORE",false),
	SSCAN("SSCAN",false),
	ZADD("ZADD",true),
	ZCARD("ZCARD",false),
	ZCOUNT("ZCOUNT",false),
	ZINCRBY("ZINCRBY",true),
	ZRANGE("ZRANGE",false),
	ZRANGEBYSCORE("ZRANGEBYSCORE",true),
	ZRANK("ZRANK",false),
	ZREM("ZREM",true),
	ZREMRANGEBYRANK("ZREMRANGEBYRANK",true),
	ZREMRANGEBYSCORE("ZREMRANGEBYSCORE",true),
	ZREVRANGE("ZREVRANGE",false),
	ZREVRANGEBYSCORE("ZREVRANGEBYSCORE",false),
	ZREVRANK("ZREVRANK",false),
	ZSCORE("ZSCORE",false),
	ZINTERSTORE("ZINTERSTORE",true),
	ZSCAN("ZSCAN",false),
	DISCARD("DISCARD",true),
	ZUNIONSTORE("ZUNIONSTORE",true),
	
	EXEC("EXEC",true),
	MULTI("MULTI",true),
	UNWATCH("UNWATCH",true),
	WATCH("WATCH",true),
	UNSUBSCRIBE("UNSUBSCRIBE",true),
	
	SUBSCRIBE("SUBSCRIBE",true),
	PUNSUBSCRIBE("PUNSUBSCRIBE",true),
	PUBSUB("PUBSUB",true),
	PUBLISH("PUBLISH",true),
	PSUBSCRIBE("PSUBSCRIBE",true),
	GET("GET",false);
	
	
	
	private String command;
	
	private boolean iswrite;

	/**
	 * @param command
	 * @param iswrite
	 */
	private RedisCommandEnums(String command, boolean iswrite) {
		this.command = command;
		this.iswrite = iswrite;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the iswrite
	 */
	public boolean isIswrite() {
		return iswrite;
	}

	/**
	 * @param iswrite the iswrite to set
	 */
	public void setIswrite(boolean iswrite) {
		this.iswrite = iswrite;
	}
	
	
}
