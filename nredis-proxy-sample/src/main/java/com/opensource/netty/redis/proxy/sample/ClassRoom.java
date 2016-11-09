/**
 * 
 */
package com.opensource.netty.redis.proxy.sample;

import java.io.Serializable;

/**
 * @author liubing
 *
 */
public class ClassRoom implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3045043390218727347L;
	
	private String name;
	
	private String height;
	
	private Integer weight;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * @return the weight
	 */
	public Integer getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	/**
	 * @param name
	 * @param height
	 * @param weight
	 */
	public ClassRoom(String name, String height, Integer weight) {
		super();
		this.name = name;
		this.height = height;
		this.weight = weight;
	}
	
	
}
