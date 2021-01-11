package com.xukeer.udp.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;


/**
 * 消息报文包括
 * +-----------------------------------------------------+
 * +  魔数      | 消息序号   | 消息包长度   |  当前包序号  | 消息体长度   |  消息体    |  
 * +-----------------------------------------------------+
 * + 4byte | 4byte |   4byte |   4byte |  4byte  |  -    |
 * +-----------------------------------------------------+
 * **/
public class CommonMsg extends Msg {
	public final static short N = 1;
	
	
	private int sequence;
	
	private int totalPackage;
	
	private int packageIndex; // 该包是第几个数据包
	
	private int length;  // 消息长度
	
	private byte []msg;
	
	private InetSocketAddress sourceAddr;
	
	/**
	 * 解码消息
	 * @throws IOException 
	 * */
	@Override
	public void decodeMsg(DataInputStream strem, InetSocketAddress sourceAddr) throws IOException {
		this.sourceAddr = sourceAddr;
		this.sequence = strem.readInt();
		this.totalPackage = strem.readInt();
		this.packageIndex = strem.readInt();
		this.length = strem.readInt();
		this.msg = new byte[length];
		strem.readFully(msg);
	}
	
	/**
	 * 编码消息
	 * @throws IOException 
	 * */
	public byte[] encodeMsg() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream sss = new DataOutputStream(stream);
		sss.write(MAGIC);
		sss.writeShort(N);
		sss.writeInt(sequence);
		sss.writeInt(totalPackage);
		sss.writeInt(packageIndex);
		sss.writeInt(length);
		sss.write(msg);
		return stream.toByteArray();
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getPackageIndex() {
		return packageIndex;
	}

	public void setPackageIndex(int packageIndex) {
		this.packageIndex = packageIndex;
	}

	public int getTotalPackage() {
		return totalPackage;
	}

	public void setTotalPackage(int totalPackage) {
		this.totalPackage = totalPackage;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public byte[] getMsg() {
		return msg;
	}

	public void setMsg(byte[] msg) {
		this.msg = msg;
	}

	public InetSocketAddress getSourceAddr() {
		return sourceAddr;
	}
	
}
