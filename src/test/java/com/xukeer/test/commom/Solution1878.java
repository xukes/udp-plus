package com.xukeer.test.commom;

/*
 * @author xqw
 * @description
 * @date 17:00 2021/10/13
 **/
public class Solution1878 {
    public static void main(String[] args) {
        System.out.println(RotatedNums(6));
    }

    public static long RotatedNums(int n) {
        long[] arr = new long[n + 1];

        arr[1] = 5;

        if(n>=2) {
            arr[2] = 6;
        }

        for (int i = 3; i <= n; i++) {
            if (i % 2 == 0) {
                arr[i] = arr[i - 2] * 7;
            } else {
                arr[i] = arr[i - 1] * 5;
            }
        }
        return arr[n];
    }
}
