package com.xukeer.test.commom;

/*
 * @author xqw
 * @description
 * 给定一个整数数组和一个整数k，你需要找到和为k的最短非空子数组，并返回它的长度。
 * 如果没有这样的子数组，返回-1.
 * @date 11:33 2021/9/30
 **/
public class Solution1844 {
    public static void main(String[] args) {
        int[] nums={1,1,1,2};
        int length = subarraySumEqualsKII(nums,3);
        System.out.println(length);
    }

    public static int subarraySumEqualsKII(int[] nums, int k) {
        int maxLength = nums.length;

        for (int length = 1; length <= maxLength; length++) {
            int count =0;
            for (int i = 0; i < length; i++) {
                count+=nums[i];
            }
            if (count == k) {
                return length;
            }
            for (int index = 0; index < maxLength-length; index++) {
                count-=nums[index];
                count+=nums[index+length];
                if (count == k) {
                    return length;
                }
            }
        }
        // write your code here
        return -1;
    }
}
