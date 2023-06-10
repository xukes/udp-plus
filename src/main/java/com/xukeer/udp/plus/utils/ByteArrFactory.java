package com.xukeer.udp.plus.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/*
 * @author xqw
 * @description 字节数组构建工厂
 * 该工厂用于创建字节数组以及回收使用完成的字节数组义工下次需要的时候再使用，主要是为了避免大量反复创建字节数组，
 * @date 17:41 2021/12/6
 **/
public class ByteArrFactory {
    private static final List<byte[]> listOneKByte = new LinkedList<>();   // 1kb字节的数组,100个
    private static final List<byte[]> listTwoKByte = new LinkedList<>();   // 2kb字节的数组,50个
    private static final List<byte[]> ListFourKByte = new LinkedList<>(); // 4Kb字节的数组，50个
    private static final List<byte[]> ListEightKByte = new LinkedList<>(); // 8Kb字节的数组，50个
    private static final List<byte[]> ListSixteenKByte = new LinkedList<>(); // 16Kb字节的数组，20个
    private static final List<byte[]> ListThirtyTwoKByte = new LinkedList<>(); // 32Kb字节的数组，20个
    private static final List<byte[]> ListSixtyFourKByte = new LinkedList<>(); // 64Kb字节的数组，10个

    private static final List<byte[]> listOneKByteUsing = new LinkedList<>();   // 1kb字节的数组,100个
    private static final List<byte[]> listTwoKByteUsing = new LinkedList<>();   // 2kb字节的数组,50个
    private static final List<byte[]> ListFourKByteUsing = new LinkedList<>(); // 4Kb字节的数组，50个
    private static final List<byte[]> ListEightKByteUsing = new LinkedList<>(); // 8Kb字节的数组，50个
    private static final List<byte[]> ListSixteenKByteUsing = new LinkedList<>(); // 16Kb字节的数组，20个
    private static final List<byte[]> ListThirtyTwoKByteUsing = new LinkedList<>(); // 32Kb字节的数组，20个
    private static final List<byte[]> ListSixtyFourKByteUsing = new LinkedList<>(); // 64Kb字节的数组，10个

    private static final int kb = 1024;

    static {
        for (int i = 0; i < 100; i++) {
            listOneKByte.add(new byte[kb + 4]);
            if (i < 50) {
                int length = 2 << 10;
                listTwoKByte.add(new byte[length + 4]);
                length = length << 1;
                ListFourKByte.add(new byte[length + 4]);
                length = length << 1;
                ListEightKByte.add(new byte[length + 4]);
                if (i < 20) {
                    length = length << 1;
                    ListSixteenKByte.add(new byte[length + 4]);
                    length = length << 1;
                    ListThirtyTwoKByte.add(new byte[length + 4]);
                }
                if (i < 10) {
                    length = length << 1;
                    ListSixtyFourKByte.add(new byte[length + 4]);
                }
            }
        }
    }

    /**
     * @param length    实际长度
     * @param applySize 申请长度
     */
    private static byte[] handle(int length, int applySize, List<byte[]> preList, List<byte[]> usingList) {
            byte[] a = Utils.intToBytes(length);
            Iterator<byte[]> oneKByteIterator = preList.iterator();
            if (oneKByteIterator.hasNext()) {
                byte[] arr = oneKByteIterator.next();
                System.arraycopy(a, 0, arr, 0, 4);
                oneKByteIterator.remove();
//            usingList.add(arr);
                return arr;
            }
            byte[] arr = new byte[applySize + 4];
            System.arraycopy(a, 0, arr, 0, 4);

            return arr;
    }

    /**
     * 申请数组
     */
    public static byte[] applyByteArr(int length) {
        int len = length + 4;
        if (len < kb) {
            synchronized (listOneKByte) {
                return handle(length, kb, listOneKByte, listOneKByteUsing);
            }
        }
        if (len < kb << 1) {
            synchronized (listTwoKByte) {
                return handle(length, kb << 1, listTwoKByte, listTwoKByteUsing);
            }
        }
        if (len < kb << 2) {
            synchronized (ListFourKByte) {
                return handle(length, kb << 2, ListFourKByte, ListFourKByteUsing);
            }
        }
        if (len < kb << 3) {
            synchronized (ListEightKByte) {
                return handle(length, kb << 3, ListEightKByte, ListEightKByteUsing);
            }
        }
        if (len < kb << 4) {
            synchronized (ListSixteenKByte) {
                return handle(length, kb << 4, ListSixteenKByte, ListSixteenKByteUsing);
            }
        }
        if (len < kb << 5) {
            synchronized (ListThirtyTwoKByte) {
                return handle(length, kb << 5, ListThirtyTwoKByte, ListThirtyTwoKByteUsing);
            }
        }
        if (len < kb << 6) {
            synchronized (ListSixtyFourKByte) {
                return handle(length, kb << 6, ListSixtyFourKByte, ListSixtyFourKByteUsing);
            }
        }
        byte[] retArr =  new byte[len];
        System.arraycopy( Utils.intToBytes(len), 0, retArr, 0, 4);
        return retArr;
    }

    public static void giveBackByteArr(byte[][] arr){
        for(byte []b: arr){
            giveBackByteArr(b);
        }
    }

    /**
     * 使用完归还数组
     */
    public static void giveBackByteArr(byte[] arr) {
        int length = arr.length-4;
        if (length == kb) {
            synchronized (listOneKByte) {
                if (listOneKByte.size() < 200) {
                    listOneKByte.add(arr);
                }
            }
        } else if (length == kb << 1) {
            synchronized (listTwoKByte) {
                if (listTwoKByte.size() < 2000) {
                    listTwoKByte.add(arr);
                }
            }
        } else if (length == kb << 2) {
            synchronized (ListFourKByte) {
                if (ListFourKByte.size() < 100) {
                    ListFourKByte.add(arr);
                }
            }
        } else if (length == kb << 3) {
            synchronized (ListEightKByte) {
                if (ListEightKByte.size() < 100) {
                    ListEightKByte.add(arr);
                }
            }
        } else if (length == kb << 4) {
            synchronized (ListSixteenKByte) {
                if (ListSixteenKByte.size() < 50) {
                    ListSixteenKByte.add(arr);
                }
            }
        } else if (length == kb << 5) {
            synchronized (ListThirtyTwoKByte) {
                if (ListThirtyTwoKByte.size() < 50) {
                    ListThirtyTwoKByte.add(arr);
                }
            }
        } else if (length == kb << 6) {
            synchronized (ListSixtyFourKByte) {
                if (ListSixtyFourKByte.size() < 30) {
                    ListSixtyFourKByte.add(arr);
                }
            }
        }
    }

    public static void main(String[] args) {
//        byte[] a = applyByteArr(1022);
//        byte[] a1 = applyByteArr(1025);
//        byte[] a2 = applyByteArr(3022);
//        byte[] a3 = applyByteArr(5622);
//        byte[] a4 = applyByteArr(9622);
//        byte[] a5 = applyByteArr(19622);
//        byte[] a6 = applyByteArr(39622);
//
//
//
//        System.out.println(a.length);
//        giveBackByteArr(a);
//        giveBackByteArr(a1);
//        giveBackByteArr(a2);
//        giveBackByteArr(a3);
//        giveBackByteArr(a4);
//        giveBackByteArr(a5);
//        giveBackByteArr(a6);
//        System.out.println(a.length);


    }

}
