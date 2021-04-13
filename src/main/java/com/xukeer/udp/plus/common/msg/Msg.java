package com.xukeer.udp.plus.common.msg;

import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

@Data
public abstract class Msg {
	public final static byte[] MAGIC = {(byte)0xEF, (byte)0xEF, (byte)0xEF, (byte)0xEF};  // 使用本协议的魔术，放在于消息头
	
	public final static byte STATUS_SUCCESS = 1;  // 状态，成功
	public final static byte STATUS_FAILED = 0;   // 状态，失败


	public int TYPE;

	private int sequence;   // 序号，用来唯一标识一个消息，为随机数

	private int totalCrowdAmount;  // 该消息总共有多少个簇

	private int crowdIndex; // 当前简单消息的簇序号

	private int totalSimpleAmount; // 当前消息当前簇共有多少个基础消息体

	private int simpleIndex; // 当前基础消息在当前簇中的序号

	private int msgLength;  // 该基础消息共有多少个字节

	private byte []msg;  //消息体

	private InetSocketAddress sourceAddr;
	
	public void decodeMsg1(DataInputStream stream, InetSocketAddress sourceAddr) throws IOException {
		decodeMsg(stream, sourceAddr);
	}

	/**
	 * 解码消息， 给子类读的
	 * */
	public void decodeMsg(DataInputStream stream, InetSocketAddress sourceAddr) throws IOException{
		setSourceAddr(sourceAddr);
		this.sequence = stream.readInt();
		this.TYPE = stream.readShort();
		this.totalCrowdAmount=stream.readInt();
		this.crowdIndex=stream.readInt();
		this.totalSimpleAmount=stream.readInt();
		this.simpleIndex=stream.readInt();
		int length =stream.readInt();
		this.msg = new byte[length];
		stream.readFully(msg);
	}
	
	/**
	 * 编码消息，给子类的方法
	 * */
	public byte[] encodeMsg() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(stream);
		dataOutputStream.write(MAGIC);
		dataOutputStream.writeInt(getSequence());
		dataOutputStream.writeShort(TYPE);
		dataOutputStream.writeInt(getTotalCrowdAmount());
		dataOutputStream.writeInt(getCrowdIndex());
		dataOutputStream.writeInt(getTotalSimpleAmount());
		dataOutputStream.writeInt(getSimpleIndex());
		dataOutputStream.writeInt(getMsgLength());
		dataOutputStream.write(getMsg());
		return stream.toByteArray();
	}
}
