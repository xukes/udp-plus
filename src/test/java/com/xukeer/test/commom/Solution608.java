package com.xukeer.test.commom;

import org.junit.Test;

/*
 * @Author xqw
 * @Description 两数和 II-输入已排序的数组
 * 给定一个已经 按升序排列 的数组，找到两个数使他们加起来的和等于特定数。
 * 函数应该返回这两个数的下标，index1必须小于index2。注意返回的值不是 0-based。
 * @Date 13:53 2021/5/12
 **/
public class Solution608 {
    public int[] twoSum(int[] nums, int target) {
        // write your code here
        int[] arr = new int[2];
        for(int i=0;i<nums.length;i++){
            int first = nums[i];
            int lastNumber = target - first;
            int kk = i+1;
            int j = find(nums, lastNumber,kk,nums.length-1);
            if(j>0){
                arr[0]=i+1;
                arr[1]=j+1;
                return arr;
            }
        }
        return arr;
    }

    private int find(int[] nums, int target, int first, int last) {
        int key = (last + first) / 2;

        if (nums[key] == target) {
            return key;
        }

        if (key >= last) {
            return -1;
        }

        if (nums[key] > target) {

            return find(nums, target, first, key);
        } else if (nums[key] < target) {
            key+=((last + first) % 2);
            return find(nums, target, key, last);
        }

        return -1;
    }

    @Test
    public void test(){
        int [] arr = {0,0,11,15,23,43,44,56,57};

        int[] result = twoSum(arr,0);
        for(int i=0;i<result.length;i++){
            System.out.println(result[i]);
        }
    }
}
