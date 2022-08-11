package com.xukeer.udp.plus.common.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
 * @author xqw
 * @description
 * @date 14:49 2021/12/1
 **/
public final class RetryFailMsg extends Msg {

    public RetryFailMsg() {
        super.TYPE = MsgConstants.RETRY_FAIL_MSG;
    }

    public RetryFailMsg(long seq) {
        super.TYPE = MsgConstants.RETRY_FAIL_MSG;
        super.setSequence(seq);
    }

    @Override
    public byte[] childEncodeMsg(ByteArrayOutputStream stream, DataOutputStream dataOutputStream) {
        return stream.toByteArray();
    }

    @Override
    public  void childDecodeMsg(DataInputStream stream){
    }

    @Override
    public String toString() {
        return "RetryFailMsg{" +
                "crowdIndex=" + super.getCrowdIndex() +
                ", TYPE=" + TYPE +
                ", sequence=" + sequence +
                '}';
    }
}
