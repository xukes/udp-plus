package com.xukeer.test.commom;


import java.util.HashMap;
import java.util.Map;

/*
 * @Author xqw
 * @Description 滑动窗口内唯一元素数量和
 * @Date 16:10 2021/4/29
 **/
public class Solution692 {

//    @Test
    public void test() {
        int[] nums = {1,1,1,1,14,23,12,12,154,123,422,12,1};
        System.out.println(slidingWindowUniqueElementsSum(nums, 3));
    }

    public int slidingWindowUniqueElementsSum(int[] nums, int k) {
        StreamArr streamArr = new StreamArr();

        int total = 0;
        if (nums.length < k) {
            for (int num : nums) {
                streamArr.swap(num);
            }
            total = streamArr.getOnceNum();
        } else {
            for (int i = 0; i < k; i++) {
                streamArr.swap(nums[i]);
            }
            total+=streamArr.getOnceNum();
            for (int index = k, tmpLength = nums.length ; index < tmpLength; index++) {
                streamArr.swap(nums[index - k], nums[index]);
                total+=streamArr.getOnceNum();
            }
        }
        return total;
    }

    public static class StreamArr {
        private Map<Integer, Integer> map = new HashMap<>();
        private int onceNum;

        public void swap(int in) {
            Integer inNum = map.get(in);
            if (inNum == null) {
                map.put(in, 1);
                onceNum++;
            } else {
                if(inNum == 1){
                   onceNum = onceNum - 1;
                }
                map.put(in, inNum + 1);
            }
        }

        public void swap(int out, int in) {
            int num = map.get(out);
            if (num == 1) {
                onceNum = onceNum - 1;
                map.remove(out);

            } else if (num > 1) {
                if (num == 2) {
                    onceNum++;
                }
                map.put(out, num - 1);
            }

            Integer inNum = map.get(in);
            if (inNum == null) {
                map.put(in, 1);
                onceNum++;
            } else {
                if (inNum == 1) {
                    onceNum = onceNum - 1;
                }
                map.put(in, inNum + 1);
            }
        }

        public int getOnceNum() {
            return onceNum;
        }
    }
}
