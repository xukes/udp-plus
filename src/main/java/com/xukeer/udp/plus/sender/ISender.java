package com.xukeer.udp.plus.sender;

import java.io.IOException;
import java.net.InetSocketAddress;

public interface ISender {
	
	/**
	 * 发送消息
	 * */
	void sendMsg(byte[] bytes, InetSocketAddress targertAddr)throws IOException ;
}
