package com.xukeer.test.commom;

/*
 * @author xqw
 * @description 数组游戏
 * 给定一个整数数组，请算出让所有元素相同的最小步数。每一步你可以选择一个元素，使得其他元素全部加1。
 * 1≤len(arr)≤10 5
 * 0≤arri≤10 9
 * @date 15:28 2021/10/9
 **/
public class Solution1907 {
    public static void main(String[] args) {
        int[] arr = {104011621,670461818,196734305,571788873,657486783,131974634,540557728,653966184,157276364,221811590};

        long steps = arrayGame(arr);

        System.out.println(steps);
    }

    public static long arrayGame(int[] arr) {
        // write your code here
        int min = arr[0];
        int length = arr.length;
        long step = 0;
        for (int i = 1; i < length; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        for (int value : arr) {
            step = step + value - min;
        }
        return step;
    }
}
