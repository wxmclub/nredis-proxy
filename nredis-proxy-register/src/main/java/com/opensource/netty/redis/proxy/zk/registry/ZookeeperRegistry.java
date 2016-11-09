/**
 * 
 */
package com.opensource.netty.redis.proxy.zk.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import com.opensource.netty.redis.proxy.commons.exception.LBRedisProxyFrameworkException;
import com.opensource.netty.redis.proxy.core.enums.RedisProxyParamType;
import com.opensource.netty.redis.proxy.core.enums.ZkNodeType;
import com.opensource.netty.redis.proxy.core.listen.IRegistryListen;
import com.opensource.netty.redis.proxy.core.registry.impl.AbstractRegistry;
import com.opensource.netty.redis.proxy.core.registry.impl.support.ZkUtils;
import com.opensource.netty.redis.proxy.core.url.RedisProxyURL;

/**
 * @author liubing
 *
 */
public class ZookeeperRegistry extends AbstractRegistry {
    
	private ZkClient zkClient;
	private final ReentrantLock clientLock = new ReentrantLock();
    private final ReentrantLock serverLock = new ReentrantLock();
	
	public ZookeeperRegistry(RedisProxyURL redisProxyURL, ZkClient client) {
		super(redisProxyURL);
		this.zkClient=client;
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.registry.impl.AbstractRegistry#doRegister(com.wanda.ffan.redis.proxy.core.url.RedisProxyURL)
	 */
	@Override
	protected void doRegister(RedisProxyURL url,final IRegistryListen registryListen) {
		try{
			 serverLock.lock();
			 removeNode(url, ZkNodeType.AVAILABLE_SERVER);
			 createNode(url, ZkNodeType.AVAILABLE_SERVER);
		     String nodeTypePath = ZkUtils.toNodePath(url,url.getParentServerPath(), ZkNodeType.AVAILABLE_SERVER);
			 if(url.getParentServerPath()==null){
				 zkClient.subscribeDataChanges(nodeTypePath, new IZkDataListener() {					
					@Override
					public void handleDataDeleted(String dataPath) throws Exception {
						registryListen.handleDataDeleted(dataPath);
					}					
					@Override
					public void handleDataChange(String dataPath, Object data) throws Exception {
						registryListen.handleDataChange(dataPath, data);
					}
				});
				 zkClient.subscribeChildChanges(nodeTypePath, new IZkChildListener() {
					
					@Override
					public void handleChildChange(String parentPath, List<String> currentChilds)
							throws Exception {
						String value=zkClient.readData(parentPath, true);
						currentChilds=zkClient.getChildren(parentPath);
						List<String> childDatas=new ArrayList<String>();
						if(currentChilds!=null){
							for(String currentChild:currentChilds){
								StringBuilder sbStringBuilder=new StringBuilder();
								sbStringBuilder.append(parentPath).append("/").append(currentChild);
								String result=zkClient.readData(sbStringBuilder.toString(), true);
								childDatas.add(result);
							}
						}
						registryListen.handleChildChange(value,parentPath, childDatas);
					}
				});
			 }
		}catch(Exception e){
            throw new LBRedisProxyFrameworkException(String.format("Failed to register %s to zookeeper(%s), cause: %s", url, getRedisProxyURL(), e.getMessage()), e);
		}finally{
			 serverLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.registry.impl.AbstractRegistry#doUnregister(com.wanda.ffan.redis.proxy.core.url.RedisProxyURL)
	 */
	@Override
	protected void doUnregister(RedisProxyURL url) {
		 try {
	            clientLock.lock();
	            
	            removeNode(url, ZkNodeType.AVAILABLE_SERVER);
	        } catch (Throwable e) {
	            throw new LBRedisProxyFrameworkException(String.format("Failed to unregister %s to zookeeper(%s), cause: %s", url, getRedisProxyURL(), e.getMessage()), e);
	        } finally {
	        	clientLock.unlock();
	        }
	}

	/* (non-Javadoc)
	 * @see com.wanda.ffan.redis.proxy.core.registry.impl.AbstractRegistry#doDelete(com.wanda.ffan.redis.proxy.core.url.RedisProxyURL, java.lang.String)
	 */
	@Override
	protected boolean doDelete(RedisProxyURL redisProxyURL) {
		try{
			 removeNode(redisProxyURL, ZkNodeType.AVAILABLE_SERVER);
		}catch(Exception e){
			return false;
		}
		 return true;
	}

	/* (non-Javadoc)
	 * ip:port:weight
	 * 比如:
	 * @see com.wanda.ffan.redis.proxy.core.registry.impl.AbstractRegistry#doCreatePersistent(com.wanda.ffan.redis.proxy.core.url.RedisProxyURL, java.lang.String, java.lang.String)
	 */
	@Override
	protected void doCreatePersistent(RedisProxyURL redisProxyURL,String value) {
		 //removeNode(redisProxyURL, ZkNodeType.AVAILABLE_SERVER);
		 redisProxyURL.addParameter(RedisProxyParamType.REDISSERVER.getName(), value);
		 createNode(redisProxyURL, ZkNodeType.AVAILABLE_SERVER);
	}
	
	private void removeNode(RedisProxyURL url, ZkNodeType nodeType) {
        String nodePath = ZkUtils.toNodePath(url,url.getParentServerPath(), nodeType);
        List<String> childRen=new ArrayList<String>();
        if (zkClient.exists(nodePath)) {
        	try{
        		childRen=zkClient.getChildren(nodePath);
        		
        	}catch(Exception e){
        		
        	}
        	if(childRen==null||childRen.size()==0){
                zkClient.delete(nodePath);

        	}
        }
    }
	
	private void createNode(RedisProxyURL url, ZkNodeType nodeType) {
        String nodeTypePath = ZkUtils.toNodePath(url,url.getParentServerPath(), nodeType);
        if (!zkClient.exists(nodeTypePath)) {
            zkClient.createPersistent(nodeTypePath, true);
            
        }
        
        zkClient.writeData(nodeTypePath, url.getParameter(RedisProxyParamType.REDISSERVER.getName(),RedisProxyParamType.REDISSERVER.getValue()));
        
	}

	@Override
	public List<String> getChildren(String parentPath) {
		List<String> strings=zkClient.getChildren(parentPath);
		return strings;
	}

	@Override
	public String readData(String path, boolean flag) {
		String result=zkClient.readData(path, flag);
		return result;
	}
}
