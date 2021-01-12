package com.xukeer.test.client;

import com.xukeer.udp.plus.client.UdpPlusClient;

import java.io.IOException;

/*
 * @Author xqw
 * @Description
 * @Date 16:15 2021/1/11
 **/
public class UdpPlusClientTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        UdpPlusClient udpPlusClient = new UdpPlusClient("127.0.0.1",9000);

        udpPlusClient.setClientMessageHandler(msg-> {
                String msgStr = new String(msg);
                System.out.println(msgStr);
            }
        );
        byte[] arr = new byte[100];

        udpPlusClient.sendMsg(arr);
    }
}
