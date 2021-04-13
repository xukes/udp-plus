package com.xukeer.test.server;

import com.xukeer.udp.plus.common.msg.CommonMsg;
import com.xukeer.udp.plus.newserver.IMsgFactory;
import com.xukeer.udp.plus.newserver.MsgCrowd;
import com.xukeer.udp.plus.newserver.MsgFactory;
import com.xukeer.udp.plus.newserver.SimpleMsgBody;
import com.xukeer.udp.plus.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

/*
 * @Author xqw
 * @Description
 * @Date 17:35 2021/4/12
 **/
public class MsgFactoryTest {

    public static void main(String[] argv) throws IOException {
        IMsgFactory msgFactory = new MsgFactory();
        int msgLength =20480;
        byte[] message = new byte[msgLength];//{1,23,67,45,32,12,34,56,54,33,22,56,67,78,90,76,5,2,2,1,2,3,4,5,6,7,8,12,12,3,44,56,44,32,12,34,56,78,90,89,45};
        for(int i=0;i<msgLength;i++){
            message[i]= (byte) i;
        }
      // String s = Utils.byteArrToHexString(message);
       Iterator<MsgCrowd> iterator = msgFactory.create(message);
      //  Iterator<MsgCrowd> iterator =  msgFactory.create(new File("D:\\主题订阅流程.jmx"));
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            MsgCrowd msgCrowd =  iterator.next();
            int length = msgCrowd.getLength();
            for (int i = 0; i < length; i++) {
                SimpleMsgBody simpleMsgBody = msgCrowd.getMsgBodies(i);

                CommonMsg commonMsg = new CommonMsg();
                commonMsg.setSequence(simpleMsgBody.getSequence());
                commonMsg.setTotalCrowdAmount((int)simpleMsgBody.getTotalCrow());
                commonMsg.setMsg(simpleMsgBody.getMsg());
                commonMsg.setTotalSimpleAmount(simpleMsgBody.getTotalSimpleBody());
                commonMsg.setCrowdIndex(simpleMsgBody.getCrowdIndex());
                commonMsg.setSimpleIndex(simpleMsgBody.getMsgIndex());
                commonMsg.setMsgLength(simpleMsgBody.getMsg().length);

                byte[] bytes  = commonMsg.encodeMsg();

                System.out.println(Utils.byteArrToHexString(bytes));
            }
            System.out.println();
        }
    }
}
