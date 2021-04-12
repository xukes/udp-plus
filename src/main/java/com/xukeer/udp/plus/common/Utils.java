package com.xukeer.udp.plus.common;

/*
 * @Author xqw
 * @Description
 * @Date 11:59 2021/1/11
 **/
public class Utils {
    public static String byteToHexString(byte[] arr) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            str.append(arr[i]);
        }
        return str.toString();
    }
}
