package com.opensource.netty.redis.proxy.monitor.spring.helper;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
/**
 * SpringFactory
 *
 * @author wangyang
 */
public class SpringFactory implements BeanFactoryAware {

    private static Logger logger = LogManager.getLogger(SpringFactory.class);
    private static BeanFactory beanFactory;

    public SpringFactory() {
    }

    @Override
    public void setBeanFactory(BeanFactory factory) throws BeansException {
        logger.info("初始化beanFactory==============");
        beanFactory = factory;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        if (null == beanFactory) {
            logger.error("beanFactory 为空============");
        }
        if (null != beanFactory) {
            return (T) beanFactory.getBean(beanName);
        }
        return null;
    }
}
