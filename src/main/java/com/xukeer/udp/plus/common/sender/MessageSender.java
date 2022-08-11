package com.xukeer.udp.plus.common.sender;

import com.xukeer.udp.plus.common.msg.MsgCrowd;
import com.xukeer.udp.plus.common.MsgFactory;
import com.xukeer.udp.plus.common.msg.CommonMsg;
import com.xukeer.udp.plus.common.msg.CrowdOptionMsg;
import com.xukeer.udp.plus.utils.SnowflakeUtil;
import com.xukeer.udp.plus.utils.Utils;

import java.net.InetSocketAddress;
import java.util.Iterator;

/*
 * @author xqw
 * @description
 * @date 18:19 2021/12/1
 **/
public class MessageSender {
    private Iterator<MsgCrowd> msgCrowdIterator;
    private InetSocketAddress targetAddr;

    private long sequence;
    private int currentCrowdIndex;
    private boolean canRunning = true;

    private ISimpleMsgSender iSimpleMsgSender;

    public MessageSender(byte[] bytes, InetSocketAddress targetAddr, ISimpleMsgSender iSimpleMsgSender) {
        this.targetAddr = targetAddr;
        this.sequence = SnowflakeUtil.nextId();
        msgCrowdIterator = MsgFactory.create(bytes, this.sequence);
        this.iSimpleMsgSender = iSimpleMsgSender;
    }

    public boolean handle() {
        if (!canRunning) {
            return false;
        }
        if(!msgCrowdIterator.hasNext()){
            iSimpleMsgSender.removeMsgCrowdMap(sequence);
            return true;
        }

        while (msgCrowdIterator.hasNext()) {
            if (!canRunning) {
                return false;
            }
            canRunning = false;
            MsgCrowd msgCrowd = msgCrowdIterator.next();
            CrowdOptionMsg crowdOptionMsg = new CrowdOptionMsg(msgCrowd.getSequence(), msgCrowd.getIndex(), msgCrowd.getTotalCrowd(), msgCrowd.getSimMagTotal());

            iSimpleMsgSender.create(crowdOptionMsg, targetAddr);

            iSimpleMsgSender.putMsgCrowdMap(sequence, msgCrowd);

            int length = msgCrowd.getMsgBodies().length;
            for (byte i = 0; i < length; i++) {
                CommonMsg simpleMsgBody = msgCrowd.getMsgBodies(i);
                iSimpleMsgSender.iSendCommonMsg(simpleMsgBody, targetAddr);
            }
        }

        return false;
    }

    public void onRsp(int crowIndex) {
        if (crowIndex >= currentCrowdIndex) {
            currentCrowdIndex = crowIndex + 1;
            this.canRunning = true;
        }


    }

    public long getSequence() {
        return sequence;
    }
}
