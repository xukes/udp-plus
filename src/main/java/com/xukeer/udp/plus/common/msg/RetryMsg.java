/**
 * 
 */
package com.xukeer.udp.plus.common.msg;


import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author xukeer
 * 通知对方重发的消息
 */
public final class RetryMsg extends Msg {

	private byte[] simpleMsgIndex; //丢失的消息

	public RetryMsg(long seq, int crowdIndex, byte[] simpleMsgIndex) {
		super.TYPE = MsgConstants.RETRY_MSG;
		super.setSequence(seq);

		super.setCrowdIndex(crowdIndex);
		this.simpleMsgIndex=simpleMsgIndex;
	}
	public RetryMsg() {
		super.TYPE = MsgConstants.RETRY_MSG;
	}
	@Override
	public byte[] childEncodeMsg(ByteArrayOutputStream stream, DataOutputStream dataOutputStream) {
		try {
			int length = simpleMsgIndex.length;
			dataOutputStream.writeInt(length);
			for (byte msgIndex : simpleMsgIndex) {
				dataOutputStream.writeByte(msgIndex);
			}
		}catch (IOException ignored){}
		return stream.toByteArray();
	}

	@Override
	public void childDecodeMsg(DataInputStream stream) throws IOException {
		int length = stream.readInt();
		if(length > 8096) {
			return;
		}
		simpleMsgIndex = new byte[length];
		for (int i = 0; i < length; i++) {
			simpleMsgIndex[i] = stream.readByte();
		}
	}

	public byte[] getSimpleMsgIndex() {
		return simpleMsgIndex;
	}

	@Override
	public String toString() {
		return "RetryMsg{" +
				"crowdIndex=" + super.getCrowdIndex() +
				", simpleMsgIndex=" + intArrTOString(simpleMsgIndex) +
				", TYPE=" + TYPE +
				", sequence=" + sequence +
				'}';
	}

	private String intArrTOString(byte[] simpleMsgIndex){
		StringBuilder stringBuilder = new StringBuilder();
		for(int i: simpleMsgIndex){
			stringBuilder.append(i).append(",");
		}
		return stringBuilder.toString();
	}
}
