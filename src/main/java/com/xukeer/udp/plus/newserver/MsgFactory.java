package com.xukeer.udp.plus.newserver;

import com.xukeer.udp.plus.utils.Utils;

import java.io.File;
import java.util.Iterator;

/**
 * 消息构建工厂
 * */
public class MsgFactory implements IMsgFactory {
    public static int MSG_LENGTH = 2048; // 每个包的字节数量
    public static int CLOUD_LENGTH = 100; // 每个簇的消息数量


    @Override
    public MsgCrowd create(byte[] bytes) {
        int legth = bytes.length;
        int msgLength = (int) Utils.roundUpperNumbers(legth,MSG_LENGTH);  // 消息体个数
        int crowdLength = (int) Utils.roundUpperNumbers(msgLength, CLOUD_LENGTH); // 消息簇的格式

        return null;
    }

    @Override
    public Iterator<MsgCrowd> create(File file) {
        return null;
    }
}
