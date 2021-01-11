/**
 * 
 */
package com.xukeer.udp.server;

import java.net.InetAddress;

/**
 * @author xukeer
 *
 */
public class SocketConfig {
	private  int port ;

	public SocketConfig(int port) {
		this.port = port;
	}
	
	/**
	 * 获取端口
	 * */
	public  int getPort() {
		return port;
	}
	
	/**
	 * 获取地址
	 * */
}
