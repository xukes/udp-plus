package com.xukeer.udp.plus.bus;

import com.xukeer.udp.plus.common.MessageHandler;
import com.xukeer.udp.plus.common.msg.*;
import com.xukeer.udp.plus.common.sender.MsgSender;
import com.xukeer.udp.plus.utils.CacheSet;
import com.xukeer.udp.plus.utils.ScheduleUtil;
//import com.xukeer.udp.plus.utils.Utils;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;


/*
 * @author xqw
 * @description 主线， 所有接收到的消息都会丢到这里进行处理
 * @date 16:34 2021/11/19
 **/
//@Slf4j
public final class BusLine {
    private final static long POLLING_INTERVAL = 3;  // 主线轮询间隔（毫秒）

    // 主线列表，所有的消息都会在这个列表中进行处理
    private final static List<MsgSendMsgBody> RECEIVE_MSG_MAIN_LINE_LIST = new LinkedList<>();

    // 主线列表，所有的消息都会在这个列表中进行处理
    private final static List<WaiterSendMsgBody> WAIT_MAIN_LINE_LIST = new LinkedList<>();

    // 新的消息会先暂时存入这个队列，等待一次轮询之后，会将该队列里面的消息全部移到主线上
    private final List<MsgSendMsgBody> RECEIVE_MSG_SIMPLE_MSG_LIST = new LinkedList<>();

    // 新的消息会先暂时存入这个队列，等待一次轮询之后，会将该队列里面的消息全部移到主线上
    private final List<WaiterSendMsgBody> WAIT_MAIN_SIMPLE_MSG_LIST = new LinkedList<>();

    // 为了去重，把接收成功的消息序号存在一个缓冲器中，接收成功的消息序号会在缓存器中存储60秒，在这60秒内如果有收到同样序号的数据消息(请求确认消息则不会被丢弃，依然会进行回应)，会被直接丢弃。
    //
    private final CacheSet<Long> success = new CacheSet<>();

    private CrowdReceiverHandler crowdReceiverHandler;

    private final MessageHandler messageHandler;

    private final MsgSender msgSender;

//    private final static Map<String, Integer> MsgSendMsgBodyPriorityMap = new HashMap<>();
//    private final static int DEFAULT_PRIORITY = 1;

//    static {
//        MsgSendMsgBodyPriorityMap.put(CommonMsg.class.getSimpleName(), 1);
//        MsgSendMsgBodyPriorityMap.put(CrowdOptionMsg.class.getSimpleName(), 2);
//        MsgSendMsgBodyPriorityMap.put(RspMsg.class.getSimpleName(), 3);
//        MsgSendMsgBodyPriorityMap.put(ReceiveRspMsg.class.getSimpleName(), 4);
//        MsgSendMsgBodyPriorityMap.put(RetryMsg.class.getSimpleName(), 5);
//    }

    public BusLine(MsgSender msgSender, MessageHandler messageHandler) {
        this.msgSender = msgSender;
        this.messageHandler = messageHandler;

        // 一： 初始化接收处理器
        initCrowdReceive();

        // 二： 处理主线
        initMainHandle();
    }


    /**
     * 初始化接收处理器
     * */
    private void initCrowdReceive() {
        this.crowdReceiverHandler = new CrowdReceiverHandler(new ICrowdReceiver(){
            @Override
            public void simpleMsgReceiveComplete(long sequence, int crowdIndex, InetSocketAddress inetSocketAddress) {
                SendRspMsgSendMsgBody sendRspMsgSendMsgBody = new SendRspMsgSendMsgBody(200,20*1000, sequence, crowdIndex, msgSender, inetSocketAddress);
                addHandleSimpleMsg(sendRspMsgSendMsgBody);

                findWaiter(sequence, crowdIndex, CommonMsgWaiterSendMsgBody.class);
            }

            @Override
            public void msgReceiveComplete(byte[] msg, long sequence, InetSocketAddress inetSocketAddress) {
                synchronized (success) {
                    if (!success.contains(sequence)) {
                        success.add(sequence);
                        messageHandler.receiveMsg(msg, inetSocketAddress);
                    }
                }
            }
            @Override
            public void addReqSendMissMsg(long sequence, int crowdIndex, InetSocketAddress inetSocketAddress) {
                CommonMsgWaiterSendMsgBody commonMsgWaiterSendMsgBody = new CommonMsgWaiterSendMsgBody(200, 20 * 1000, sequence, crowdIndex, msgSender, crowdReceiverHandler, inetSocketAddress);
                addHandleSimpleMsg(commonMsgWaiterSendMsgBody);
            }
        });
    }

