package com.xukeer.udp.plus.client;

import com.xukeer.udp.plus.sender.Sender;

import java.io.IOException;
import java.net.InetSocketAddress;

/*
 * @Author xqw
 * @Description 客户端入口类
 * @Date 11:42 2021/1/11
 **/
public class UdpPlusClient {
    private InetSocketAddress inetSocketAddress;
    public UdpPlusClient(String ip, Integer port) {
        inetSocketAddress = new InetSocketAddress(ip,port);
    }
    /**
     * 发送消息
     * */
    public void sendMsg(byte[] msgArr) throws IOException, InterruptedException {
        Sender.getInstance().sendMsg(msgArr,inetSocketAddress);
    }

    public void setClientMessageHandler(ClientMessageHandler clientMessageHandler) {

    }

}
