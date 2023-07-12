package com.xukeer.udp.plus.client;

import com.xukeer.udp.plus.common.MessageHandler;
import com.xukeer.udp.plus.common.MsgReceiveHandle;
import com.xukeer.udp.plus.common.sender.MsgSender;
import com.xukeer.udp.plus.utils.ScheduleUtil;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/*
 * @Author xqw
 * @Description 客户端入口类
 * @Date 11:42 2021/1/11
 **/
public class UdpPlusClient {

    private InetSocketAddress inetSocketAddress;  // 服务器地址

    private MsgSender msgSender;   // 消息发送器

    private static final int DEFAULT_SEND_OUT_TIME_MILL_SECOND = 20*1000;  // 发送消息的超时时间

    /**
     * 初始化本地UDP客户端
     *
     * @param serverIp       服务器IP
     * @param listenPort     本地监听端口
     * @param serverPort     服务器端口
     * @param messageHandler 消息处理类
     */
    public UdpPlusClient(String serverIp, Integer serverPort, Integer listenPort, MessageHandler messageHandler) {
        try {
            this.inetSocketAddress = new InetSocketAddress(serverIp, serverPort);
            DatagramSocket datagramSocket = new DatagramSocket( new InetSocketAddress(listenPort));
            datagramSocket.setReceiveBufferSize(10*1024*1024);// 两兆
            datagramSocket.setTrafficClass(4);
            msgSender = new MsgSender(datagramSocket, DEFAULT_SEND_OUT_TIME_MILL_SECOND);
            ScheduleUtil.threadPool().execute(new MsgReceiveHandle(datagramSocket, messageHandler, msgSender));
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化本地UDP客户端
     *
     * @param serverIp       服务器IP
     * @param listenPort     本地监听端口
     * @param serverPort     服务器端口
     * @param timeOutSecond 超时时间 秒
     * @param messageHandler 消息处理类
     */
    public UdpPlusClient(String serverIp, Integer serverPort, Integer listenPort, int timeOutSecond, MessageHandler messageHandler) {
        try {
            this.inetSocketAddress = new InetSocketAddress(serverIp, serverPort);
            DatagramSocket datagramSocket = new DatagramSocket( new InetSocketAddress(listenPort));
            msgSender = new MsgSender(datagramSocket, timeOutSecond);
            ScheduleUtil.threadPool().execute(new MsgReceiveHandle(datagramSocket, messageHandler, msgSender));
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     */
    public void sendMsg(byte[] msgArr) {
                msgSender.sendCommonMsgSingleThread(msgArr, inetSocketAddress,null,null);
    }

    public void sendMsg(byte[] msgArr,ResponseI response) {
        msgSender.sendCommonMsgSingleThread(msgArr, inetSocketAddress, response,null);
    }
}
