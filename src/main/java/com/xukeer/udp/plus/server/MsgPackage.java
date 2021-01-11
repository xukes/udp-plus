package com.xukeer.udp.plus.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.xukeer.udp.plus.common.msg.CommonMsg;
import com.xukeer.udp.plus.sender.DeepSend;

/**
 * 消息小包
 * */
public class MsgPackage {
	final static int DATALENGTH = 4096;  // 每个数据包的长度
	
	private int sequence;
	private byte[] totalMsg;
	private int totalDataPackageLength;  // 总共有多少个数据包
	private long createTime; // 消息创建的时间,用来控制超时被删除操作
	private InetSocketAddress targetAddr; // 目标地址
	
	public MsgPackage(byte[] msg, InetSocketAddress targetAddr) throws UnknownHostException {
		this.createTime = System.currentTimeMillis();
		this.targetAddr = targetAddr;
		this.sequence = (int) (Math.random() * Integer.MAX_VALUE);
		this.totalMsg = msg;
		this.totalDataPackageLength = msg.length / DATALENGTH;
		if( msg.length % DATALENGTH > 0) {
			totalDataPackageLength ++;
		}
	}
	
	public void send() throws IOException {
		for(int i = 0; i < totalDataPackageLength; i ++) {
			byte[] sendData = getMsgByPackIndex(i);
			CommonMsg msg = new CommonMsg();
			msg.setSequence(sequence);
			msg.setTotalPackage(totalDataPackageLength);
			msg.setPackageIndex(i);
			msg.setLength(sendData.length);
			msg.setMsg(sendData);
			DeepSend.getInstance().send(msg.encodeMsg(),targetAddr);
		}
	}
	
	public void send(int index) throws IOException {
		byte[] sendData = getMsgByPackIndex(index);
		CommonMsg msg = new CommonMsg();
		msg.setSequence(sequence);
		msg.setTotalPackage(totalDataPackageLength);
		msg.setPackageIndex(index);
		msg.setLength(sendData.length);
		msg.setMsg(sendData);
		DeepSend.getInstance().send(msg.encodeMsg(),targetAddr);
	}
	
	private byte[] getMsgByPackIndex(int index) {
		// 如果不是最后一帧，如果该消息只有一帧的话，则当作最后一帧处理
		if(index < totalDataPackageLength-1) {  
			byte[] sendBytes = new byte[DATALENGTH];
			System.arraycopy(totalMsg, DATALENGTH * index, sendBytes, 0, DATALENGTH);
			return sendBytes;
		} else {   // 如果为最后一帧
			int length = totalMsg.length - DATALENGTH * index;
			byte[] sendBytes = new byte[length];
			System.arraycopy(totalMsg, DATALENGTH * index, sendBytes, 0, length);
			return sendBytes;
		}
	}
	
	/**
	 * 获取该消息的创建时间
	 * */
	public long getCreateTime() {
		return createTime;
	}
	
	/**
	 * 获取大的消息包序号
	 * */
	public int getSequence() {
		return sequence;
	}

	public int getTotalDataPackageLength() {
		return totalDataPackageLength;
	}
	
	
	
}
