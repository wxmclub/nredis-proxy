/**
 * 
 */
package com.opensource.netty.redis.proxy.core.client;

/**
 * @author liubing
 *
 */
public interface Client {

	/**
     * open the channel
     * 
     * @return
     */
    boolean open();

    /**
     * close the channel.
     */
    void close();

    /**
     * close the channel gracefully.
     */
    void close(int timeout);

    /**
     * is closed.
     * 
     * @return closed
     */
    boolean isClosed();

    /**
     * the node available status
     * 
     * @return
     */
    boolean isAvailable();

}
