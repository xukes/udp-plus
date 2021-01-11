package com.xukeer.udp.common.msg;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class Msg {
	public final static byte[] MAGIC = {(byte)0xEF, (byte)0xEF, (byte)0xEF, (byte)0xEF};  // 使用本协议的魔术，放在于消息头
	
	public final static byte STATUS_SUCCESS = 1;  // 状态，成功
	public final static byte STATUS_FAILED = 0;   // 状态，失败
	
	
	public void decodeMsg1(DataInputStream strem, InetSocketAddress sourceAddr) throws IOException {
		decodeMsg(strem, sourceAddr);
	}
	
	public byte[] encodeMsg1() throws IOException{
		return encodeMsg();
	}
	
	/**
	 * 解码消息， 给子类读的
	 * */
	public abstract void decodeMsg(DataInputStream strem, InetSocketAddress sourceAddr) throws IOException;
	
	/**
	 * 编码消息，给子类的方法
	 * */
	public abstract byte[] encodeMsg() throws IOException ;
}
