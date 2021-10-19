package com.xukeer.test.commom;

import java.util.HashMap;
import java.util.Map;

/*
 * @author xqw
 * @description 1840 · 矩阵还原
 * 现有一个n行m列的矩阵beforebefore，对于before里的每一个元素before[i][j]，我们会使用以下算法将其转化为after[i][j]。
 * 现给定afterafter矩阵，请还原出原有的矩阵beforebefore。
 *
 * s = 0
 * for i1: 0 -> i
 *   for j1: 0 -> j
 *       s = s + before[i1][j1]
 * after[i][j] = s
 *
 * @date 10:09 2021/10/8
 *
 * 解题思路，通过递归的方式,首先求最后一个，
 **/
public class Solution1840 {
static Map<String, Integer> map= new HashMap<>();
    public static void main(String[] args) {
        int[][] nums = {{744,1466,2369},{426,393,846},{590,1364,2583},{277,740,1193},{616,891,1941},{1442,2342,2407},{2250,2362,2998},{2832,2540,2621},{3281,2547,2918},{2816,1132,533},{3272,943,552},{2483,-709,-797},{2574,-330,-998},{2367,-195,-1490},{1735,-1225,-2652},{1905,-1776,-3670},{1998,-2368,-4364},{1839,-2236,-3727},{2805,-1272,-2238},{1960,-1148,-2822},{2151,-1722,-2751},{1155,-2487,-3210},{1910,-1777,-2998},{2086,-1105,-2434},{2211,-598,-2596},{2446,-376,-2880},{3365,1408,-1096},{2770,1677,-1707},{2305,583,-2948},{2694,1900,-1935},{3066,1596,-2096},{3523,2323,-2002},{4345,2873,-2351},{5321,2857,-2567},{4922,3328,-1796},{4400,3491,-2591},{5341,3437,-1716},{5016,3890,-2168},{5852,4617,-2208},{5082,3147,-3176},{5116,4138,-1464},{4179,2779,-2784},{3618,1399,-3328},{4153,1241,-3270},{4020,1406,-3339},{4954,2846,-2275},{5514,3803,-594}};
        int n = 47;
        int m = 3;
        int[][] result = matrixRestoration(n, m, nums);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(result[i][j] + ",");
            }
            System.out.println();
        }
    }

    public static int[][] matrixRestoration(int n, int m, int[][] after) {
        int[][] before = new int[n][m];
        before[0][0]=after[0][0];
        handler(n, m, after, before);
        return before;
    }


    public static void handler(int n, int m, int[][] after, int[][] before) {
        int[] sum = new int[m];
        for (int i = 0; i < n; i++) {
            getSumI(sum, i - 1, m, before);
            int currentSum = 0;
            for (int j = 0; j < m; j++) {
                before[i][j] = after[i][j] - (currentSum + getss(j, sum));
                currentSum = currentSum + before[i][j];
            }
        }
    }

    private static int getss(int index, int[] sum) {
        int sunResult = 0;
        for (int i = 0; i <= index; i++) {
            sunResult += sum[i];
        }
        return sunResult;
    }


    private static void getSumI( int []sum, int n, int m, int[][] before) {
        if (n < 0) {
            return;
        }
        for (int l = 0; l < m; l++) {
            sum[l] += before[n][l];
        }
    }
}
