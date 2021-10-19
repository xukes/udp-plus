package com.xukeer.test.commom;

//import java.util.stream.Collectors;
//import java.util.stream.Stream;

/*
 * @author xqw
 * @description 聪明的销售
 * 销售主管的任务是出售一系列的物品，其中每个物品都有一个编号。
 * 由于出售具有相同编号的商品会更容易，所以销售主管决定删除一些物品。
 * 现在她知道她最多能删除多少物品，她想知道最终袋子里最少可以包含多少种不同编号的物品。
 * 例如，最开始她有n = 6 个物品，编号为：ids = [1,1,1,2,2,3]，她最多可以删除 m = 2 个物品。
 * 如果删除两个物品 1，则剩下的物品 ids = [1,2,2,3]，此时她拥有三种不同编号的物品。
 * 如果删除两个物品 2，则剩下的物品 ids = [1,1,1,3]，此时她拥有两种不同编号的物品。
 * 如果删除物品 2 和物品 3 各 1个，则剩下的物品 ids = [1,1,1,2]，此时她拥有两种不同编号的物品。
 * 我们发现，物品最多可以剩下两种不同的编号，所以你的程序要返回 2
 * @date 16:01 2021/6/28
 **/
public class Solution1914 {
    public static void main(String[] args) {
        //List<Integer> ids = Stream.of(1,3,4,5,2,3,4,5,6,7,12,33,12,34,53,1,3,6).collect(Collectors.toList());
        java.util.List<Integer> ids = java.util.stream.Stream.of(1,1,1,2,2,3).collect(java.util.stream.Collectors.toList());
        Integer m = 2;
        System.out.println(   minItem(ids,m));
    }

    /**
     * @param ids: ID number of items
     * @param m:   The largest number of items that can be remove
     * @return: the result of the min item
     */
    public static int minItem(java.util.List<Integer> ids, int m) {
        // write your code here
        java.util.List<Long> list = ids.stream().collect(java.util.stream.Collectors.groupingBy(Integer::intValue,
                java.util.stream.Collectors.counting())).values().parallelStream().sorted(java.util.Comparator.naturalOrder())
                .collect(java.util.stream.Collectors.toList());

        int size = list.size();
        int totalRemoveAmount = 0;


        int totalRemoveCount = 0;
        for (Long i : list) {
            totalRemoveAmount += i.intValue();
            totalRemoveCount++;
            if (totalRemoveAmount >= m) {
                if (totalRemoveAmount > m) {
                    totalRemoveCount--;
                }
                return size - totalRemoveCount;
            }
        }

        return 0;
    }
}
