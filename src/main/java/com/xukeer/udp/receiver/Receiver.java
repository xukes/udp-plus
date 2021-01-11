/**
 * 
 */
package com.xukeer.udp.receiver;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.xukeer.udp.common.exception.MsgRepeatException;
import com.xukeer.udp.common.msg.CommonMsg;
import com.xukeer.udp.common.msg.Msg;
import com.xukeer.udp.common.msg.MsgFactory;
import com.xukeer.udp.common.msg.RetryMsg;
import com.xukeer.udp.common.msg.RspMsg;
import com.xukeer.udp.common.msg.RspSuccess;
import com.xukeer.udp.sender.DeepSend;
import com.xukeer.udp.sender.Sender;
import com.xukeer.udp.server.ServerPool;
import com.xukeer.udp.utils.ScheduleUtil;

/**
 * @author xukeer
 *
 */
public class Receiver {
	
	private IReceiver revice;
	
	LinkedBlockingQueue<RspMsg> queue = new LinkedBlockingQueue<>(Integer.MAX_VALUE);
	static Map<Integer, CommonMsg[]> allMsg = new ConcurrentHashMap<Integer, CommonMsg[]>();
	static Map<Integer, Long> lastReceive = new ConcurrentHashMap<>();
	
	
	static int count = 0;
	public Receiver(IReceiver reviceve) {
		this.revice = reviceve;
		final DatagramSocket socket = ServerPool.getSocket();
		
		
		ScheduleUtil.threadPool().execute(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						RspMsg commMsg = queue.take();
						DataInputStream strem = new DataInputStream(new ByteArrayInputStream(commMsg.getBytes()));
						strem.skip(4);  // 读取魔术
						short N = strem.readShort();
						Msg msg = MsgFactory.getMsg(N);
						msg.decodeMsg1(strem,commMsg.getSourceAdddress());
						if(msg instanceof CommonMsg){
							handleReceiveMsg((CommonMsg)msg);
						} else if(msg instanceof RetryMsg) {
							RetryMsg retrymsg = (RetryMsg)msg;
							Sender.getInstance().retryMsg(retrymsg.getSequence(), retrymsg.getMsgIndex());
						} else if(msg instanceof RspSuccess) {
							RspSuccess retrymsg = (RspSuccess)msg;
							Sender.getInstance().onReceive(retrymsg.getSequence(), retrymsg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		ScheduleUtil.threadPool().execute( new Runnable() {
			@Override
			public void run() {
				try {
					while(true) {
						byte[] receiveData = new byte[8026];
						DatagramPacket dataPacket = new DatagramPacket(receiveData, receiveData.length);
						socket.receive(dataPacket);
						RspMsg ms = new RspMsg(receiveData, (InetSocketAddress) dataPacket.getSocketAddress());
						queue.add(ms);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		 checkRepeat();
	}
	/**
	 * 查找需要重发的消息
	 * */
	public void checkRepeat() {
		ScheduleUtil.timer().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					if (!allMsg.isEmpty()) {
						for (Map.Entry<Integer, CommonMsg[]> msg : allMsg.entrySet()) {
							CommonMsg[] arr = msg.getValue();
							int kk = getQueru(arr);
							if (arr.length < 0) {
								continue;
							}
							int sequenec = arr[kk].getSequence();
							if (System.currentTimeMillis() - lastReceive.get(sequenec) < 50) {
								continue;
							}
							sendRetrymsg(arr, kk);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1, 50, TimeUnit.MILLISECONDS);
	}
	
	public void sendRetrymsg(CommonMsg[] arr, int kk) {
		int length = arr.length;
		int sequenec = arr[kk].getSequence();
		List<Integer> arr1 = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			if (arr[i] == null) {
				arr1.add(i);
			}
		}
		
		int size = arr1.size();
		for(int i=0;i<=size/100;i++){
			boolean isLast =  (i == (size/100))?true:false;
			Integer[] ss;
			if(isLast){
				 ss = new Integer[size%100];
				arr1.subList(i*100, i*100+size%100).toArray(ss);
			} else {
				 ss = new Integer[100];
				arr1.subList(i*100, (i+1)*100).toArray(ss);
			}
			RetryMsg retry = new RetryMsg(sequenec, ss);
			try {
				DeepSend.getInstance().send(retry.encodeMsg(), arr[kk].getSourceAddr());
				lastReceive.put(sequenec, System.currentTimeMillis());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取第一个不为空的消息
	 * */
	public int getQueru(CommonMsg[] indexs){
		for(int i=0; i < indexs.length; i++) {
			if(indexs[i] != null){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 处理接收到的消息片段
	 * */
	public void handleReceiveMsg(CommonMsg msg) throws MsgRepeatException, IOException {
		int sequence = msg.getSequence();
		lastReceive.put(sequence, System.currentTimeMillis());
		CommonMsg[] list = allMsg.get(sequence);
		if(list != null) {
			CommonMsg baseMsgArr[] = list;
			int index = msg.getPackageIndex();
			baseMsgArr[index] = msg;
		} else {
			list = new CommonMsg[msg.getTotalPackage()];
			list[msg.getPackageIndex()] = msg;
			allMsg.put(sequence, list);
		}
		for(int i = 0; i < list.length; i++) {
			if(list[i] == null) {
				return;
			}
		}
		int msgLength = list[0].getLength(); 
		int msgTotalLength = getMsgLength(list);
		byte[] msgByte = new byte[msgTotalLength];
		for(int i = 0; i < list.length; i++){
			System.arraycopy(list[i].getMsg(), 0, msgByte, msgLength*i, list[i].getLength());
		}
		allMsg.remove(msg.getSequence());
		lastReceive.remove(msg.getSequence());
		DeepSend.getInstance().send(new RspSuccess(sequence,Msg.STATUS_SUCCESS).encodeMsg(), msg.getSourceAddr());
		if(revice != null) {
			revice.receiveMsg(msg.getSourceAddr(), msgByte);
		}
	
	}
	
	/**
	 * 获取消息的长度
	 * */
	public int getMsgLength(CommonMsg[] list) {
		int length = 0;
		int msgLength = list.length;
		length = list[0].getLength() * (list.length-1) + list[msgLength-1].getLength();
		return length;
	}
	
}
