package com.xukeer.test.commom;

/*
 * @author xqw
 * @description 分区数组
 *
 * 给定一个数字数组，您需要检查是否可以将该数组划分为每个长度为k的子序列，例如
 * 数组中的每个元素仅在一个子序列中出现
 * 子序列中的所有数字都是不同的
 * 数组中具有相同值的元素必须位于不同的子序列中
 *
 * 是否可以对满足以上条件的数组进行分区？ 如果可能，返回true，否则返回false。
 *
 * 模拟法
 * @date 9:31 2021/10/14
 **/
public class Solution1523 {
    public static void main(String[] args) {
        int[] A = {0, 2, 3, 4};
        int k = 2;
        boolean result = PartitioningArray(A,k);
        System.out.println(result);

    }
    public static boolean PartitioningArray(int[] A, int k) {
        // write your code here
        int length = A.length;
        if (length% k > 0) {
            return false;
        }
        //int l = length/k;
        int[][] aArr = new int[length][2];
        int index =0;
        for (int i = 0; i < length; i++) {
            int findIndex = findIndex(aArr, A[i]);
            if(findIndex>-1) {
                aArr[findIndex][1]++;
                if(i==0){
                    index++;
                }
            } else {
                aArr[index][0]=A[i];
                aArr[index][1]++;
                index++;
            }
        }
        int max = 0;
        for (int[] arr : aArr) {
            if (max < arr[1]) {
                max = arr[1];
            }
            if(arr[1]==0){
                break;
            }
        }
        return k>=max;
    }

    private static int findIndex(int[][] aArr, int v) {
        for (int i = 0, length = aArr.length; i < length; i++) {
            if (aArr[i][0] == v) {
                return i;
            }

            if(aArr[i][1]==0){
                break;
            }
        }
        return -1;
    }
}
