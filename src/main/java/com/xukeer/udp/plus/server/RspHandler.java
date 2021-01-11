package com.xukeer.udp.plus.server;

public class RspHandler extends Thread {
/*	private DatagramSocket socket;
	private HandlerReceiveMsg handlerReceive = null;
	private	ReceiveMsg receiveMsg = null;
	private Map<Integer, ReviveMsg> sendMsgList = new HashMap<Integer, ReviveMsg>();
	public RspHandler(DatagramSocket socket, ReceiveMsg receiveMsg) {
		this.socket = socket;
		this.receiveMsg = receiveMsg;
		handlerReceive = new HandlerReceiveMsg();
		new Thread(this).start();
	}*/
	
	
	/*@Override
	public void run() {
		while(true) {
			try {
				byte[] recvBuf = new byte[1024];
				DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
				socket.receive(recvPacket);
				final byte []receiveMsg = recvPacket.getData();
				final InetAddress address = recvPacket.getAddress();
				final int port = recvPacket.getPort();
				ScheduleUtil.threadPool().execute(new Runnable() {
					@Override
					public void run() {
						try {
							handleReceiveMsg(receiveMsg, address, port);
						} catch (DataOutException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (MsgRepeatException e) {
							e.printStackTrace();
						}
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/
	
	/**
	 * 处理接收到的消息 
	 * @throws DataOutException 
	 * @throws IOException 
	 * @throws MsgRepeatException 
	 * */
	/*public void handleReceiveMsg(byte [] bytes, InetAddress address, int port) throws DataOutException, IOException, MsgRepeatException {
		if(bytes.length > BaseMsg.MAGIC.length && (Utils.cutBytes(bytes, 0, BaseMsg.MAGIC.length).equals(BaseMsg.MAGIC))) { // 如果收到的消息长度大于
			BaseMsg msg = new BaseMsg();
			msg.decodeMsg(bytes);
			byte [] retMsg  = handlerReceive.addMsg(msg);
			if(retMsg != null) {
				receiveMsg.receiveMsg(address, port, retMsg);
			}
		}
		else {
			receiveMsg.receiveMsg(address, port, bytes);
		}
	}

	public Map<Integer, ReviveMsg> getSendMsgList() {
		return sendMsgList;
	}

	public void setSendMsgList(Map<Integer, ReviveMsg> sendMsgList) {
		this.sendMsgList = sendMsgList;
	}*/
	
	
}
