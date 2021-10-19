package com.xukeer.test.commom;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * @author xqw
 * @description 恢复旋转排序数组
 *
 * 给定一个旋转排序数组，在原地恢复其排序。（升序）
 * 什么是旋转数组？
 * 比如，原始数组为[1,2,3,4], 则其旋转数组可以是[1,2,3,4], [2,3,4,1], [3,4,1,2], [4,1,2,3]
 * @date 10:19 2021/10/14
 **/
public class Solution39 {
    public static void main(String[] args) {
        List<Integer> nums = Stream.of(3, 4, 5, 6, 7, 1, 2).collect(Collectors.toList());
        recoverRotatedSortedArray(nums);
        for (Integer s : nums) {
            System.out.println(s);
        }
    }

    public static void recoverRotatedSortedArray(List<Integer> nums) {
        // write your code here
        int last = nums.get(0);
        for (int i = 1, length = nums.size(); i < length; i++) {
            int current = nums.get(i);
            if (current < last) {
                rotate(nums,length-i);
                return;
            }
            last = current;
        }
    }

    private static void rotate(List<Integer> nums, int k) {
        int length = nums.size();
        reverse(length - k, length - 1, nums);
        reverse(0, length - 1 - k, nums);
        reverse(0, length - 1, nums);
    }

    private static void reverse(int start, int end, List<Integer> nums) {
        for (int i = start, j = end; i < j; i++, j--) {
            Integer temp = (nums.get(j));
            temp ^= nums.get(i);
            nums.set(j, temp);
            temp = (nums.get(i));
            temp ^= nums.get(j);
            nums.set(i, temp);
            temp = (nums.get(j));
            temp ^= nums.get(i);
            nums.set(j, temp);
        }
    }
}
