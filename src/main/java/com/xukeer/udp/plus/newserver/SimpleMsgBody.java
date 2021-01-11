package com.xukeer.udp.plus.newserver;

/**
 * 消息体
 * */
public class SimpleMsgBody {
    public final static byte TYPE_BYTE_MESSAGE = 1;
    public final static byte TYPE_FILE_HEAD = 2;
    public final static byte TYPE_FILE_CONTENT = 3;

    private byte type;  // 消息类型，1、字节消息体，2、文件头 3、文件体
    private int sequence;     // 消息序号
    private int totalCrow;    // 总共用多少个簇
    private int totalSimpleBody;  // 当前消息所在的簇有多少个SimpleMsgBody
    private int crowdIndex;   // 该消息属于第几个簇
    private int msgIndex;     // 该消息属于簇的第几个包
    private byte[] msg;       // 消息体

    public SimpleMsgBody(int sequence, byte type,  int totalCrow, int totalSimpleBody, int crowdIndex, int index, byte[] bytes) {
        this.type = type;
        this.sequence = sequence;
        this.totalCrow = totalCrow;
        this.totalSimpleBody = totalSimpleBody;
        this.crowdIndex = crowdIndex;
        this.msgIndex = index;
        this.msg = bytes;
    }

    public int getTotalCrow() {
        return totalCrow;
    }

    public int getTotalSimpleBody() {
        return totalSimpleBody;
    }

    public int getCrowdIndex() {
        return crowdIndex;
    }

    public int getMsgIndex() {
        return msgIndex;
    }

    public int getSequence() {
        return sequence;
    }

    public byte[] getMsg() {
        return msg;
    }

    public void setMsg(byte[] msg) {
        this.msg = msg;
    }

    public byte getType() {
        return type;
    }
}
