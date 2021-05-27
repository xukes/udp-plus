package com.xukeer.test.commom;

import org.junit.Test;

/*
 * @Author xqw
 * @Description 漂亮数组
 *  对于某些固定的 N，如果数组 A 是整数 1, 2, ..., N 组成的排列，使得：
    对于每个 i < j，都不存在 k 满足 i < k < j 使得 A[k] * 2 = A[i] + A[j]。
    那么数组 A 是漂亮数组。
    给定 N，返回任意漂亮数组 A（保证存在一个）。
 * @Date 10:09 2021/5/12
 **/
public class Solution1710 {
    @Test
    public void test() {
        // A[k] * 2 = A[i] + A[j];

        int n = 100;
        int []arr = beautifulArray(n);
    }

    public int[] beautifulArray(int N) {
        int[] arr = new int[N];
        for (int i = 0; i < N; i++) {
            arr[i] = i + 1;
        }
        return arr;
    }
}
