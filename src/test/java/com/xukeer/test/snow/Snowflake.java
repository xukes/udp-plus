package com.xukeer.test.snow;

/**
 * @author xqw
 * @description
 * @date 14:28 2022/7/20
 **/
public class Snowflake {
    /**
     * 起始的时间戳 2020/01/01
     */
    private final static long START_STMP = 1577808000000L;

    /**
     * 每一部分占用的位数
     */
    private final static long MACHINE_BIT = 5;   //机器标识占用的位数
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private static long sequence = 0L; //序列号
    private static long  lastStmp = -1L;//上一次时间戳

    /**
     * 产生下一个ID
     *
     * @return id
     */
    public static synchronized long nextId() {
        long currStmp = System.currentTimeMillis();
        if (currStmp < lastStmp) {
            throw new RuntimeException("时间改变，不能再次生成id");
        }

        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | sequence;                             //序列号部分
    }

    /**
     * 获取下一毫秒
     *
     * @return 。
     */
    private static  long getNextMill() {
        long mill = System.currentTimeMillis();
        while (mill <= lastStmp) {
            mill = System.currentTimeMillis();
        }
        return mill;
    }
}
