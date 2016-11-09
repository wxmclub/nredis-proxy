/**
 * 
 */
package com.opensource.netty.redis.proxy.zk.registry.listen;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.opensource.netty.redis.proxy.commons.constants.RedisConstants;
import com.opensource.netty.redis.proxy.commons.utils.StringUtils;
import com.opensource.netty.redis.proxy.core.config.LBRedisServerMasterCluster;
import com.opensource.netty.redis.proxy.core.config.support.LBRedisServerBean;
import com.opensource.netty.redis.proxy.core.listen.impl.AbstractRegistryListenImpl;
import com.opensource.netty.redis.proxy.core.log.impl.LoggerUtils;

/**
 * @author liubing
 *
 */
public class ZookeeperRegistryListen extends AbstractRegistryListenImpl {
	
	
	public ZookeeperRegistryListen(LBRedisServerMasterCluster ffanRedisServerMasterCluster) {
		super(ffanRedisServerMasterCluster);
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.listen.IRegistryListen#handleDataChange(java.lang.String, java.lang.Object)
	 */
	@Override
	public void handleDataChange(String dataPath, Object data) {
		//LoggerUtils.info("dataPath:"+dataPath+",data:"+data);
		int index=dataPath.lastIndexOf(RedisConstants.PATH_SEPARATOR);
		dataPath=dataPath.substring(index+1, dataPath.length());
		dataPath=dataPath.replace(RedisConstants.PROTOCOL_SEPARATOR, RedisConstants.SEPERATOR_ACCESS_LOG);
		StringBuffer sbBuffer=new StringBuffer();
		sbBuffer.append(RedisConstants.REDIS_PROXY).append(dataPath);
		LBRedisServerBean ffanRedisServerBean=JSONObject.parseObject(String.valueOf(data), LBRedisServerBean.class);
		super.getFfanRedisServerMasterCluster().updateFfanRedisMasterBean(sbBuffer.toString(), ffanRedisServerBean);
		
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.listen.IRegistryListen#handleChildChange(java.lang.String, java.util.List)
	 */
	@Override
	public void handleChildChange(String data,String dataPath, List<String> ChildDatas) {
		if(StringUtils.isBlank(data)){
			LoggerUtils.warn("this master had been deleted,path:"+dataPath);
		}else{
			int index=dataPath.lastIndexOf(RedisConstants.PATH_SEPARATOR);
			dataPath=dataPath.substring(index+1, dataPath.length());
			dataPath=dataPath.replace(RedisConstants.PROTOCOL_SEPARATOR, RedisConstants.SEPERATOR_ACCESS_LOG);
			StringBuffer sbBuffer=new StringBuffer();
			sbBuffer.append(RedisConstants.REDIS_PROXY).append(dataPath);
			List<LBRedisServerBean> ffanRedisClusterBeans=new ArrayList<LBRedisServerBean>();
			if(ChildDatas!=null&&ChildDatas.size()>0){//有从，从的值发生改变
				for(String childData:ChildDatas){
					LBRedisServerBean ffanRedisServerBean=JSONObject.parseObject(childData, LBRedisServerBean.class);
					ffanRedisClusterBeans.add(ffanRedisServerBean);
				}
				super.getFfanRedisServerMasterCluster().updateFfanRedisClusterBeans(sbBuffer.toString(), ffanRedisClusterBeans);
			}else{//有主无从
				super.getFfanRedisServerMasterCluster().removeSlavesByMaster(sbBuffer.toString());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.listen.IRegistryListen#handleDataDeleted(java.lang.String)
	 * 主节点删除的变化
	 */
	@Override
	public void handleDataDeleted(String dataPath) {
		int index=dataPath.lastIndexOf(RedisConstants.PATH_SEPARATOR);
		dataPath=dataPath.substring(index+1, dataPath.length());
		dataPath=dataPath.replace(RedisConstants.PROTOCOL_SEPARATOR, RedisConstants.SEPERATOR_ACCESS_LOG);
		StringBuffer sbBuffer=new StringBuffer();
		sbBuffer.append(RedisConstants.REDIS_PROXY).append(dataPath);
		super.getFfanRedisServerMasterCluster().remove(sbBuffer.toString());
	}

}
