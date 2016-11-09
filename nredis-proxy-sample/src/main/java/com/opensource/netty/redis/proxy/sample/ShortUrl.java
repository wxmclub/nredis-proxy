/**
 * 
 */
package com.opensource.netty.redis.proxy.sample;

/**
 * @author liubing
 *
 */
public class ShortUrl {
	
	public static final String ALPHABET = "23456789bcdfghjkmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ-_";
	public static final int BASE = ALPHABET.length();

	public static String encode(int num) {
		StringBuilder str = new StringBuilder();
		while (num > 0) {
			str.insert(0, ALPHABET.charAt(num % BASE));
			num = num / BASE;
		}
		return str.toString();
	}

	public static int decode(String str) {
		int num = 0;
		for (int i = 0; i < str.length(); i++) {
			num = num * BASE + ALPHABET.indexOf(str.charAt(i));
		}
		return num;
	}
	
	public static void main(String[] args) {
		System.out.println(ShortUrl.encode(1148665819));
		System.out.println(ShortUrl.decode("baidu"));
	}
}
