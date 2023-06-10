package com.xukeer.udp.plus.bus;

import com.xukeer.udp.plus.common.msg.CommonMsg;
import com.xukeer.udp.plus.common.msg.CrowdOptionMsg;
import com.xukeer.udp.plus.utils.ByteArrFactory;
import com.xukeer.udp.plus.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/*
 * @author xqw
 * @description 消息集接收处理器
 * @date 11:56 2021/12/3
 **/
final class CrowdReceiverHandler {
    private static final Logger log = LoggerFactory.getLogger(CrowdReceiverHandler.class);
    private final Map<Long, ReceiveCrowd>  receiveCrowdMap = new HashMap<>();
    private final ICrowdReceiver iCrowdReceiver;

     CrowdReceiverHandler(ICrowdReceiver iCrowdReceiver) {
        this.iCrowdReceiver = iCrowdReceiver;
    }

     void addCommonMsg(CrowdOptionMsg crowdOptionMsg, InetSocketAddress inetSocketAddress) {
        long sequence = crowdOptionMsg.getSequence();
        ReceiveCrowd receiveCrowd;
        if (!receiveCrowdMap.containsKey(sequence)) {
            receiveCrowd = new ReceiveCrowd(crowdOptionMsg.getTotalCrowdAmount(), iCrowdReceiver, receiveCrowdMap::remove);
            receiveCrowdMap.put(sequence, receiveCrowd);
        } else {
            receiveCrowd = receiveCrowdMap.get(sequence);
        }
        receiveCrowd.addSimpleMsg(crowdOptionMsg, inetSocketAddress);
    }

     void remove(long sequence) {
        receiveCrowdMap.remove(sequence);
    }

     byte[] getMissSimpleIndex(long sequence) {
        ReceiveCrowd receiveCrowd = receiveCrowdMap.get(sequence);
        if(receiveCrowd!=null){
            return receiveCrowd.getMissSimpleIndex();
        }
        return null;
    }

    private interface IRemoveReceiveCrowdMap {
        void remove (long sequence);
    }

    private static class ReceiveCrowd {
        private final int totalCrowdAmount;       // 总的消息集
        private int currentCrowdIndex = 0;     // 当前处理到第几个集,使用情况下是会递增的
        private final byte[][] crowdData;  // 整个消息体的消息内容
        private byte[][] simpleMsgData; // 单个簇的消息内容
        private byte currentSimpleCompleteCount;   // 当前簇完成接收的消息个数

        private byte[] missSimpleIndex;
        private byte missCount;

        private final ICrowdReceiver iCrowdReceiver;
        private final IRemoveReceiveCrowdMap iRemoveReceiveCrowdMap;

        ReceiveCrowd(int totalCrowdAmount, ICrowdReceiver iCrowdReceiver, IRemoveReceiveCrowdMap iRemoveReceiveCrowdMap) {
            this.totalCrowdAmount = totalCrowdAmount;
            this.iCrowdReceiver = iCrowdReceiver;
            this.iRemoveReceiveCrowdMap = iRemoveReceiveCrowdMap;
            crowdData = new byte[totalCrowdAmount][];
        }

        private void addSimpleMsg(CrowdOptionMsg crowdOptionMsg, InetSocketAddress inetSocketAddress) {
            long sequence = crowdOptionMsg.getSequence();
            int crowdIndex = crowdOptionMsg.getCrowdIndex();

            if (crowdIndex != currentCrowdIndex) {
                // 假如 ，第一个簇还没有接收完成，就已经接收到了第二个簇，这种情况下是属于无效接收，应该直接返回掉
                return;
            }

            if (simpleMsgData == null) {
                byte simpleAmount = crowdOptionMsg.getTotalSimpleAmount();

                simpleMsgData = new byte[simpleAmount][];
                missSimpleIndex =  new byte[simpleAmount];
                missCount = simpleAmount;
                iCrowdReceiver.addReqSendMissMsg(sequence,crowdIndex,inetSocketAddress);
            }

            if(crowdOptionMsg instanceof CommonMsg) {
                CommonMsg commonMsg = (CommonMsg) crowdOptionMsg;
                if (simpleMsgData[commonMsg.getSimpleIndex()] == null) {
                    simpleMsgData[commonMsg.getSimpleIndex()] =  commonMsg.getMsg();


                    currentSimpleCompleteCount++;

                    missSimpleIndex[commonMsg.getSimpleIndex()] = 2;
                    missCount--;

                    byte totalSimpleAmount = commonMsg.getTotalSimpleAmount();

                    if (currentSimpleCompleteCount == totalSimpleAmount) {

                       // ByteArrFactory.giveBackByteArr(missSimpleIndex);

                        // 消息集处理完成
                        crowdData[currentCrowdIndex] = getAllBytes(simpleMsgData,true);


                        if (currentCrowdIndex == totalCrowdAmount - 1) {
                            // 整个消息处理完成
                            byte[] result = getAllBytes(crowdData,false);
                            ByteArrFactory.giveBackByteArr(crowdData);
                            iCrowdReceiver.msgReceiveComplete(result, sequence, inetSocketAddress);
                           // ByteArrFactory.giveBackByteArr(result);
                            iRemoveReceiveCrowdMap.remove(sequence);
                        }

                        iCrowdReceiver.simpleMsgReceiveComplete(sequence, currentCrowdIndex, inetSocketAddress);
                        ByteArrFactory.giveBackByteArr(simpleMsgData);
                        simpleMsgData = null;
                        missSimpleIndex = null;

                        currentSimpleCompleteCount = 0;
                        currentCrowdIndex++;
                    }
                }
            }
        }


        private byte[] getAllBytes(byte[][] byteArr, boolean isReturn) {
            int sum = 0;
            for (byte[] value : byteArr) {
                sum += Utils.byteArrToInt(value);
            }
            int index = isReturn ? 4 : 0;
            byte[] retBytes;
            if (isReturn) {
                retBytes = ByteArrFactory.applyByteArr(sum);
                System.arraycopy(Utils.intToBytes(sum), 0, retBytes, 0, 4);
            } else {
                retBytes = new byte[sum];
            }
            for (byte[] bytes : byteArr) {
                int currentLength = Utils.byteArrToInt(bytes);
                System.arraycopy(bytes, 4, retBytes, index, currentLength);
                index += currentLength;
            }
            return retBytes;
        }


        private byte[] getMissSimpleIndex() {
            if (missSimpleIndex != null && missSimpleIndex.length > 0) {
                byte[] ret = new byte[missCount];
                int index = 0;
                for (byte b = 0; b < missSimpleIndex.length; b++) {
                    if (missSimpleIndex[b] != 2) {
                        ret[index] = b;
                        index++;
                    }
                }
                return ret;
            }else {
                log.error("get getMissSimpleIndex failed" );
                return null;
            }
        }
    }
}
