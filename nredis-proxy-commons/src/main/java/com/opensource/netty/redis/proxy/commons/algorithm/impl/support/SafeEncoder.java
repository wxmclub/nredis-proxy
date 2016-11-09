package com.opensource.netty.redis.proxy.commons.algorithm.impl.support;

import java.io.UnsupportedEncodingException;

import com.opensource.netty.redis.proxy.commons.constants.RedisConstants;

/**
 * 
 * @author liubing
 *
 */
public class SafeEncoder {
	public static byte[][] encodeMany(final String... strs){
		byte[][] many = new byte[strs.length][];
		for(int i=0;i<strs.length;i++){
			many[i] = encode(strs[i]);
		}
		return many;
	}
	
    public static byte[] encode(final String str) {
        try {
            if (str == null) {
            }
            return str.getBytes(RedisConstants.DEFAULT_CHARACTER);
        } catch (UnsupportedEncodingException e) {
        }
		return null;
    }

    public static String encode(final byte[] data) {
        try {
			return new String(data, RedisConstants.DEFAULT_CHARACTER);
        } catch (UnsupportedEncodingException e) {
        }
		return null;
    }
}
