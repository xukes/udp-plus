package com.xukeer.udp.plus.server;

//import com.xukeer.udp.plus.common.ByteMsgCreator;
import com.xukeer.udp.plus.common.MessageHandler;
import com.xukeer.udp.plus.common.MsgReceiveHandle;
import com.xukeer.udp.plus.common.sender.MsgSender;
import com.xukeer.udp.plus.utils.ScheduleUtil;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/*
 * @Author xqw
 * @Description 服务端入口类
 * udp服务类
 * @Date 11:47 2021/1/11
 **/
public class UpdPlusServer {
    private MsgSender msgSender;
    private static final int DEFAULT_SEND_OUT_TIME_MILL_SECOND = 20 * 1000;  // 发送消息的超时时间
    /**
     * 初始化服务器类
     * @param port 监听的端口
     * @param serverMessageHandler 事件处理器
     */
    public UpdPlusServer(int port, MessageHandler serverMessageHandler) {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(new InetSocketAddress(port));
            this.msgSender = new MsgSender(datagramSocket, DEFAULT_SEND_OUT_TIME_MILL_SECOND);
            ScheduleUtil.threadPool().execute(new MsgReceiveHandle(datagramSocket,  serverMessageHandler, msgSender));
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    /**
     * 初始化服务器类
     * @param port 监听的端口
     * @param timeOutMillSecond 超时时间 毫秒
     * @param serverMessageHandler 事件处理器
     */
    public UpdPlusServer(int port, int timeOutMillSecond, MessageHandler serverMessageHandler) {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(new InetSocketAddress(port));
            this.msgSender = new MsgSender(datagramSocket, timeOutMillSecond);
            ScheduleUtil.threadPool().execute(new MsgReceiveHandle(datagramSocket,  serverMessageHandler, msgSender));
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(byte[] msgArr, InetSocketAddress inetSocketAddress) {
        this.msgSender.sendCommonMsgSingleThread(msgArr, inetSocketAddress);
    }
}
