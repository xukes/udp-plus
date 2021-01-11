package com.xukeer.udp.server;

import com.xukeer.udp.msg.RspSuccess;


public interface RspCallback {
		//等到了应答消息
		public void onRsp(RspSuccess rsp);
		
		//等应答超时
		public void onTimeout(long time);
}
