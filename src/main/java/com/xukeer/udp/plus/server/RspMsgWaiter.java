/**
 * 
 */
package com.xukeer.udp.plus.server;

import com.xukeer.udp.plus.common.msg.RspSuccess;

/**
 * @author xukeer
 *
 */
public class RspMsgWaiter implements RspCallback {

	
	
public static final int DEFAULT_WAIT_TIMEOUT =5500;
	
	private RspSuccess rspMsg;
	
	private long timeout;
	
	public RspMsgWaiter() {
		this.timeout = DEFAULT_WAIT_TIMEOUT;
	}
	
	public RspMsgWaiter(long timeout) {
		this.timeout = timeout;
	}
	
	@Override
	public void onRsp(RspSuccess rsp) {
		synchronized(this) {
			rspMsg = rsp;
			notifyAll();
		}
	}

	@Override
	public void onTimeout(long time) {
		synchronized(this) {
			notifyAll();
		}
	}
	
	public RspSuccess waitRsp() throws InterruptedException {
		//有可能waitRsp还没来得及调用，应答就来了(onRsp被调用)
		//所以这里先判断rspMsg是否为null再wait，rspMsg不为null说明应答早就来了，直接返回rspMsg
		synchronized(this) {
			if(rspMsg != null) {
				return rspMsg;
			}
			wait(timeout);
			return rspMsg;
		}
	}

}
