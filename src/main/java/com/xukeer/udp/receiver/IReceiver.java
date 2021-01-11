package com.xukeer.udp.receiver;

import java.net.InetSocketAddress;

public interface IReceiver {
	
	void receiveMsg(InetSocketAddress sourceAddr, byte[] bytes);
}
