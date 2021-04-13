package com.xukeer.udp.plus.common.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

public class RspSuccess extends Msg {
	public final static Short N = 3;
	private int sequence;
	private byte status;
	

	public RspSuccess() {
		super();
	}

	public RspSuccess(int sequence, byte status) {
		super();
		this.sequence = sequence;
		this.status = status;
	}

	@Override
	public void decodeMsg(DataInputStream stream, InetSocketAddress sourceAddr) throws IOException {
		sequence = stream.readInt();
		status = stream.readByte();
	}

	@Override
	public byte[] encodeMsg() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream sss = new DataOutputStream(stream);
		sss.write(MAGIC);
		sss.writeShort(N);
		sss.writeInt(sequence);
		sss.writeByte(status);
		return stream.toByteArray();
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

}
