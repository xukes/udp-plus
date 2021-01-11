package com.xukeer.udp.plus.newserver;

import com.xukeer.udp.plus.utils.Utils;

import java.io.*;
import java.util.Iterator;

public class MsgBody {
    public static int MSG_LENGTH = 2048; // 每个包的字节数量
    public static int CLOUD_LENGTH = 100; // 每个簇的消息数量

    private int crowdLength;
    private int sequence;
    private MsgCrowd[] msgCrowds;

    private byte type;

    private int msgBodyComplete = 0;

    public MsgBody(byte[] bytes) {
        long length = bytes.length;
        this.sequence = Utils.getIntRand();
        long msgLength = Utils.roundUpperNumbers(length, MSG_LENGTH);  // 消息体个数
        this.crowdLength = (int) Utils.roundUpperNumbers(msgLength, CLOUD_LENGTH); // 消息簇的格式
        msgCrowds = new MsgCrowd[crowdLength];
        for (int i = 0; i < crowdLength; i++) {
            int startIndex = i * MSG_LENGTH * CLOUD_LENGTH;
            int endIndex = (i + 1) * MSG_LENGTH * CLOUD_LENGTH;
            int crowdBytesLength;
            if (endIndex > length) {
                crowdBytesLength = (int) (length - startIndex);
            } else {
                crowdBytesLength = MSG_LENGTH * CLOUD_LENGTH;
            }
            byte[] crowdBytes = new byte[crowdBytesLength];
            System.arraycopy(bytes, startIndex, crowdBytes, 0, crowdBytesLength);
            msgCrowds[i] = new MsgCrowd(sequence, crowdLength, i, crowdBytes);
        }
    }

    private BufferedInputStream bufferedInputStream;
    String fileName;
    int msgCrowdIndex = 0;
    boolean readHead = false;

    public MsgBody(File file) throws FileNotFoundException {
        type =  SimpleMsgBody.TYPE_FILE_CONTENT;
        fileName = file.getName();
        this.sequence = Utils.getIntRand();
        long msgLength = Utils.roundUpperNumbers(file.length(), MSG_LENGTH);  // 消息体个数
        this.crowdLength = (int) Utils.roundUpperNumbers(msgLength, CLOUD_LENGTH); // 消息簇的格式
        bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
    }

    public Iterator<MsgCrowd> getIterarot() {
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

    public void close() throws IOException {
        bufferedInputStream.close();
    }

    public MsgBody(int sequence, int crowdLength) {
        this.sequence = sequence;
        this.crowdLength = crowdLength;
        msgCrowds = new MsgCrowd[crowdLength];
    }

    public int addSimpleMsgBody(SimpleMsgBody simpleMsgBody) {
        int crowdIndex = simpleMsgBody.getCrowdIndex();
        int msgIndex = simpleMsgBody.getMsgIndex();
        int simpleTotal = simpleMsgBody.getTotalSimpleBody();
        MsgCrowd msgCrowd = getMsgCrowds(crowdIndex);
        if (msgCrowd == null) {
            msgCrowd = new MsgCrowd(simpleMsgBody.getSequence(), simpleTotal, msgIndex);
        }
        msgCrowds[crowdIndex] = msgCrowd;
        if (msgCrowd.addSimpleMsgBody(simpleMsgBody) == 2) {
            msgBodyComplete++;
        }
        if (msgBodyComplete == simpleMsgBody.getTotalCrow()) {
            return 2;
        }
        return 1;
    }

    public int getCrowdLength() {
        return crowdLength;
    }

    public int getSequence() {
        return sequence;
    }

    public MsgCrowd getMsgCrowds(int index) {
        return msgCrowds[index];
    }
}
