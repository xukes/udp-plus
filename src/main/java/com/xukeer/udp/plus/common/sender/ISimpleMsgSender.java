package com.xukeer.udp.plus.common.sender;

import com.xukeer.udp.plus.common.msg.MsgCrowd;
import com.xukeer.udp.plus.common.msg.CommonMsg;
import com.xukeer.udp.plus.common.msg.CrowdOptionMsg;

import java.net.InetSocketAddress;

/*
 * @author xqw
 * @description
 * @date 18:29 2021/12/1
 **/
public interface ISimpleMsgSender {
    void iSendCommonMsg(CommonMsg simpleMsgBody, InetSocketAddress targetAddr);

    void create(CrowdOptionMsg crowdOptionMsg, InetSocketAddress targetAddr);

    void putMsgCrowdMap(long sequence, MsgCrowd msgCrowd);

    void removeMsgCrowdMap(long sequence);
}
