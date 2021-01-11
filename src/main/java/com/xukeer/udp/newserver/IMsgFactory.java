package com.xukeer.udp.newserver;

import java.io.File;
import java.util.Iterator;

public interface IMsgFactory {
    /**
     * 根据字节数组创建一个消息簇
     * */
    MsgCrowd create(byte[] bytes);

    /**
     * 根据文件创建一个消息簇迭代器
     *
     *
     * */
    Iterator<MsgCrowd> create(File file);
}
