package com.xukeer.udp.server;

import java.util.HashMap;
import java.util.Map;

import com.xukeer.udp.common.exception.MsgRepeatException;
import com.xukeer.udp.common.msg.CommonMsg;

/**
 * 处理已接收的消息
 * */
public class HandlerReceiveMsg {
	Map<Integer, CommonMsg[]> allMsg = new HashMap<Integer, CommonMsg[]>();
	static int indexx=0;
	public byte[] addMsg(CommonMsg msg) throws MsgRepeatException {
		CommonMsg[] list = allMsg.get(msg.getSequence());
		if(list != null) {
			CommonMsg baseMsgArr[] = allMsg.get(msg.getSequence());
			int index = msg.getPackageIndex();
			baseMsgArr[index] = msg;
		} else {
			CommonMsg baseMsgArr[] = new CommonMsg[msg.getTotalPackage()];
			baseMsgArr[msg.getPackageIndex()] = msg;
			allMsg.put(msg.getSequence(), baseMsgArr);
		}
		list = allMsg.get(msg.getSequence());
		if(list != null) {
			for(int i = 0; i < list.length; i++) {
				if(list[i] == null) {
					return null;
				}
			}
			int msgLength = list[0].getLength(); 
			int msgTotalLength = getMsgLength(list);
			byte[] msgByte = new byte[msgTotalLength];
			for(int i = 0; i < list.length; i++){
				System.arraycopy(list[i].getMsg(), 0, msgByte, msgLength*i, list[i].getLength());
			}
			indexx++;
			System.out.println("length:" + indexx);
			return msgByte;
		}
		return null;
	}
	
	public int getMsgLength(CommonMsg[] list) {
		int length = 0;
		int msgLength = list.length;
		length = list[0].getLength() * (list.length-1) + list[msgLength-1].getLength();
		return length;
	}
}
