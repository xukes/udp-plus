package com.xukeer.udp.plus.newserver;

import com.xukeer.udp.plus.utils.Utils;
import lombok.Data;

import java.io.*;
import java.util.Iterator;

/**
 * 消息构建工厂
 */
@Data
public class MsgFactory implements IMsgFactory {
    public static int MSG_LENGTH = 1024; // 每个消息基础的字节数量（最小的发送单元）
    public static int CLOUD_LENGTH = 3; // 每个簇的基础包数量

    public static int CLOUD_MSG_TOTAL = MSG_LENGTH * CLOUD_LENGTH;  // 每个簇的总消息大小

    private int msgCrowdIndex = 0;
    private long crowdLength;
    private int sequence;
    private byte type;
    private String fileName;
    private BufferedInputStream bufferedInputStream;
    private boolean readHead;

    @Override
    public Iterator<MsgCrowd> create(final byte[] bytes) {
        int length = bytes.length;

        int crowdLength = (int) Utils.roundUpperNumbers(length, CLOUD_MSG_TOTAL); // 消息簇的格式
        boolean isExactly = bytes.length % CLOUD_MSG_TOTAL == 0;
        this.sequence = Utils.getIntRand();

        return new Iterator<MsgCrowd>() {
            @Override
            public boolean hasNext() {
                return msgCrowdIndex < crowdLength;
            }
            @Override
            public MsgCrowd next() {
                int msgLength = (int) Utils.roundUpperNumbers(length, MSG_LENGTH);  // 消息体个数
                byte[] arr;
                // 判断是否是最后一个
                if (msgCrowdIndex == (crowdLength - 1) && !isExactly) {
                    arr = new byte[bytes.length % CLOUD_MSG_TOTAL];
                } else {
                    arr = new byte[CLOUD_MSG_TOTAL];
                }
                System.arraycopy(bytes, msgCrowdIndex * CLOUD_MSG_TOTAL, arr, 0, arr.length);
                MsgCrowd msgCrowd = new MsgCrowd(sequence, SimpleMsgBody.TYPE_FILE_CONTENT, crowdLength, msgCrowdIndex, arr);
                msgCrowdIndex++;
                return msgCrowd;
            }
        };
    }

    @Override
    public Iterator<MsgCrowd> create(File file) throws FileNotFoundException {
        type = SimpleMsgBody.TYPE_FILE_CONTENT;
        fileName = file.getName();
        this.sequence = Utils.getIntRand();
        long msgLength = Utils.roundUpperNumbers(file.length(), MSG_LENGTH);  // 消息体个数
        this.crowdLength = (int) Utils.roundUpperNumbers(msgLength, CLOUD_LENGTH); // 消息簇的格式
        bufferedInputStream = new BufferedInputStream(new FileInputStream(file));

        return new Iterator<MsgCrowd>() {
            @Override
            public boolean hasNext() {
                return msgCrowdIndex < crowdLength;
            }

            @Override
            public MsgCrowd next() {
                if (type == SimpleMsgBody.TYPE_FILE_CONTENT && !readHead) {
                    readHead = true;
                    return new MsgCrowd(sequence, SimpleMsgBody.TYPE_FILE_HEAD, 1, 0, fileName.getBytes());
                }

                byte[] arr = new byte[CLOUD_LENGTH * MSG_LENGTH];
                try {
                    int length = bufferedInputStream.read(arr);
                    MsgCrowd msgCrowd;
                    if (length < CLOUD_MSG_TOTAL) {
                        byte[] newByteArr = new byte[length];
                        System.arraycopy(arr, 0, newByteArr, 0, length);
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
    }
}
