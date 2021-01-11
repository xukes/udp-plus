package com.xukeer.udp.server;

import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerPool {
	
	public static DatagramSocket DATAGRAM_SOCKET;
	
	public ServerPool(SocketConfig config) throws SocketException {
		DATAGRAM_SOCKET = new DatagramSocket(config.getPort());
		DATAGRAM_SOCKET.setReceiveBufferSize(Integer.MAX_VALUE);
		DATAGRAM_SOCKET.setSendBufferSize(Integer.MAX_VALUE);
	}
	

	public  static DatagramSocket getSocket() {
		return DATAGRAM_SOCKET;
	}

}
