package com.xukeer.udp.plus.common;

import com.xukeer.udp.plus.bus.BusLine;
import com.xukeer.udp.plus.bus.MsgAddMachine;
import com.xukeer.udp.plus.common.msg.Msg;
import com.xukeer.udp.plus.common.sender.MsgSender;
import com.xukeer.udp.plus.utils.ScheduleUtil;
import com.xukeer.udp.plus.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/*
 * @author xqw
 * @description
 * @date 18:54 2021/11/4
 **/
public class MsgReceiveHandle implements Runnable {
    private final static int MAX_RECEIVE_BUFFER_LENGTH = 8096; // 消息最大缓冲区

    private final boolean isRunning = true;

    private final DatagramSocket datagramSocket;

    private final BusLine busLine;

    public MsgReceiveHandle(DatagramSocket datagramSocket, MessageHandler messageHandler, MsgSender msgSender) {
        this.datagramSocket = datagramSocket;
        this.busLine = new BusLine(msgSender, messageHandler);
        msgSender.setMsgAddMachine(new MsgAddMachine(busLine));
    }

    @Override
    public void run() {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[MAX_RECEIVE_BUFFER_LENGTH], MAX_RECEIVE_BUFFER_LENGTH);
        ScheduleUtil.threadPool().execute(() -> {
            while (isRunning) {
                try {
                    datagramSocket.receive(datagramPacket);
                    byte[] bytes = Utils.cutBytes(datagramPacket.getData(), 0, datagramPacket.getLength());
                    Msg msg = MsgFactory.createMsg(new DataInputStream(new ByteArrayInputStream(bytes)));
                    busLine.addHandleSimpleMsg(msg, (InetSocketAddress) datagramPacket.getSocketAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
