package com.xukeer.udp.sender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.xukeer.udp.common.msg.RspSuccess;
import com.xukeer.udp.server.MsgPackage;
import com.xukeer.udp.server.RspMsgWaiter;

/**
 * 消息发送器
 * */
public class Sender {
	private static Sender INSTANCE;

	/**
	 * 所有发送的消息都会暂时存放在这里，直到接收到相应，或者时被垃圾被回收
	 * */
	private static Map<Integer, MsgPackage> senderMsg = new HashMap<>();
	private static Map<Integer, RspMsgWaiter> msgWaiter = new HashMap<>();
	
	private Sender() {
		
	}
	
	public static Sender getInstance() {
		if (INSTANCE == null) {
			synchronized (Sender.class) {
				if (INSTANCE == null) {
					INSTANCE = new Sender();
				}
			}
		}
		return INSTANCE;
	}
	
	/**
	 * 消息发送器
	 * @throws InterruptedException 
	 * */
	public RspSuccess sendMsg(byte[] bytes, InetSocketAddress targertAddr) throws IOException, InterruptedException {
		MsgPackage pa = new MsgPackage(bytes, targertAddr);
		RspMsgWaiter waiter = new RspMsgWaiter();
		msgWaiter.put(pa.getSequence(), waiter);
		 pa.send();
		senderMsg.put(pa.getSequence(), pa);
		return waiter.waitRsp();
	}
	
	public void onReceive(Integer sequence, RspSuccess rsp) {
		RspMsgWaiter waiter = msgWaiter.get(sequence);
		if(waiter!=null){
			waiter.onRsp(rsp);
		}
		msgWaiter.remove(sequence);
		senderMsg.remove(sequence);
	}
	
	public void retryMsg(Integer sequence, Integer[] index) throws IOException {
		System.out.println(System.currentTimeMillis()+"retry"+index.length);
		MsgPackage msg = senderMsg.get(sequence);
		if(msg== null){
			return;
		}
		for(int i=0;i<index.length;i++) {
			msg.send(index[i]);
		}
	}
}
