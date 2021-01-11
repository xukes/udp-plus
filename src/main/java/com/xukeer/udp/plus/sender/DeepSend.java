package com.xukeer.udp.plus.sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.SynchronousQueue;

import com.xukeer.udp.plus.server.ServerPool;

/**
 * 消息真正发送的地方
 * */
public class DeepSend {
	
	private static DeepSend INSTANCE;
	private DatagramSocket socket;

	private DeepSend(){
		socket = ServerPool.getSocket();
		new Thread(() -> {
			try {
					DatagramPacket datagramPacket = synchronousQueue.take();
					socket.send(datagramPacket);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	private SynchronousQueue<DatagramPacket> synchronousQueue = new SynchronousQueue<>();
	
	public static DeepSend getInstance() {
		if (INSTANCE == null) {
			synchronized (DeepSend.class) {
				if (INSTANCE == null) {
					INSTANCE = new DeepSend();
				}
			}
		}
		return INSTANCE;
	}
	
	public synchronized void send(byte[] msg, InetSocketAddress targertAddr) throws IOException {
		DatagramPacket packet = new DatagramPacket(msg, msg.length, targertAddr);
		synchronized (synchronousQueue) {
			try {
				synchronousQueue.put(packet);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
