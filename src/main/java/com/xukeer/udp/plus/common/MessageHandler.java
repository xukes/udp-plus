package com.xukeer.udp.plus.common;

import java.net.InetSocketAddress;

/*
 * @Author xqw
 * @Description
 * @Date 11:53 2021/1/11
 **/
public interface MessageHandler {
    /**
     * 接收到消息
     * */
    void receiveMsg(byte [] msg, InetSocketAddress inetSocketAddress,Long sequence);

}
