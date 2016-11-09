/**
 * 
 */
package com.opensource.netty.redis.proxy.core.listen;

import java.util.List;

/**
 * @author liubing
 *
 */
public interface IRegistryListen {
	
	/**
	 * 监听处理数据变化
	 * @param dataPath
	 * @param data
	 */
	public void handleDataChange(String dataPath, Object data);
	
	/**
	 * 处理子节点变化
	 * 更新从权重的变化
	 * @param parentPath
	 * @param currentChilds
	 */
	public void handleChildChange(String data,String path, List<String> ChildDatas);
	
	/**
	 * 数据节点删除
	 * @param dataPath
	 */
	public void handleDataDeleted(String dataPath);
}
