package com.xukeer.udp.plus.common.sender;

import com.xukeer.udp.plus.bus.ISendCrowdOptionMsg;
import com.xukeer.udp.plus.bus.MsgAddMachine;
import com.xukeer.udp.plus.client.ResponseI;
import com.xukeer.udp.plus.common.msg.MsgCrowd;
import com.xukeer.udp.plus.common.msg.*;
import com.xukeer.udp.plus.utils.ScheduleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/*
 * @author xqw
 * @description
 * @date 18:16 2021/11/4
 **/
public class MsgSender {
    private static final Logger log = LoggerFactory.getLogger(MsgSender.class);
    private static final int COMMON_MSG_PRIORITY = 1; // 普通消息的优先级
    private static final int RSP_MSG_PRIORITY = 2; // 完成接受确认的优先级
    private static final int RETRY_MSG_PRIORITY = 3; // 申请重发的消息的优先级
    private static final int SENDING_INTERVAL = 5;  // 发送间隔（主线会每隔这个单位的时间轮询一次发送队列，并将当前簇的消息发送出去）

    // 优先级阻塞队列，消息优先级字段值越大，越在前面被执行
    private final PriorityBlockingQueue<SendMsgBody> priorityQueue = new PriorityBlockingQueue<>(20, (p1, p2) -> p2.getPriority() - p1.getPriority());

    private final boolean isRunning = true;

    private final Map<Long, MsgCrowd> msgCrowdMap = new HashMap<>();

    private MsgAddMachine msgAddMachine;
    private final int timeOutMillSecond;

    /**
     * 响应消息
     * */
    private final Map<Long, ResponseI> responseIHashMap = new HashMap<>();

    public MsgSender(DatagramSocket datagramSocket,int timeOutMillSecond) {
        this.timeOutMillSecond = timeOutMillSecond;
        init(datagramSocket);

    }

    private static class SendMsgBody {
        private DatagramPacket datagramPacket; // 数据包
        private int priority; // 优先级, 优先级越高越先发送
        private IGiveBackByteArr iGiveBackByteArr;

        public SendMsgBody(IGiveBackByteArr iGiveBackByteArr){
            if(iGiveBackByteArr!=null) {
                this.iGiveBackByteArr = iGiveBackByteArr;
            }
        }

        DatagramPacket getDatagramPacket() {
            return datagramPacket;
        }
        void setDatagramPacket(DatagramPacket datagramPacket) {
            this.datagramPacket = datagramPacket;
        }
        int getPriority() {
            return priority;
        }
        void setPriority(int priority) {
            this.priority = priority;
        }

        public IGiveBackByteArr getiGiveBackByteArr() {
            return iGiveBackByteArr;
        }
    }

