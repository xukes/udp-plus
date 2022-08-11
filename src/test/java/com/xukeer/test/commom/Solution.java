package com.xukeer.test.commom;


import java.util.HashMap;

/*
 * @Author xqw
 * @Description
 * @Date 16:17 2021/4/23
 **/
public class Solution {
    //private static native int registerNatives();

    // static  int k=0;
    public static int integerReplacement(HashMap<Integer, Integer> map, int n) {
        // Write your code here
        if (!map.containsKey(n)) {
            if (n % 2 == 0) {
                map.put(n, integerReplacement(map, n / 2) + 1);
            } else {
                map.put(n, Math.min(integerReplacement(map, n - 1), integerReplacement(map, n + 1)) + 1);
            }
        }
        return map.get(n);
    }

    public static void main(String[] args) {
        int k=190912;
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, 0);
        int j = integerReplacement(map, k);
        System.out.println(String.format("i=%d, k=%d", k, j));
    }

    private static class Amount {
        private Integer count = 0;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }
}
