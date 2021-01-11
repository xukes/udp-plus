package com.xukeer.udp.server;

import java.net.DatagramSocket;
import java.net.InetAddress;

public interface IServerPool {
	
	/**
	 * 获取地址
	 * */
	InetAddress getAddress();
	
	/**
	 * 获取端口
	 * */
	int getPort();
	
	/**
	 * 获取UDP通信的socket
	 * */
	DatagramSocket getSocket();
}