    private void init(DatagramSocket datagramSocket) {
        ScheduleUtil.threadPool().execute(() -> {
            try {
                while (isRunning) {
                    SendMsgBody sendMsgBody = priorityQueue.take();
                    datagramSocket.send(sendMsgBody.getDatagramPacket());
                    IGiveBackByteArr iGiveBackByteArr = sendMsgBody.getiGiveBackByteArr();
                    if(iGiveBackByteArr!=null){
                        iGiveBackByteArr.giveBackByteArr();
                    }
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });

        ScheduleUtil.timer().scheduleWithFixedDelay(() -> {
                try {
                    synchronized (sendMainLinkedList) {
                        sendMainLinkedList.removeIf(MessageSender::handle);
                    }
                }catch (Exception ignored){}
        }, 0, SENDING_INTERVAL, TimeUnit.MILLISECONDS);

    }

    private void send(int priority, byte[] msg, InetSocketAddress targetAddr) {
        send(priority, msg, targetAddr, null );
    }

    private void send(int priority, byte[] msg, InetSocketAddress targetAddr, IGiveBackByteArr iGiveBackByteArr) {
        DatagramPacket packet = new DatagramPacket(msg, msg.length, targetAddr);
        SendMsgBody sendMsgBody = new SendMsgBody(iGiveBackByteArr);
        sendMsgBody.setDatagramPacket(packet);
        sendMsgBody.setPriority(priority);

        priorityQueue.add(sendMsgBody);
    }

    private final LinkedList<MessageSender> sendMainLinkedList = new LinkedList<>();

    /**
     * 单线程添加
     * */
    public void sendCommonMsgSingleThread(byte[] bytes, InetSocketAddress targetAddr, ResponseI response,Long seq)  {
        MessageSender messageSender = new MessageSender (bytes,seq, targetAddr, new ISimpleMsgSender(){

            public void iSendCommonMsg(CommonMsg simpleMsgBody, InetSocketAddress targetAddr){
                sendSimpleMsg(COMMON_MSG_PRIORITY, simpleMsgBody, targetAddr);
            }

            @Override
            public void create(CrowdOptionMsg crowdOptionMsg, InetSocketAddress targetAddr) {
                if (msgAddMachine != null) {
                    msgAddMachine.addMsg(timeOutMillSecond, crowdOptionMsg, targetAddr, new ISendCrowdOptionMsg(){
                        @Override
                        public void iSendCrowdOptionMsg(CrowdOptionMsg crowdOptionMsg, InetSocketAddress targetAddr) {
                            sendCrowdOptionMsg(crowdOptionMsg, targetAddr);
                        }
                        @Override
                        public void iRspMsgFailed(long sequence, int crowdIndex) {
                            rspMsgFailed(sequence,crowdIndex );
                        }
                        @Override
                        public void isResponse(byte[] arr){
                            response.receiveResponse(arr);
                        }
                    });
                }
            }

            @Override
            public void putMsgCrowdMap(long sequence, MsgCrowd msgCrowd) {
                msgCrowdMap.put(sequence, msgCrowd);
            }

            @Override
            public void removeMsgCrowdMap(long sequence) {
                msgCrowdMap.remove(sequence);
            }
        });

        responseIHashMap.put(messageSender.getSequence(), response);

        synchronized (sendMainLinkedList) {
            sendMainLinkedList.add(messageSender);
        }
    }

    private void sendSimpleMsg(int priority, CommonMsg simpleMsgBody, InetSocketAddress targetAddr) {
        if(simpleMsgBody!=null) {
            send(priority, simpleMsgBody.encodeMsg(), targetAddr, simpleMsgBody::giveBackByteArr);
//            log.debug(simpleMsgBody.toString());
        }
    }

    private interface IGiveBackByteArr {
        void giveBackByteArr();
    }

    private void sendRetryFailMsg(RetryFailMsg retryFailMsg, InetSocketAddress targetAddr) {
        send(RSP_MSG_PRIORITY, retryFailMsg.encodeMsg(), targetAddr);
//        log.warn(retryFailMsg.toString());
    }

    /**
     * 发送收到响应的消息
     */
    public void sendRspMsg(long sequence, int crowdIndex, InetSocketAddress targetAddr) {
        RspMsg rspMsg = new RspMsg();
        rspMsg.setSequence(sequence);
        rspMsg.setCrowdIndex(crowdIndex);
        send(RSP_MSG_PRIORITY, rspMsg.encodeMsg(), targetAddr);
//        log.info(rspMsg.toString());
    }

    /**
     * 发送请求重发消息
     */
    public void sendRetryMsg(long sequence, int crowdIndex, byte[] simpleMsgIndex, InetSocketAddress targetAddr) {
        if (simpleMsgIndex.length == 0) {
            return;
        }
        RetryMsg retryMsg = new RetryMsg(sequence, crowdIndex, simpleMsgIndex);
        send(RETRY_MSG_PRIORITY, retryMsg.encodeMsg(), targetAddr);
//        log.debug(retryMsg.toString());
    }

    public void sendReceiveRspMsg(long sequence, int crowdIndex, InetSocketAddress targetAddr) {
        ReceiveRspMsg receiveRspMsg = new ReceiveRspMsg(sequence, crowdIndex);
        send(RETRY_MSG_PRIORITY, receiveRspMsg.encodeMsg(), targetAddr);
//        log.info(receiveRspMsg.toString());
    }

    private void sendCrowdOptionMsg(CrowdOptionMsg crowdOptionMsg, InetSocketAddress targetAddr) {
        send(RSP_MSG_PRIORITY, crowdOptionMsg.encodeMsg(), targetAddr);
//        log.debug(crowdOptionMsg.toString());
    }

    /**
     * 重发消息
     */
    public void reSendMsg(RetryMsg retryMsg, InetSocketAddress socketAddress) {
        long req = retryMsg.getSequence();
        byte[] retryMsgSimpleMsgIndexArr = retryMsg.getSimpleMsgIndex();
        MsgCrowd msgCrowd = msgCrowdMap.get(req);
        if (msgCrowd != null) {
            for (byte index : retryMsgSimpleMsgIndexArr) {
                CommonMsg simpleMsgBody = msgCrowd.getMsgBodies(index);
                sendSimpleMsg(RETRY_MSG_PRIORITY, simpleMsgBody, socketAddress);
            }
        } else {
            RetryFailMsg retryFailMsg = new RetryFailMsg(req);
            sendRetryFailMsg( retryFailMsg, socketAddress);
        }
    }

    public void rspMsg(long seq,int crowdIndex) {
        synchronized (sendMainLinkedList) {
            for (MessageSender messageSender : sendMainLinkedList) {
                if (messageSender.getSequence() == seq) {
                    messageSender.onRsp(crowdIndex);
                }
            }
        }
    }

    private void rspMsgFailed(long seq, int crowdIndex) {
        // TODO 抛出发送超时异常, 该异常以目前技术不足以处理，等待日后技术有所提升，再来尝试处理吧
        // TODO 主要难点在于，发送是即使的立即会执行完的，但是异常是在别的线程中产生的，会滞后，目前所掌握的技术很难处理
        synchronized (sendMainLinkedList) {
            log.error("send failed time out seq={},currentCrowdIndex={}", seq, crowdIndex);
            sendMainLinkedList.removeIf(messageSender -> messageSender.getSequence() == seq);
        }
    }

    public void setMsgAddMachine (MsgAddMachine msgAddMachine) {
            this.msgAddMachine = msgAddMachine;
    }
}
