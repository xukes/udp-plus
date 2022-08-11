package com.xukeer.udp.plus.common.msg;

import com.xukeer.udp.plus.utils.ByteArrFactory;
import com.xukeer.udp.plus.utils.Utils;

import static com.xukeer.udp.plus.common.MsgFactory.MSG_LENGTH;

/**
 * 消息簇
 * 一个完整的消息会包含一个或多个消息簇
 * 一个消息簇中会包含多个最小消息单元
 */
//@Slf4j
public final class MsgCrowd {
    private int index;
    private long sequence;
    private byte simMagTotal;  // msgBodies length
    private int totalCrowd;
    private CommonMsg[] msgBodies;

    public MsgCrowd(long sequence, int totalCrowd, int crowIndex, byte[] bytes) {
        this.index = crowIndex;
        this.sequence = sequence;
        this.totalCrowd = totalCrowd;
        int byteLength = bytes.length;
        this.simMagTotal = (byte) Utils.roundUpperNumbers(byteLength, MSG_LENGTH);
        msgBodies = new CommonMsg[simMagTotal];
        for (byte simpleMsgIndex = 0; simpleMsgIndex < simMagTotal; simpleMsgIndex++) {
            int startIndex = simpleMsgIndex * MSG_LENGTH;
            int endIndex = (simpleMsgIndex + 1) * MSG_LENGTH;
            int commonMsgLength;
            if (endIndex > byteLength) {
                commonMsgLength = byteLength - startIndex;
            } else {
                commonMsgLength = MSG_LENGTH;
            }
            byte[] commonMsgBytes = new byte[commonMsgLength];


//            byte[] commonMsgBytes = ByteArrFactory.applyByteArr(commonMsgLength);
            System.arraycopy(bytes, startIndex, commonMsgBytes, 0, commonMsgLength);
            msgBodies[simpleMsgIndex] = new CommonMsg(sequence, totalCrowd, simMagTotal, crowIndex, simpleMsgIndex, commonMsgLength, commonMsgBytes);
        }
    }

    public CommonMsg getMsgBodies(byte index) {
        CommonMsg commonMsg = null;
        try {
            commonMsg = msgBodies[index];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commonMsg;
    }

    public int getIndex() {
        return index;
    }

    public byte getSimMagTotal() {
        return simMagTotal;
    }

    public CommonMsg[] getMsgBodies() {
        return msgBodies;
    }


    public long getSequence() {
        return this.sequence;
    }

    public int getTotalCrowd() {
        return totalCrowd;
    }
}
