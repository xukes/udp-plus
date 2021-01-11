package com.xukeer.udp.server;

import java.net.InetAddress;

public interface ReceiveMsg {
	
	public void receiveMsg(InetAddress address, int port, byte[] msg);
	
}
