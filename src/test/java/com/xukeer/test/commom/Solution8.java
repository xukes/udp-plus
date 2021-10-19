package com.xukeer.test.commom;

/*
 * @author xqw
 * @description
 * @date 16:20 2021/10/13
 **/
public class Solution8 {
    public static void main(String[] args) {
        char[] str = {'a','b','c','d','e','f','g'};

        rotateString(str,1);

        for(char s: str){
            System.out.println(s);
        }
    }

    public static void rotateString(char[] str, int offset) {
        int length = str.length;

        if (length < 1) {
            return;
        }

        if (offset > length) {
            offset = offset % length;
        }
        char[] newStr = new char[length];
        for (int i = 0; i < length; i++) {
            int index = i - offset;
            if (index < 0) {
                index = length+index;
            }
            newStr[i] = str[index];
        }

        System.arraycopy(newStr, 0, str, 0, length);
    }
}
