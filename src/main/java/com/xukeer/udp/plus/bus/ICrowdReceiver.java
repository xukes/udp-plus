package com.xukeer.udp.plus.bus;

import java.net.InetSocketAddress;

/*
 * @author xqw
 * @description
 * @date 18:15 2021/12/3
 **/
 interface ICrowdReceiver {
    /**
     * 单个消息集接收完成
     * */
    void simpleMsgReceiveComplete(long sequence,int crowdIndex, InetSocketAddress inetSocketAddress);

    /**
     * 整个消息接收完成
     * */
    void msgReceiveComplete(byte [] msg, long sequence, InetSocketAddress inetSocketAddress);

    /**
     * 往主线中添加缺失的消息的请求
     * */
    void addReqSendMissMsg(long sequence,int crowdIndex, InetSocketAddress inetSocketAddress);
}
