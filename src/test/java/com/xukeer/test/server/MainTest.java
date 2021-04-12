package com.xukeer.test.server;

import com.xukeer.udp.plus.newserver.*;

import java.io.*;
import java.util.Iterator;

public class MainTest {

    public static void main(String[] argv) {
        Semaphore<Integer> semaphore = new Semaphore<>();
        SingleMessageQueue linkedBlockingQueue = new SingleMessageQueue(semaphore);

        new Thread(() -> new MsgCreater(linkedBlockingQueue).run()).start();

        new Thread(() -> {
            try {
                MsgBody msgBody = new MsgBody(new File("D:\\gradle-6.7.1-all.zip"));
                Iterator<MsgCrowd> iterator = msgBody.getIterarot();
                while (iterator.hasNext()) {
                    MsgCrowd msgCrowd = iterator.next();
                    for (int j = 0; j < msgCrowd.getLength(); j++) {
                        SimpleMsgBody simpleMsgBody = msgCrowd.getMsgBodies(j);
                        Integer i = linkedBlockingQueue.addMessage(simpleMsgBody);
                        System.out.println("--" + i);
                    }
                }
                System.out.println("end");
                msgBody.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
