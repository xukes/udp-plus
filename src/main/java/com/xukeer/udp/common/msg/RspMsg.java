package com.xukeer.udp.common.msg;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 公共的消息
 * */
public class RspMsg extends Msg implements Comparable<Object>
{
	
	private byte[] bytes;   // 消息体 
	
	private InetSocketAddress sourceAdddress;  // 消息源端口

	
	public RspMsg(byte[] bytes, InetSocketAddress sourceAdddress) {
		super();
		this.bytes = bytes;
		this.sourceAdddress = sourceAdddress;
	}
	public byte[] getBytes() {
		return bytes;
	}
	
	public InetSocketAddress getSourceAdddress() {
		return sourceAdddress;
	}
	
	@Override
	public int compareTo(Object o) {
		return bytes.length;
	}
	@Override
	public void decodeMsg(DataInputStream strem, InetSocketAddress sourceAddr) throws IOException {
		
	}
	@Override
	public byte[] encodeMsg() throws IOException {
		return null;
	}
	
}
