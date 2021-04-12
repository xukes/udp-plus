package com.xukeer.test.server;

import com.xukeer.udp.plus.common.Utils;
import com.xukeer.udp.plus.newserver.MsgCrowd;
import com.xukeer.udp.plus.newserver.MsgFactory;
import com.xukeer.udp.plus.newserver.SimpleMsgBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

/*
 * @Author xqw
 * @Description
 * @Date 17:35 2021/4/12
 **/
public class MsgFactoryTest {

    public static void main(String[] argv) throws FileNotFoundException {
        MsgFactory msgFactory = new MsgFactory();
        Iterator<MsgCrowd> iterator =  msgFactory.create(new File("D:\\主题订阅流程.jmx"));
        while (iterator.hasNext()){
            MsgCrowd msgCrowd =  iterator.next();
            int length = msgCrowd.getLength();
            for(int i=0;i<length;i++){
                SimpleMsgBody simpleMsgBody = msgCrowd.getMsgBodies(i);
                System.out.print(Utils.byteToHexString(simpleMsgBody.getMsg()));
            }
            System.out.println();
        }
    }

}
