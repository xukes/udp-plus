package com.xukeer.udp.plus.receiver;

import java.net.InetSocketAddress;

public interface IReceiver {
	
	void receiveMsg(InetSocketAddress sourceAddr, byte[] bytes);
}
