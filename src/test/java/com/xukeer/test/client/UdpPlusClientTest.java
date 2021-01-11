package com.xukeer.test.client;

import com.xukeer.udp.plus.client.UdpPlusClient;

/*
 * @Author xqw
 * @Description
 * @Date 16:15 2021/1/11
 **/
public class UdpPlusClientTest {
    public static void main(String[] args) {
        UdpPlusClient udpPlusClient = new UdpPlusClient("127.0.0.1",9000);

        udpPlusClient.setClientMessageHandler(msg-> {
                String msgStr = new String(msg);
                System.out.println(msgStr);
            }
        );

        udpPlusClient.sendMsg();
    }
}
