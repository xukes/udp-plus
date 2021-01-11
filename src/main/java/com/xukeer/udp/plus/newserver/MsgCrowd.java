package com.xukeer.udp.plus.newserver;

import com.xukeer.udp.plus.utils.Utils;

/**
 * 消息簇
 */
public class MsgCrowd {
    private int index;
    private int sequence;
    private int length;
    private SimpleMsgBody[] msgBodies;

    /**
     * 标识当前簇是否传输完成
     */
    private int crowdIsComplete = 0;

    public MsgCrowd(int sequence, int totalCrowd, int index, byte[] bytes) {
         this(sequence, SimpleMsgBody.TYPE_BYTE_MESSAGE,totalCrowd,index,bytes);
    }

    public MsgCrowd(int sequence, byte msgType, int totalCrowd, int index, byte[] bytes) {
        this.index = index;
        this.sequence = sequence;
        int byteLength = bytes.length;
        this.length = (int) Utils.roundUpperNumbers(byteLength, MsgBody.MSG_LENGTH);
        msgBodies = new SimpleMsgBody[length];
        for (int i = 0; i < length; i++) {
            int startIndex = i * MsgBody.MSG_LENGTH;
            int endIndex = (i + 1) * MsgBody.MSG_LENGTH;
            int crowdBytesLength;
            if (endIndex > byteLength) {
                crowdBytesLength = byteLength - startIndex;
            } else {
                crowdBytesLength = MsgBody.MSG_LENGTH;
            }
            byte[] crowdBytes = new byte[crowdBytesLength];
            System.arraycopy(bytes, startIndex, crowdBytes, 0, crowdBytesLength);
            msgBodies[i] = new SimpleMsgBody(sequence, msgType, totalCrowd, length, index, i, crowdBytes);
        }
    }

    public MsgCrowd(int sequence, int simpleTotal, int index) {
        this.sequence = sequence;
        this.length = simpleTotal;
        this.index = index;
    }

    public void clear() {
        for (SimpleMsgBody simpleMsgBody : msgBodies) {
            simpleMsgBody.setMsg(new byte[]{});
        }
        msgBodies = new SimpleMsgBody[]{};
    }

    public int addSimpleMsgBody(SimpleMsgBody simpleMsgBody) {
        int index = simpleMsgBody.getMsgIndex();
        int total = simpleMsgBody.getTotalSimpleBody();
        if (msgBodies == null) {
            msgBodies = new SimpleMsgBody[total];
        }
        if (msgBodies[index] == null) {
            crowdIsComplete++;
        }
        msgBodies[index] = simpleMsgBody;
        if (crowdIsComplete == total) {
            return 2;
        }
        return 1;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public SimpleMsgBody getMsgBodies(int index) {
        return msgBodies[index];
    }

    public void setMsgBodies(SimpleMsgBody[] msgBodies) {
        this.msgBodies = msgBodies;
    }

    public boolean isComplete() {
        return crowdIsComplete == length;
    }
}
