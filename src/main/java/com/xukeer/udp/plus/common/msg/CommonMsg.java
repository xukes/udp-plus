package com.xukeer.udp.plus.common.msg;

import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;


/**
 * 消息报文包括
 * +------------------------------------------------------------------------------|
 * +     魔数    | 消息序号   | 消息包长度   |  当前包序号  | 消息体长度   |  消息体      |
 * +------------------------------------------------------------------------------|
 * +    4byte   | 4byte     |   4byte    |   4byte     |  4byte     |  -    |
 * +------------------------------------------------------------------------------|
 * **/
@Data
public class CommonMsg extends Msg {
	public final static short TYPE = 1;
	public CommonMsg(){
		super.TYPE = TYPE;
	}
}

