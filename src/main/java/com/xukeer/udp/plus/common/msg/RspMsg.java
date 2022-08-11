package com.xukeer.udp.plus.common.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * // 完成接收后的确认消息
 * */
public final class RspMsg extends Msg {
	public RspMsg() {
		super.TYPE = MsgConstants.RECEIVE_MSG;
	}


	@Override
	public byte[] childEncodeMsg(ByteArrayOutputStream stream, DataOutputStream dataOutputStream) {
		return stream.toByteArray();
	}

	@Override
	public void childDecodeMsg(DataInputStream stream){
	}


	@Override
	public String toString() {
		return "RspMsg{" +
				"crowdIndex=" + super.getCrowdIndex() +
				", TYPE=" + TYPE +
				", sequence=" + sequence +
				'}';
	}


}
