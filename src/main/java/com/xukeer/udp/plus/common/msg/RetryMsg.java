/**
 * 
 */
package com.xukeer.udp.plus.common.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author xukeer
 * 通知对方重发的消息
 */
public class RetryMsg extends Msg {
	public final static short N = 2; 
	
	private int sequence;  // 重发的消息序列号
	private int length;
	private Integer[] msgIndex;
	
	public RetryMsg() {
	}

	public RetryMsg (int sequence, Integer[] msgIndex) {
		this.sequence = sequence;
		this.msgIndex = msgIndex;
	}
	
	@Override
	public void decodeMsg(DataInputStream strem, InetSocketAddress sourceAddr) throws IOException {
		this.sequence = strem.readInt();
		this.length = strem.readInt();
		msgIndex = new Integer[length];
		for(int i=0;i<length;i++){
			msgIndex[i] = strem.readInt();
		}
	}
	@Override
	public byte[] encodeMsg() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream sss = new DataOutputStream(stream);
		sss.write(MAGIC);
		sss.writeShort(N);
		sss.writeInt(sequence);
		sss.writeInt(msgIndex.length);
		for(int i=0;i<msgIndex.length;i++){
			sss.writeInt(msgIndex[i]);
		}
		return stream.toByteArray();
	}

	public int getSequence() {
		return sequence;
	}

	public int getLength() {
		return length;
	}

	public Integer[] getMsgIndex() {
		return msgIndex;
	}
}
