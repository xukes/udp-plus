package com.xukeer.udp.plus.utils;

import com.xukeer.udp.plus.common.exception.ByteNumberErrorException;
import com.xukeer.udp.plus.common.exception.DataOutException;

public class Utils {
    /**
     * 将int转成byte数组
     * 高字节在前
     */
    public static byte[] intToBytes(int i) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (i & 0xFF);
        targets[2] = (byte) (i >> 8 & 0xFF);
        targets[1] = (byte) (i >> 16 & 0xFF);
        targets[0] = (byte) (i >> 24 & 0xFF);
        return targets;
    }

    /**
     * 字节数组转整数
     * 高字节在前
     */
    public static int byteArrToInt(byte[] arr) {
        if (arr.length < 4) {
            throw new ByteNumberErrorException(String.format("字节数组转整数失败，应该提供四个字节的数组，但是当前数组的元素为=%d", arr.length));
        }
        return ((arr[0] & 0xff) << 24) + ((arr[1] & 0xff) << 16) + ((arr[2] & 0xff) << 8) + (arr[3] & 0xff);
    }


    public static String byteArrToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte b : bArray) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 从一个数组里面取出一段
     *
     * @throws DataOutException
     */
    public static byte[] cutBytes(byte[] sources, int start, int length) {
        checkParam(sources.length, start, length);

        byte[] newBytes = new byte[length];
        System.arraycopy(sources, start, newBytes, 0, start + length - start);
        return newBytes;
    }

    /**
     * 从一个数组里面取出一段
     *
     * @throws DataOutException
     */
    public static int[] cutInt(int[] sources, int start, int length) {
        checkParam(sources.length, start, length);
        int[] newBytes = new int[length];
        System.arraycopy(sources, start, newBytes, 0, start + length - start);
        return newBytes;
    }

    /**
     * 检查参数是否合法
     */
    private static void checkParam(int sourceLength, int start, int length) {
        if (start < 0 || length < 0) {
            throw new DataOutException("索引的起始位置或长度应该大于0，当前 start: {" + start + "}, length: {" + length + "}");
        }
        if (sourceLength < start + length) {
            throw new DataOutException(String.format("数组越界，数组长度=%d,start=%d,length=%d", sourceLength, start, length));
        }
    }

    /**
     * 取两个数相除向上取整的值
     *
     * @param molecule    分子
     * @param denominator 分母
     */
    public static long roundUpperNumbers(long molecule, int denominator) {
        long value = molecule / denominator;
        return molecule % denominator == 0 ? value : value + 1;
    }

    public static String byteToHexString(byte[] arr) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            str.append(arr[i]);
        }
        return str.toString();
    }


    public static String strToHexByteString(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0, length = str.length(); i < length; i++) {
            char c = str.charAt(i);
            stringBuilder.append(Integer.toHexString(c));
        }
        return stringBuilder.toString();
    }

    public static int getIntRand() {
        return (int) (Math.random() * Integer.MAX_VALUE);
    }

    public static Long getLongFromIPPort(byte[] ip, int port) {
        long longIp = byteArrToInt(ip);
        return (longIp << 32) + port;
    }


    public static long getLongRand(){
        return (long) (Math.random() * Long.MAX_VALUE);
    }


    public static void main(String[] args) {
        byte[] s={(byte) 127, (byte) 127, (byte) 127, (byte) 127};
        System.out.println(getLongFromIPPort(s,4999));

        //		for (int i = 10000; i < 11000; i++) {
//			System.out.println(byteArrToHexString(intToBytes(i)));
//		}

        //	System.out.println(roundUpperNumbers(1213L,13));

     //   System.out.println(strToHexByteString("he12ksdi8495932312849382918293849583910912384478596079083he12ksdihe12ksdi84959323he12ksdi8495932312849382918293849583910912384478596079083888211aaddccffeello12849382918293849583910912384478596079083888211aaddccffeello8495932312849382918293849583910912384478596079083888211aaddccffeellohe12ksdi8495932312849382918he12ksdi8495932312849382918293849583910912384478596079083888211aaddccffeello2he12ksdi8495932312849382918293849583910912384478596079083888211aaddccffeello93849583910912384478596079083888211aaddccffeello888211aaddccffeello"));

    }


    public static String getKey(int sequence, int crowdIndex) {
        return sequence + "-" + crowdIndex;
    }
}
