package com.xukeer.udp.plus.bus;

import com.xukeer.udp.plus.common.msg.CrowdOptionMsg;

import java.net.InetSocketAddress;

public interface ISendCrowdOptionMsg {
    void iSendCrowdOptionMsg(CrowdOptionMsg crowdOptionMsg, InetSocketAddress targetAddr);

    void iRspMsgFailed(long sequence, int crowdIndex);
}
