package com.xukeer.udp.plus.common;

import com.xukeer.udp.plus.common.msg.*;
import com.xukeer.udp.plus.utils.Utils;

import java.io.*;
import java.util.Iterator;

/**
 * 消息构建工厂
 */
public class MsgFactory  {

    public static int MSG_LENGTH = 4068; // 每个消息基础的字节数量（最小的发送单元）
    private static int CLOUD_LENGTH = 120; // 每个簇的基础包数量

    private static int CLOUD_MSG_TOTAL = MSG_LENGTH * CLOUD_LENGTH;  // 每个簇的总消息大小

    public static Iterator<MsgCrowd> create(final byte[] bytes, long sequence) {
        int length = bytes.length;
        int crowdLength = (int) Utils.roundUpperNumbers(length, CLOUD_MSG_TOTAL); // 消息簇的格式
        boolean isExactly = length % CLOUD_MSG_TOTAL == 0;

        return new Iterator<MsgCrowd>() {
            int msgCrowdIndex = 0;
            @Override
            public boolean hasNext() {
                return msgCrowdIndex < crowdLength;
            }
            @Override
            public MsgCrowd next() {
                byte[] arr;
                // 判断是否是最后一个
                if (msgCrowdIndex == (crowdLength - 1) && !isExactly) {
                    arr = new byte[bytes.length % CLOUD_MSG_TOTAL];
                } else {
                    arr = new byte[CLOUD_MSG_TOTAL];
                }
                System.arraycopy(bytes, msgCrowdIndex * CLOUD_MSG_TOTAL, arr, 0, arr.length);
                MsgCrowd msgCrowd = new MsgCrowd(sequence,  crowdLength, msgCrowdIndex, arr);
                msgCrowdIndex ++;
                return msgCrowd;
            }
        };
    }

//    public static Iterator<MsgCrowd> create(File file) throws FileNotFoundException {
//        String fileName = file.getName();
//        int sequence = Utils.getIntRand();
//        long msgLength = Utils.roundUpperNumbers(file.length(), MSG_LENGTH);  // 消息体个数
//        int crowdLength = (int) Utils.roundUpperNumbers(msgLength, CLOUD_LENGTH); // 消息簇的格式
//        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
//
//        return new Iterator<MsgCrowd>() {
//            boolean readHead = false;
//            int msgCrowdIndex = 0;
//
//            @Override
//            public boolean hasNext() {
//                return msgCrowdIndex < crowdLength;
//            }
//
//            @SneakyThrows
//            @Override
//            public MsgCrowd next() {
//                if (!readHead) {
//                    readHead = true;
//                    return new MsgCrowd(sequence, SimpleMsgBody.TYPE_FILE_HEAD, 1, 0, fileName.getBytes());
//                }
//
//                byte[] arr = new byte[CLOUD_LENGTH * MSG_LENGTH];
//                int length = bufferedInputStream.read(arr);
//                MsgCrowd msgCrowd;
//                if (length < CLOUD_MSG_TOTAL) {
//                    byte[] newByteArr = new byte[length];
//                    System.arraycopy(arr, 0, newByteArr, 0, length);
//                    msgCrowd = new MsgCrowd(sequence, SimpleMsgBody.TYPE_FILE_CONTENT, crowdLength, msgCrowdIndex, newByteArr);
//                } else {
//                    msgCrowd = new MsgCrowd(sequence, SimpleMsgBody.TYPE_FILE_CONTENT, crowdLength, msgCrowdIndex, arr);
//                }
//                msgCrowdIndex++;
//                return msgCrowd;
//            }
//        };
//    }

    /**
     * 消息解码
     * */
    public static Msg createMsg(DataInputStream stream) {
        try {
            long l = stream.skip(Msg.MAGIC.length);
            if (l < Msg.MAGIC.length) {
                return null;
            }
            long sequence = stream.readLong();
            int msgType = stream.readShort();

            Msg msg;
            if (MsgConstants.COMMON_MSG == msgType) {
                msg = new CommonMsg();
            } else if (MsgConstants.RECEIVE_MSG == msgType) {
                msg = new RspMsg();
            } else if (MsgConstants.RECEIVE_MSG_REQ == msgType) {
                msg = new ReceiveRspMsg();
            } else if (MsgConstants.OPTION_MSG == msgType) {
                msg = new CrowdOptionMsg();
            } else if(MsgConstants.RETRY_FAIL_MSG == msgType){
                msg = new RetryFailMsg();
            } else {
                msg = new RetryMsg();
            }
            msg.setSequence(sequence);
            msg.decodeMsg(stream);
            stream.close();
            return msg;
        }catch (IOException e){
            return null;
        }
    }

}
