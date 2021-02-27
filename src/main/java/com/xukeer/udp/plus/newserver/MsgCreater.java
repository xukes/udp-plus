package com.xukeer.udp.plus.newserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MsgCreater implements Runnable {
    private SingleMessageQueue linkedBlockingQueue;
    Map<Integer, MsgBody> map = new HashMap<>();
    Map<String, MsgCrowd> msgCrowdMap = new HashMap<>();
    Map<Integer, FileCreator> fileCreatorMap = new HashMap<>();
    private File file;
    private FileOutputStream fileOutputStream;
    /**
     *
     */
    public MsgCreater(SingleMessageQueue linkedBlockingQueue) {
        this.linkedBlockingQueue = linkedBlockingQueue;
    }



    @Override
    public void run() {
        while (true) {
            try {
                    SimpleMsgBody simpleMsgBody = linkedBlockingQueue.read();

                FileCreator fileCreator = fileCreatorMap.get(simpleMsgBody.getSequence());
                if(fileCreator == null) {
                     fileCreator = new FileCreator(simpleMsgBody);
                     fileCreatorMap.put(simpleMsgBody.getSequence(), fileCreator);
                }
                fileCreator.addSimpleMsg(simpleMsgBody);
                linkedBlockingQueue.getSemphore().setValue(simpleMsgBody.getCrowdIndex());

//               int crowdIndex = simpleMsgBody.getCrowdIndex();
//               String key = crowdIndex+"-"+simpleMsgBody.getSequence();
//                MsgCrowd msgCrowd = msgCrowdMap.get(key);
//                if (msgCrowd == null) {
//                    msgCrowd = new MsgCrowd(simpleMsgBody.getSequence(), simpleMsgBody.getTotalSimpleBody(), simpleMsgBody.getCrowdIndex());
//                }
//                if (msgCrowd.addSimpleMsgBody(simpleMsgBody) == 2) {
//                    fileCreator.createFile(msgCrowd);
//                }
//                msgCrowdMap.put(key, msgCrowd);

                // simpleMsgBody=null;
//                MsgBody ms = map.get(simpleMsgBody.getSequence());
//                if (ms == null) {
//                    ms = new MsgBody(simpleMsgBody.getSequence(), simpleMsgBody.getTotalCrow());
//                    map.put(simpleMsgBody.getSequence(), ms);
//                }
//                if (2 == ms.addSimpleMsgBody(simpleMsgBody)) {
//                    try {
//                        createFile(ms);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("complete");
//                }
                //      System.out.println(simpleMsgBody.getCrowdIndex() + "-" + simpleMsgBody.getMsgIndex() + "-" + simpleMsgBody.getSequence());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized File createFile(MsgCrowd msgCrowd) throws IOException {
        for (int j = 0; j < msgCrowd.getLength(); j++) {
            SimpleMsgBody simpleMsgBody = msgCrowd.getMsgBodies(j);
            fileOutputStream.write(simpleMsgBody.getMsg());
            fileOutputStream.flush();
        }
        if(msgCrowd.getIndex() == 10800) {
            fileOutputStream.close();
        }
        return file;
    }
}
