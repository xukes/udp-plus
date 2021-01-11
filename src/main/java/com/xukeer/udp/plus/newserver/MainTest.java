package com.xukeer.udp.plus.newserver;

import java.io.*;
import java.util.Iterator;

public class MainTest {

    public static void main(String[] argv) throws IOException {
        com.xukeer.udp.plus.newserver.Semaphore<Integer> semaphore = new Semaphore<>();
        SingleMessageQueue linkedBlockingQueue = new SingleMessageQueue(semaphore);

        new Thread(() -> new MsgCreater(linkedBlockingQueue).run()).start();
        //  MsgBody msgBody = new MsgBody(createByteArr());
        //MsgBody msgBody = new MsgBody(new File("D:\\tools\\database.rar"));

//        while (true){
//            try {
//                Thread.sleep(10000);

             //   File[] files = new File(argv[0]).listFiles();
               // for (File file : files) {
                    // LinkedBlockingQueue<SimpleMsgBody> linkedBlockingQueue = new LinkedBlockingQueue<>();
                    new Thread(() -> {
                        try {
                            MsgBody msgBody = new MsgBody(new File("D:\\linuxSystem\\2019-06-20-raspbian-buster-ssh.7z"));
                            Iterator<MsgCrowd> iterator = msgBody.getIterarot();
                            while (iterator.hasNext()) {
                                MsgCrowd msgCrowd = iterator.next();
                                for (int j = 0; j < msgCrowd.getLength(); j++) {
                                    SimpleMsgBody simpleMsgBody = msgCrowd.getMsgBodies(j);
                                  //  synchronized (linkedBlockingQueue) {
                                    Integer i=  linkedBlockingQueue.addMessage(simpleMsgBody);
                                     System.out.println("--"+i);
                                       // future.get();
                                    }
                                }
                           // }
                            msgBody.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
             //   }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
       // }


//        for (int i = 0; i < msgBody.getCrowdLength(); i++) {
//            MsgCrowd msgCrowd = msgBody.getMsgCrowds(i);
//            for (int j = 0; j < msgCrowd.getLength(); j++) {
//                SimpleMsgBody simpleMsgBody = msgCrowd.getMsgBodies(j);
//                synchronized (linkedBlockingQueue) {
//                    linkedBlockingQueue.offer(simpleMsgBody);
//                }
//            }
//        }
    }

    public static byte[] createByteArr() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            File file = new File("D:\\tools\\database.rar");
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] ss = new byte[1024];
            int i = 0;
            while ((i = fileInputStream.read(ss)) != -1) {
                byteArrayOutputStream.write(ss, 0, i);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {

        }
        return new byte[192003000];

    }
}
