package com.xukeer.udp.plus.common.msg;


import com.xukeer.udp.plus.utils.ByteArrFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * 消息报文包括
 * +------------------------------------------------------------------------------|
 * +     魔数    | 消息序号   | 消息包长度   |  当前包序号  | 消息体长度   |  消息体      |
 * +------------------------------------------------------------------------------|
 * +    4byte   | 4byte     |   4byte    |   4byte     |  4byte     |  -    |
 * +------------------------------------------------------------------------------|
 * **/
public final class CommonMsg extends CrowdOptionMsg {
	private byte simpleIndex; // 当前基础消息在当前簇中的序号
	private int msgLength;  // 该基础消息共有多少个字节
	private byte []msg;  //消息体

	public CommonMsg(){
		super.TYPE = MsgConstants.COMMON_MSG;
	}
	public CommonMsg(long sequence, int totalCrowd, byte simMagTotal, int crowIndex, byte simpleMsgIndex, int commonMsgLength, byte[] commonMsgBytes) {
		super.TYPE = MsgConstants.COMMON_MSG;
		super.setSequence(sequence);
		super.setCrowdIndex(crowIndex);
		super.setTotalCrowdAmount(totalCrowd);
		super.setTotalSimpleAmount( simMagTotal);
		this.simpleIndex = simpleMsgIndex;
		this.msgLength = commonMsgLength;
		this.msg = commonMsgBytes;
	}



	@Override
	public byte[] childEncodeMsg(ByteArrayOutputStream stream, DataOutputStream dataOutputStream) {
		try {
			dataOutputStream.writeInt(super.getTotalCrowdAmount());
			dataOutputStream.writeByte(super.getTotalSimpleAmount());
			dataOutputStream.writeByte(simpleIndex);
			dataOutputStream.writeInt(msgLength);
			dataOutputStream.write(msg);
//			dataOutputStream.write(msg, 4, msgLength);
//			ByteArrFactory.giveBackByteArr(msg);
		} catch (IOException ignored){ }
		return stream.toByteArray();
	}

	@Override
	public void giveBackByteArr() {
		//ByteArrFactory.giveBackByteArr(msg);
	}

	@Override
	public void childDecodeMsg(DataInputStream stream) throws IOException {
		super.setTotalCrowdAmount(stream.readInt());
		this.setTotalSimpleAmount(stream.readByte());
		this.simpleIndex=stream.readByte();
		this.msgLength = stream.readInt();
		this.msg = ByteArrFactory.applyByteArr(this.msgLength);
		stream.read(msg,4, this.msgLength);

	}

	public int getSimpleIndex() {
		return simpleIndex;
	}

	public int getMsgLength() {
		return msgLength;
	}

	public byte[] getMsg() {
		return msg;
	}

//	@Override
//	public void finalize(){
//		ByteArrFactory.giveBackByteArr(msg);
//	}

	@Override
	public String toString() {
		return "CommonMsg{" +
				"totalCrowdAmount=" +super.getTotalCrowdAmount() +
				", crowdIndex=" + super.getCrowdIndex() +
				", totalSimpleAmount=" +super.getTotalSimpleAmount() +
				", simpleIndex=" + simpleIndex +
				", msgLength=" + msgLength +
				", TYPE=" + TYPE +
				", sequence=" + sequence +
				'}';
	}
}

