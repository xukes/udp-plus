package com.xukeer.udp.plus.bus;

/*
 * @author xqw
 * @description
 * @date 15:45 2021/11/26
 **/

import com.xukeer.udp.plus.common.msg.CrowdOptionMsg;

import java.net.InetSocketAddress;

//@Slf4j
public class MsgAddMachine {
    private BusLine busLine;

    public MsgAddMachine(BusLine busLine){
        this.busLine = busLine;
    }

    public void addMsg(int timeOutMillSecond, CrowdOptionMsg crowdOptionMsg, InetSocketAddress targetAddr, ISendCrowdOptionMsg iSendCrowdOptionMsg ) {
        busLine.addCrowdOptionMsgSendMsgBody(timeOutMillSecond, crowdOptionMsg, targetAddr, iSendCrowdOptionMsg);
    }
}
