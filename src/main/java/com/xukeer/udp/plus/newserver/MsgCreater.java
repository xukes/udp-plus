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
