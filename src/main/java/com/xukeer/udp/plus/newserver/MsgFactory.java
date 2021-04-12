package com.xukeer.udp.plus.newserver;

import com.xukeer.udp.plus.utils.Utils;

import java.io.*;
import java.util.Iterator;

/**
 * 消息构建工厂
 * */
public class MsgFactory implements IMsgFactory {
    public static int MSG_LENGTH = 2048; // 每个包的字节数量
    public static int CLOUD_LENGTH = 2; // 每个簇的消息数量

    private int msgCrowdIndex = 0;
    private long crowdLength;
    private int sequence;
    private byte type;
    private String fileName;
    private BufferedInputStream bufferedInputStream;
    private boolean readHead;

    @Override
    public Iterator<MsgCrowd> create(byte[] bytes) {
        int legth = bytes.length;
        int msgLength = (int) Utils.roundUpperNumbers(legth,MSG_LENGTH);  // 消息体个数
        int crowdLength = (int) Utils.roundUpperNumbers(msgLength, CLOUD_LENGTH); // 消息簇的格式

        return null;
    }

    @Override
    public Iterator<MsgCrowd> create(File file) throws FileNotFoundException {
        type =  SimpleMsgBody.TYPE_FILE_CONTENT;
        fileName = file.getName();
        this.sequence = Utils.getIntRand();
        long msgLength = Utils.roundUpperNumbers(file.length(), MSG_LENGTH);  // 消息体个数
        this.crowdLength = (int) Utils.roundUpperNumbers(msgLength, CLOUD_LENGTH); // 消息簇的格式
        bufferedInputStream = new BufferedInputStream(new FileInputStream(file));

        Iterator<MsgCrowd> iterator = new Iterator<MsgCrowd>() {
            @Override
            public boolean hasNext() {
                return msgCrowdIndex < crowdLength;
            }

            @Override
            public MsgCrowd next() {
                if(type == SimpleMsgBody.TYPE_FILE_CONTENT && !readHead) {
                    readHead = true;
                    return new MsgCrowd(sequence, SimpleMsgBody.TYPE_FILE_HEAD, 1, 0, fileName.getBytes());
                }

                byte[] arr = new byte[CLOUD_LENGTH * MSG_LENGTH];
                try {
                    int i = bufferedInputStream.read(arr);
                    MsgCrowd msgCrowd;
                    if (i < MSG_LENGTH * MSG_LENGTH) {
                        byte[] newByteArr = new byte[i];
                        System.arraycopy(arr, 0, newByteArr, 0, i);
                        msgCrowd = new MsgCrowd(sequence, SimpleMsgBody.TYPE_FILE_CONTENT, crowdLength, msgCrowdIndex, newByteArr);
                    } else {
                        msgCrowd = new MsgCrowd(sequence, SimpleMsgBody.TYPE_FILE_CONTENT, crowdLength, msgCrowdIndex, arr);
                    }
                    msgCrowdIndex++;
                    return msgCrowd;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        return iterator;
    }
}
