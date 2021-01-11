package com.xukeer.udp.server;

public class ReviveMsg {
	private int queue;
	
	private int totalLength;
	
	private byte[] receiveMsg;

	public int getQueue() {
		return queue;
	}

	public void setQueue(int queue) {
		this.queue = queue;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}
	
	
	public void receiveMsg(byte[] ss) {
		if(receiveMsg == null) {
			
		}
	}
}
