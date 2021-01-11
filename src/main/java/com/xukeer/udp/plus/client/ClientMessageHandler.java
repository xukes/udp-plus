package com.xukeer.udp.plus.client;

/*
 * @Author xqw
 * @Description
 * @Date 11:53 2021/1/11
 **/
public interface ClientMessageHandler {
    /**
     * 接收到消息
     * */
    void receiveMsg(byte [] msg);
}
