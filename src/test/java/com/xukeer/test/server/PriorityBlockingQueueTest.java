package com.xukeer.test.server;

import com.xukeer.udp.plus.utils.ScheduleUtil;

import java.util.concurrent.PriorityBlockingQueue;


//@Slf4j
public class PriorityBlockingQueueTest {
    private static final PriorityBlockingQueue<SendMsgBody> priorityQueue = new PriorityBlockingQueue<>(20, (p1, p2) -> p2.getPriority()-p1.getPriority());

    public static void main(String[] argv) {
//        ScheduleUtil.threadPool().execute(()->{
//            while (true){
//                try {
//                    SendMsgBody sendMsgBody= priorityQueue.take();
//                    System.out.println(sendMsgBody.getName());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        ScheduleUtil.threadPool().execute(() -> {
//            for (int i = 0; i < 100; i++) {
//                SendMsgBody sendMsgBody = new SendMsgBody();
//                sendMsgBody.setPriority(i);
//                sendMsgBody.setName("xqw" + i);
//                priorityQueue.add(sendMsgBody);
//            }
//            System.out.println("finish");
//        });



//        Semaphore<Integer> semaphore = new Semaphore<>();
//        SingleMessageQueue linkedBlockingQueue = new SingleMessageQueue(semaphore);
//        new Thread(() -> new MsgCreater(linkedBlockingQueue).run()).start();
//        new Thread(() -> {
//            try {
//                Iterator<MsgCrowd> iterator = MsgFactory.create(new File("D:\\gradle-6.7.1-all.zip"));
//
//                while (iterator.hasNext()) {
//                    MsgCrowd msgCrowd = iterator.next();
//                    for (int j = 0; j < msgCrowd.getLength(); j++) {
//                        SimpleMsgBody simpleMsgBody = msgCrowd.getMsgBodies(j);
//                        linkedBlockingQueue.addMessage(simpleMsgBody);
//                    }
//                    System.out.println("--" + msgCrowd.getIndex());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
    }

    private static boolean gerRand() {
        return (int) (Math.random() * 100) > 98;
    }

    private static class SendMsgBody {
        private int priority; // 优先级, 优先级越高越先发送
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPriority() {
            return priority;
        }
        public void setPriority(int priority) {
            this.priority = priority;
        }
    }
}
