package com.xukeer.test.commom;

/*
 * @Author xqw
 * @Description 切割剩余金属
 *
 * 金属棒工厂的厂长拥有 n 根多余的金属棒。当地的一个承包商提出，只要所有的棒材具有相同的长度（用 saleLength 表示棒材的长
 * 度），就将金属棒工厂的剩余棒材全部购买。厂长可以通过将每根棒材切割零次或多次来增加可销售的棒材数量，
 * 但是每次切割都会产生一定的成本（用 costPerCut 表示每次切割的成本）。等所有的切割完成以后，多余的棒材将被丢弃，
 * 没有利润。金属棒工厂的厂长获得的销售总利润计算公式如下：
 * totalProfit = totalUniformRods * saleLength * salePrice - totalCuts * costPerCut
 * 其中 totalUniformRods 是可销售的金属棒数量，salePrice 是承包商同意支付的每单位长度价格，totalCuts是需要切割棒材的次数。
 *
 * 1≤n≤50
 * 1 ≤ lengths[i] \le 10^4
 * 1 ≤ salePrice,costPerCut ≤ 10
 *
 * @Date 15:11 2021/6/11
 **/
public class Solution1917 {

    public static void main(String[] args) {
        int []arr = {30,59,110};
       int profit = maxProfit(1,10,arr);

        System.out.println(profit);
    }

    /**
     * @param costPerCut: integer cost to make a cut
     * @param salePrice:  integer per unit length sales price
     * @param lengths:    an array of integer rod lengths
     * @return: The function must return an integer that denotes the maximum possible profit.
     */
    public static int maxProfit(int costPerCut, int salePrice, int[] lengths) {
        int cutMax = 0;
        for (int l : lengths) {
            cutMax = Math.max(l, cutMax);
        }

        int profit = 0;
        for (int i = 1; i < cutMax; i++) {
            profit = Math.max(profit, profit(costPerCut, salePrice, lengths, i));
        }
        return profit;
    }

    private static int profit(int costPerCut, int salePrice, int[] lengths, int cutLength) {
        int totalCut = 0;
        int total = 0;

        for (int l : lengths) {
            int cut = l / cutLength;
            totalCut += cut;
            if (l % cutLength == 0) {
                total += cut - 1;
            } else {
                total += cut;
            }
        }
        return totalCut * salePrice * cutLength - costPerCut * total;
    }
}
