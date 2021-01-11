package com.xukeer.udp.plus.server;

import java.net.InetAddress;

public interface ReceiveMsg {
	
	public void receiveMsg(InetAddress address, int port, byte[] msg);
	
}
