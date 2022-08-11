package com.xukeer.udp.plus.common.msg;


import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
 * @author xqw
 * @description
 * @date 15:49 2021/11/11
 **/
public final class ReceiveRspMsg extends Msg {


    public ReceiveRspMsg() {
        super.TYPE=MsgConstants.RECEIVE_MSG_REQ;
    }

    public ReceiveRspMsg(long seq, int crowdIndex) {
        super.TYPE = MsgConstants.RECEIVE_MSG_REQ;
        super.setSequence(seq);
        super.setCrowdIndex(crowdIndex);
    }

    @Override
    public byte[] childEncodeMsg(ByteArrayOutputStream stream, DataOutputStream dataOutputStream) {
        return stream.toByteArray();
    }

    @Override
    public void childDecodeMsg(DataInputStream stream) {
       // crowdIndex = stream.readInt();
    }


    @Override
    public String toString() {
        return "ReceiveRspMsg{" +
                "crowdIndex=" + super.getCrowdIndex() +
                ", TYPE=" + TYPE +
                ", sequence=" + sequence +
                '}';
    }
}
