package com.xukeer.udp.msg;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DecodeMsg extends CommonMsg {
	
	public void decodeMsg(ByteArrayInputStream stream) throws IOException {
		stream.read();  // 读取魔术
	}
}
