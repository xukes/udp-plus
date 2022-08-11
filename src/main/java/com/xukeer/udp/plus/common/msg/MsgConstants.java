package com.xukeer.udp.plus.common.msg;

/*
 * @author xqw
 * @description
 * @date 9:34 2021/11/5
 **/
public class MsgConstants {
    public static final int COMMON_MSG = 1; // 普通消息

    public static final int RECEIVE_MSG = 2;  // 完成接收后的确认消息
    public static final int RECEIVE_MSG_REQ = 3; // 完成接收后确认消息

    public static final int RETRY_MSG = 4;   // 请求重发的消息

    public static final int OPTION_MSG = 5; // 试探性的消息

    public static final int RETRY_FAIL_MSG = 6; // 当请求发送方重新发送某个基本消息体时，发送方这个消息体已经不存在了，如遇这种情况，则做接收失败处理。
}
