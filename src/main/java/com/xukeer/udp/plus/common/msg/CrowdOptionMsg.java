package com.xukeer.udp.plus.common.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
 * @author xqw
 * @description
 * @date 17:16 2021/11/15
 **/
public class CrowdOptionMsg extends Msg {
    private int totalCrowdAmount;  // 该消息总共有多少个簇
    private byte totalSimpleAmount; // 当前消息当前簇共有多少个基础消息体

    public CrowdOptionMsg(){
        super.TYPE = MsgConstants.OPTION_MSG;
    }

    public CrowdOptionMsg(long sequence, int crowdIndex,  int totalCrowdAmount, byte totalSimpleAmount) {
        super.TYPE = MsgConstants.OPTION_MSG;
        super.setSequence(sequence);
        super.setCrowdIndex(crowdIndex);
        this.totalCrowdAmount = totalCrowdAmount;
        this.totalSimpleAmount = totalSimpleAmount;
    }

    @Override
    public byte[] childEncodeMsg(ByteArrayOutputStream stream, DataOutputStream dataOutputStream) {
      try {
          dataOutputStream.writeInt(totalCrowdAmount);
          dataOutputStream.writeByte(totalSimpleAmount);
      }catch (IOException ignored) {}
        return stream.toByteArray();
    }

    @Override
    public void childDecodeMsg(DataInputStream stream) throws IOException {
        this.totalCrowdAmount = stream.readInt();
        this.totalSimpleAmount = stream.readByte();
    }

    public int getTotalCrowdAmount() {
        return totalCrowdAmount;
    }

    public byte getTotalSimpleAmount() {
        return totalSimpleAmount;
    }

    public void setTotalCrowdAmount(int totalCrowdAmount) {
        this.totalCrowdAmount = totalCrowdAmount;
    }

    public void setTotalSimpleAmount(byte totalSimpleAmount) {
        this.totalSimpleAmount = totalSimpleAmount;
    }

    @Override
    public String toString() {
        return "CrowdOptionMsg{" +
                "totalCrowdAmount=" + totalCrowdAmount +
                ", totalSimpleAmount=" + totalSimpleAmount +
                ", TYPE=" + TYPE +
                ", sequence=" + sequence +
                ", crowdIndex=" + crowdIndex +
                '}';
    }
}
