package com.xukeer.udp.plus.newserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileCreator {
    private long totalCrowd;
    private FileOutputStream fileOutputStream;
    private int index = 0;
    private Map<String, MsgCrowd> msgCrowdMap = new HashMap<>();

    public FileCreator(SimpleMsgBody simpleMsgBody) throws IOException {
        if(simpleMsgBody.getType()== SimpleMsgBody.TYPE_FILE_HEAD) {
            String path = "D:\\back\\";
            String name = new String(simpleMsgBody.getMsg());
            File file = new File(path + name);
            if(file.exists()){
                file = new File(path+ name+1);
            }
            file.createNewFile();
            this.fileOutputStream = new FileOutputStream(file);
        }
    }

    public synchronized void createFile() throws IOException {
        MsgCrowd msgCrowd = msgCrowdMap.get(Integer.toString(index));
        if (msgCrowd!= null && msgCrowd.isComplete()) {
            inii(msgCrowdMap.get(Integer.toString(index)));
            index++;
        }
    }

    private void inii(MsgCrowd msgCrowd) throws IOException {
        for (int j = 0; j < msgCrowd.getLength(); j++) {
            SimpleMsgBody simpleMsgBody = msgCrowd.getMsgBodies(j);
            fileOutputStream.write(simpleMsgBody.getMsg());
        }
        fileOutputStream.flush();
        msgCrowd.clear();
        Integer key = msgCrowd.getIndex();
        msgCrowdMap.remove(key.toString());
        if (msgCrowd.getIndex() == (totalCrowd - 1)) {
            fileOutputStream.close();
            msgCrowdMap.clear();
            System.out.println("end");
        }
    }

    public int addSimpleMsg(SimpleMsgBody simpleMsgBody) throws IOException {
        if(simpleMsgBody.getType() == SimpleMsgBody.TYPE_FILE_HEAD){
            return 1;
        }
        totalCrowd = simpleMsgBody.getTotalCrow();
        Integer crowdIndex = simpleMsgBody.getCrowdIndex();
        MsgCrowd msgCrowd = msgCrowdMap.get(crowdIndex.toString());
        if (msgCrowd == null) {
            msgCrowd = new MsgCrowd(simpleMsgBody.getSequence(), simpleMsgBody.getTotalSimpleBody(), simpleMsgBody.getCrowdIndex());
        }
        msgCrowdMap.put(crowdIndex.toString(), msgCrowd);
        if (msgCrowd.addSimpleMsgBody(simpleMsgBody) == 2) {
            createFile();
        }
        return 0;
    }
}
