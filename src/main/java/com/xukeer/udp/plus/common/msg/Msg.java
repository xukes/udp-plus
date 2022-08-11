package com.xukeer.udp.plus.common.msg;


import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Msg {
    public final static byte[] MAGIC = {(byte) 0xEF, (byte) 0xEF, (byte) 0xEF, (byte) 0xEF};  // 使用本协议的魔术，放在于消息头


    int TYPE;    // 消息类型

    long sequence;   // 序号，用来唯一标识一个消息，为随机数

    int crowdIndex;  // 簇序号

    /**
     * 编码消息，给子类的方法
     */
    public abstract byte[] childEncodeMsg(ByteArrayOutputStream stream, DataOutputStream dataOutputStream);

    /**
     * 解码消息
     */
    public abstract void childDecodeMsg(DataInputStream stream) throws IOException;

    public void giveBackByteArr() {

    }

    /**
     * 编码消息，给子类的方法
     */
    public byte[] encodeMsg() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(stream);
        byte[] result = null;
        try {
            dataOutputStream.write(MAGIC);
            dataOutputStream.writeLong(getSequence());
            dataOutputStream.writeShort(TYPE);
            dataOutputStream.writeInt(crowdIndex);
            dataOutputStream.close();
            result = childEncodeMsg(stream, dataOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解码消息
     */
    public void decodeMsg(DataInputStream stream) throws IOException {
        this.crowdIndex = stream.readInt();
        this.childDecodeMsg(stream);
    }

    public long getSequence() {
        return sequence;
    }

    public int getCrowdIndex() {
        return crowdIndex;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public void setCrowdIndex(int crowdIndex) {
        this.crowdIndex = crowdIndex;
    }
}
