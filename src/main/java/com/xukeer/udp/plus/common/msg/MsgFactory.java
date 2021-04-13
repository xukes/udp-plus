package com.xukeer.udp.plus.common.msg;

import java.util.HashMap;
import java.util.Map;

public class MsgFactory {
	public final static Map<Short, Class<?>> msgMap =new HashMap<>();
	static{
		msgMap.put(CommonMsg.TYPE, CommonMsg.class);
		msgMap.put(RetryMsg.N, RetryMsg.class);
		msgMap.put(RspSuccess.N, RspSuccess.class);
	}
	
	public static Msg getMsg(Short N) throws InstantiationException, IllegalAccessException {
		Class<?> cls = msgMap.get(N);
		if(cls!= null) {
			Msg msg = (Msg) cls.newInstance();
			return msg;
		}
		return null;
	}
}
