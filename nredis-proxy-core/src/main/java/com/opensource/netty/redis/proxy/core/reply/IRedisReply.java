package com.opensource.netty.redis.proxy.core.reply;

import io.netty.buffer.ByteBuf;

import com.opensource.netty.redis.proxy.core.enums.Type;

/**
 * redis 回答
 * 
 * @author liubing
 *
 */
public interface IRedisReply {
	  
	  /**
	   * 获取类型
	   * @return
	   */
	  Type getType();
	  
	  /**
	   * 设置类型
	   * @param type
	   */
	  void setType(Type type);
	  
	  /**
	   * 编码
	   * @param out
	   */
	  void encode(ByteBuf out);
}
