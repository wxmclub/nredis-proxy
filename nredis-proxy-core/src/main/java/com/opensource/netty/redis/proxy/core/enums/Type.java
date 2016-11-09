/**
 * 
 */
package com.opensource.netty.redis.proxy.core.enums;

/**
 * 类型枚举类
 * @author liubing
 *
 */
public enum Type {
	
	ERROR((byte) '-'),
    STATUS((byte) '+'),
    BULK((byte) '$'),
    INTEGER((byte) ':'),
    MULTYBULK((byte) '*'),;

    private byte code;

    Type(byte code) {
      this.code = code;
    }

    public byte getCode() {
      return this.code;
    }
}
