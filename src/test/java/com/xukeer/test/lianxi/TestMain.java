package com.xukeer.test.lianxi;

import java.util.LinkedList;
import java.util.List;

/**
 * @author xqw
 * @description
 * @date 5:17 PM 6/16/2022
 **/
public class TestMain {
    public static void main(String[] args) {
        List<int[]> list = new LinkedList<>();
        int total = 5;
        int[] usedArr = new int[total];
        int[] result = new int[total];
        dfs(0, total, usedArr, result, list);
        System.out.println(list.size());
        for (int[] arr : list) {
            for (int a : arr) {
                System.out.print(a);
            }
            System.out.println();
        }
    }

    /**
     * 总的个数
     */
    private static void dfs(int step, int total, int[] usedArr, int[] result, List<int[]> resultList) {
        for (int i = 0; i < total; i++) {
            if (step == total) {
                int[] newArr = new int[total];
                System.arraycopy(result, 0, newArr, 0, total);
                resultList.add(newArr);
                return;
            }
            if (usedArr[i] == 0) {
                result[step] = i;
                usedArr[i] = 1;
                dfs(step + 1, total, usedArr, result, resultList);
                usedArr[i] = 0;
            }
        }
    }
}
