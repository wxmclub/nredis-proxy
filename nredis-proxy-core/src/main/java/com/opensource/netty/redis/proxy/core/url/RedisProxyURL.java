/**
 * 
 */
package com.opensource.netty.redis.proxy.core.url;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.opensource.netty.redis.proxy.commons.utils.StringUtils;

/**
 * @author liubing
 *
 */
public class RedisProxyURL {
	
	private String host;

	private int port;
	
	private int timeout;
	
	private Map<String, String> parameters=new HashMap<String, String>();
	
    private volatile transient Map<String, Number> numbers;
	
    private String parentServerPath;//host_port
    
    public RedisProxyURL(){
    	
    }
	/**
	 * @param host
	 * @param port
	 * @param timeout
	 */
	public RedisProxyURL(String host, int port, int timeout) {
		super();
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	 
	public String getParameter(String name) {
        return parameters.get(name);
    }

    public String getParameter(String name, String defaultValue) {
        String value = getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    public void addParameter(String name, String value) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) {
            return;
        }
        parameters.put(name, value);
    }

    public void removeParameter(String name) {
        if (name != null) {
            parameters.remove(name);
        }
    }

    public void addParameters(Map<String, String> params) {
        parameters.putAll(params);
    }

    public void addParameterIfAbsent(String name, String value) {
        if (hasParameter(name)) {
            return;
        }
        parameters.put(name, value);
    }

    public Boolean getBooleanParameter(String name, boolean defaultValue) {
        String value = getParameter(name);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        return Boolean.parseBoolean(value);
    }
    
    public boolean hasParameter(String key) {
        return StringUtils.isNotBlank(getParameter(key));
    }
    
    public Integer getIntParameter(String name, int defaultValue) {
        Number n = getNumbers().get(name);
        if (n != null) {
            return n.intValue();
        }
        String value = parameters.get(name);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        int i = Integer.parseInt(value);
        getNumbers().put(name, i);
        return i;
    }
    
    private Map<String, Number> getNumbers() {
        if (numbers == null) { // 允许并发重复创建
            numbers = new ConcurrentHashMap<String, Number>();
        }
        return numbers;
    }
    
    public Long getLongParameter(String name, long defaultValue) {
        Number n = getNumbers().get(name);
        if (n != null) {
            return n.longValue();
        }
        String value = parameters.get(name);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        long l = Long.parseLong(value);
        getNumbers().put(name, l);
        return l;
    }
    
    public Float getFloatParameter(String name, float defaultValue) {
        Number n = getNumbers().get(name);
        if (n != null) {
            return n.floatValue();
        }
        String value = parameters.get(name);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        float f = Float.parseFloat(value);
        getNumbers().put(name, f);
        return f;
    }
    
    public Boolean getBooleanParameter(String name) {
        String value = parameters.get(name);
        if (value == null) {
            return null;
        }
        return Boolean.parseBoolean(value);
    }

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
    
    public String getServerKey(){
    	StringBuilder stringBuilder=new StringBuilder();
    	stringBuilder.append(host).append(":").append(port);
    	return stringBuilder.toString();
    }

	/**
	 * @return the parentServerPath
	 */
	public String getParentServerPath() {
		return parentServerPath;
	}

	/**
	 * @param parentServerPath the parentServerPath to set
	 */
	public void setParentServerPath(String parentServerPath) {
		this.parentServerPath = parentServerPath;
	}
    
    
}