    /**
     * 初始化主线
     * */
    private void initMainHandle() {
        ScheduleUtil.timer().scheduleWithFixedDelay(() -> {
            try {
                // 将这一操作放在上面，可以稍微优化少部分时间
                RECEIVE_MSG_MAIN_LINE_LIST.removeIf(this::handle);
                WAIT_MAIN_LINE_LIST.removeIf(this::handle);

                if (!RECEIVE_MSG_SIMPLE_MSG_LIST.isEmpty()) {
                    synchronized (RECEIVE_MSG_SIMPLE_MSG_LIST) {
                        RECEIVE_MSG_MAIN_LINE_LIST.addAll(RECEIVE_MSG_SIMPLE_MSG_LIST);
                        RECEIVE_MSG_SIMPLE_MSG_LIST.clear();
                    }
                }

                if(!WAIT_MAIN_SIMPLE_MSG_LIST.isEmpty()) {
                    synchronized (WAIT_MAIN_SIMPLE_MSG_LIST){
                        WAIT_MAIN_LINE_LIST.addAll(WAIT_MAIN_SIMPLE_MSG_LIST);
                        WAIT_MAIN_SIMPLE_MSG_LIST.clear();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, POLLING_INTERVAL, TimeUnit.MILLISECONDS);

    }




    /**
     * 返回值： 是否处理完成，
     * true: 表示已经处理完成，将直接被主线删除。不再进入轮询。 一般有两种情况
     * 一：所有 {@link MsgSendMsgBody} 类型(接收到新消息) 的消息都会直接返回true
     * 二：所有 {@link WaiterSendMsgBody} 类型 的消息被调用了 onComplete 方法。
     * false: 表示还未处理完成, 在主线中轮询到可以执行的时间后被再次执行。目前只会用{@link WaiterSendMsgBody} 类型
     */
    private boolean handle(MsgSendMsgBody msgSendMsgBody) {
            Msg msg = msgSendMsgBody.getMsg();
            long sequence = msg.getSequence();
            findWaiter(sequence, msg.getCrowdIndex(), CrowdOptionMsgSendMsgBody.class);

            // 试探性的请求，为了防止整个簇丢失，每个簇都会发送一个试探性的请求
            if (msg instanceof CrowdOptionMsg) {
                CrowdOptionMsg  crowdOptionMsg = (CrowdOptionMsg) msg;
                if (!success.contains(sequence)) {
                    this.crowdReceiverHandler.addCommonMsg(crowdOptionMsg, msgSendMsgBody.getSocketAddress());
                }
            } else if (msg instanceof RspMsg) {
                msgSender.rspMsg(sequence, msg.getCrowdIndex());
                msgSender.sendReceiveRspMsg(sequence, msg.getCrowdIndex(), msgSendMsgBody.getSocketAddress());
            } else if (msg instanceof ReceiveRspMsg) {
                findWaiter(sequence, msg.getCrowdIndex(), SendRspMsgSendMsgBody.class);
            } else if (msg instanceof RetryMsg) {
                RetryMsg retryMsg = (RetryMsg) msg;
                msgSender.reSendMsg(retryMsg, msgSendMsgBody.getSocketAddress());
            } else if(msg instanceof RetryFailMsg ) {
                // 移除相关的操作
                findWaiter(sequence, msg.getCrowdIndex(), CommonMsgWaiterSendMsgBody.class);
                crowdReceiverHandler.remove(sequence);
                synchronized (success) {
                    if (!success.contains(sequence)) {
                        success.add(sequence);
                    }
                }
            }
            return true;
    }



    private boolean handle(WaiterSendMsgBody waiterSendMsgBody) {
        long currentTime = System.currentTimeMillis();
        if (waiterSendMsgBody.getIsComplete()) {
            return true;
        } else if (waiterSendMsgBody.isRunningNow()) {
            waiterSendMsgBody.setRunningNow();
            waiterSendMsgBody.timeOut();
        } else {
            if ((currentTime - waiterSendMsgBody.getStartTime()) > waiterSendMsgBody.waitTimeMiniSeconds) {
                waiterSendMsgBody.timeOut();
            }
        }

       if (currentTime - waiterSendMsgBody.getOriginStartTime() > waiterSendMsgBody.getMaxWaitTime()){
           if(waiterSendMsgBody instanceof CrowdOptionMsgSendMsgBody){
               CrowdOptionMsgSendMsgBody crowdOptionMsgSendMsgBody = (CrowdOptionMsgSendMsgBody) waiterSendMsgBody;
               crowdOptionMsgSendMsgBody.getISendCrowdOptionMsg().iRspMsgFailed(crowdOptionMsgSendMsgBody.getSequence(),crowdOptionMsgSendMsgBody.getCrowIndex());
           }
           return true;
       }
       return  false;
    }

    /**
     * 从主线中查找等待者
     * */
    private <T extends WaiterSendMsgBody> void findWaiter(long sequence, int crowIndex, Class<T> tClass) {
        // 先从主线中查找(比较大概率是可以找到的)
        synchronized (WAIT_MAIN_LINE_LIST) {
            for (WaiterSendMsgBody waiterSendMsgBody : WAIT_MAIN_LINE_LIST) {
                if (waiterSendMsgBody.getSequence() == sequence
                        && waiterSendMsgBody.getCrowIndex() == crowIndex
                        && tClass.isAssignableFrom(waiterSendMsgBody.getClass())) {
                    waiterSendMsgBody.onComplete();
                    return;
                }
            }
        }
        synchronized (WAIT_MAIN_SIMPLE_MSG_LIST) {
            // 如果在主线中找不到，则去等待队列中查找，几乎不会在这里
            for (WaiterSendMsgBody waiterSendMsgBody : WAIT_MAIN_SIMPLE_MSG_LIST) {
                if (waiterSendMsgBody.getSequence() == sequence
                        && waiterSendMsgBody.getCrowIndex() == crowIndex
                        && tClass.isAssignableFrom(waiterSendMsgBody.getClass())) {
                    waiterSendMsgBody.onComplete();
                }
            }
        }
    }


    public void addHandleSimpleMsg(Msg msg, InetSocketAddress socketAddress) {
        synchronized (RECEIVE_MSG_SIMPLE_MSG_LIST) {
            if (msg instanceof RetryMsg) {
                // 代码优化，
                // 如果队列里面还有上个申请重发的消息，则移除之前申请重发的消息以节省宽带
                RetryMsg newRetryMsg = (RetryMsg) msg;
                Iterator<MsgSendMsgBody> sendMsgBodyIterator = RECEIVE_MSG_SIMPLE_MSG_LIST.iterator();
                while (sendMsgBodyIterator.hasNext()) {
                    MsgSendMsgBody sendMsgBody = sendMsgBodyIterator.next();
                    Msg oldMsg = (sendMsgBody).getMsg();
                    if (oldMsg instanceof RetryMsg) {
                        RetryMsg oldRetryMsg = (RetryMsg) oldMsg;
                        if (newRetryMsg.getSequence() == oldRetryMsg.getSequence() && newRetryMsg.getCrowdIndex() == oldRetryMsg.getCrowdIndex()) {
                            sendMsgBodyIterator.remove();
                        }
                    }
                }
            }

            if (msg instanceof RspMsg) {
                // 代码优化，
                // 如果队列里面还有上个申请重发的消息，则移除之前申请重发的消息以节省宽带
                RspMsg newRspMsg = (RspMsg) msg;
                Iterator<MsgSendMsgBody> sendMsgBodyIterator = RECEIVE_MSG_SIMPLE_MSG_LIST.iterator();
                while (sendMsgBodyIterator.hasNext()) {
                    MsgSendMsgBody msgSendMsgBody = sendMsgBodyIterator.next();
                        Msg oldMsg = msgSendMsgBody.getMsg();
                        if (oldMsg instanceof RspMsg) {
                            RspMsg oldRspMsg = (RspMsg) oldMsg;
                            if (newRspMsg.getSequence() == oldRspMsg.getSequence() && newRspMsg.getCrowdIndex() == oldRspMsg.getCrowdIndex()) {
                                sendMsgBodyIterator.remove();
                            }
                        }
                }
            }

            MsgSendMsgBody msgSendMsgBody = new MsgSendMsgBody(msg, socketAddress);
           // msgSendMsgBody.setPriority(MsgSendMsgBodyPriorityMap.getOrDefault(msg.getClass().getSimpleName(), DEFAULT_PRIORITY));
            RECEIVE_MSG_SIMPLE_MSG_LIST.add(msgSendMsgBody);
        }
    }

    private void addHandleSimpleMsg(WaiterSendMsgBody waiterSendMsgBody) {
        synchronized (WAIT_MAIN_SIMPLE_MSG_LIST) {
            WAIT_MAIN_SIMPLE_MSG_LIST.add(waiterSendMsgBody);
        }
    }

    void addCrowdOptionMsgSendMsgBody(int timeOutMillSecond ,CrowdOptionMsg crowdOptionMsg, InetSocketAddress targetAddr, ISendCrowdOptionMsg iSendCrowdOptionMsg) {
        addHandleSimpleMsg(new CrowdOptionMsgSendMsgBody(200, timeOutMillSecond, crowdOptionMsg, targetAddr, iSendCrowdOptionMsg));
    }

    private static class SendMsgBody {
//        private int priority = 1; // 优先级, 优先级越高越先发送
//        int getPriority() {
//            return priority;
//        }
//        void setPriority(int priority) { this.priority = priority; }
    }

    private static class MsgSendMsgBody extends SendMsgBody {
        private final Msg msg;
        private final InetSocketAddress socketAddress;

        MsgSendMsgBody(Msg msg, InetSocketAddress socketAddress) {
            this.msg = msg;
            this.socketAddress = socketAddress;
        }
        public Msg getMsg() {
            return msg;
        }

        InetSocketAddress getSocketAddress() {
            return socketAddress;
        }
    }


    /**
     * 在时间轮询的总队列中的任务
     * timeOut方法，内部调用的方法，每次轮询的时候，满足timeOut的任务就会被执行，执行后会刷新开始时间，以便下一次执行
     * 执行的内容在timeOutHandle()方法中，由具体的子类实现。
     * 当达到超时时间后就会执行
     * 当onComplete被调用后，则说明该任务已经完成，在下一次轮询的时候，该任务就会被从主线中移除
     */
    static abstract class WaiterSendMsgBody extends SendMsgBody {
        private final long originStartTime;
        private  long starTime;              // 等待开始时间
        private final long waitTimeMiniSeconds;  // 等待的时长
        private final long maxWaitTime;          // 最大等待时长
        private boolean isComplete = false;     //是否已经完成
        private boolean isRunningNow;    // 是否立即执行

        private final long sequence;  // 消息序列号
        private final int crowdIndex;  // 消息集序号

        /**
         * @param isRunningNow 是否立即执行一次
         * @param waitTime     每次执行之后的等待时间
         */
        WaiterSendMsgBody(boolean isRunningNow, long waitTime, long maxWaitTime,long sequence, int crowdIndex) {
            this.sequence= sequence;
            this.crowdIndex = crowdIndex;
            this.waitTimeMiniSeconds = waitTime;
            this.isRunningNow = isRunningNow;
            this.maxWaitTime = maxWaitTime;
            originStartTime = System.currentTimeMillis();
            refreshTime();
        }

        /**
         * 刷新开始时间
         */
        void refreshTime() {
            this.starTime = System.currentTimeMillis();
        }
        /**
         * 时间到了应该执行的操作，
         * 具体的实现交给子类实现
         */
        abstract void timeOutHandle();
        /**
         * 获取等待者的序号
         * */
        long getSequence(){
            return this.sequence;
        }
        /**
         * 获取等待者的簇序号
         * */
        int getCrowIndex(){
            return this.crowdIndex;
        }
        /**
         * 任务到达执行的时间线
         */
        void timeOut() {
            timeOutHandle();
            refreshTime();
        }
        /**
         * 任务已完成，调用该方法后，在主线轮询的下一次就会被移除，并且不会再被执行。
         */
        void onComplete() {
            isComplete = true;
        }
        /**
         * 返回是否已经完成
         */
        boolean getIsComplete() {
            return isComplete;
        }
        /**
         * 返回是否是立即指定的任务
         * 如果为true,则任务在轮询的时候，被第一次轮询到的时候就会立即执行一次，然后更新开始时间，以便在下一次时间满足条件后再轮询中被执行
         */
        boolean isRunningNow() {
            return isRunningNow;
        }

        void setRunningNow() {
            isRunningNow = false;
        }

        long getStartTime() {
            return starTime;
        }

        long getMaxWaitTime() { return maxWaitTime; }

        long getOriginStartTime() { return originStartTime; }
    }

    /**
     * 请求发送放重试的操作，每隔一段时间就会执行一次
     * 每次执行的时候，会发送当前消息簇还缺失的消息，当一个消息簇接收完整后，该操作结束（）
     */
    private static final class CommonMsgWaiterSendMsgBody extends WaiterSendMsgBody {
        private final long sequence;
        private final int crowdIndex;
        private final InetSocketAddress socketAddress;
        private final MsgSender msgSender;
        private final CrowdReceiverHandler crowdReceiverHandler;

        private static final boolean IS_RIGHT_NOW_RUNNING = false;  // 是否要立即执行
        CommonMsgWaiterSendMsgBody(long waitTime, long maxWaitTime, long sequence, int crowdIndex, MsgSender msgSender, CrowdReceiverHandler crowdReceiverHandler, InetSocketAddress inetSocketAddress) {
            super(IS_RIGHT_NOW_RUNNING, waitTime, maxWaitTime, sequence, crowdIndex);
            this.sequence = sequence;
            this.crowdIndex = crowdIndex;
            this.socketAddress = inetSocketAddress;
            this.msgSender = msgSender;
            this.crowdReceiverHandler = crowdReceiverHandler;
        }
        @Override
        void timeOutHandle() {
            // 请求发送方再次发送该消息簇缺失的消息
            byte[] arr = crowdReceiverHandler.getMissSimpleIndex(sequence);
            if (arr != null && arr.length > 0) {
                msgSender.sendRetryMsg(sequence, crowdIndex, arr, socketAddress);
            }
        }
    }

    /**
     * 单个消息簇接收完成后的操作，每隔一段时间都会发送，直到收到ReceiveRspMsg消息为止
     * 注意，该操作需要与ReceiveRspMsg 配合使用，当收到ReceiveRspMsg消息后，该操作结束
     */
    private static final class SendRspMsgSendMsgBody extends WaiterSendMsgBody {
        private final long sequence;
        private final int crowdIndex;
        private final InetSocketAddress socketAddress;
        private final MsgSender msgSender;

        private static final boolean IS_RIGHT_NOW_RUNNING = true;  // 是否要立即执行
         SendRspMsgSendMsgBody(long waitTime,long maxWaitTime, long sequence, int crowdIndex, MsgSender msgSender, InetSocketAddress socketAddress) {
            super(IS_RIGHT_NOW_RUNNING, waitTime, maxWaitTime, sequence, crowdIndex);
            this.sequence = sequence;
            this.crowdIndex = crowdIndex;
            this.socketAddress = socketAddress;
            this.msgSender = msgSender;
        }

        @Override
        void timeOutHandle() {
            msgSender.sendRspMsg(sequence, crowdIndex, socketAddress);
        }
    }

    /**
     * 发送试探性请求的类，
     * 当接收到任何消息的时候应该被移除
     */
    private static final class CrowdOptionMsgSendMsgBody extends WaiterSendMsgBody {
        private final InetSocketAddress socketAddress;
        private final CrowdOptionMsg crowdOptionMsg;
        private final ISendCrowdOptionMsg iSendCrowdOptionMsg;

        // 发送试探性的消息不应该立即执行
        private static final boolean IS_RIGHT_NOW_RUNNING = false;  // 是否要立即执行
        /**
         * @param waitTime     每次执行之后的等待时间
         */
        CrowdOptionMsgSendMsgBody(long waitTime, long maxWaitTime, CrowdOptionMsg crowdOptionMsg, InetSocketAddress socketAddress, ISendCrowdOptionMsg iSendCrowdOptionMsg) {
            super(IS_RIGHT_NOW_RUNNING, waitTime, maxWaitTime, crowdOptionMsg.getSequence(),crowdOptionMsg.getCrowdIndex());
            this.socketAddress = socketAddress;
            this.crowdOptionMsg = crowdOptionMsg;
            this.iSendCrowdOptionMsg = iSendCrowdOptionMsg;
        }

        @Override
        void timeOutHandle() {
            iSendCrowdOptionMsg.iSendCrowdOptionMsg(crowdOptionMsg, socketAddress);
        }

        ISendCrowdOptionMsg getISendCrowdOptionMsg(){
            return iSendCrowdOptionMsg;
        }
    }
}
