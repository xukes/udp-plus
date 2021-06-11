package com.xukeer.test.commom;

/*
 * @Author xqw
 * @Description
 * @Date 9:02 2021/6/8
 **/
public class BTC {
    public static void main(String[] args) {
        double firstBuyPrice = 45000;                // 第一次买入价
        double firstBuyAmount = 3000;                // 初次买入金额
        double nowPrice = 38664;                     // 现价
        double exceptionPrice = 41000;               // 期望价

        fair(firstBuyPrice, firstBuyAmount, nowPrice, exceptionPrice);
    }

    /**
     * 计算持平需要继续投入的资金
     */
    private static void fair(double firstBuyPrice, double firstBuyAmount, double nowPrice, double exceptionPrice) {
        // 截止目前亏损金额
        double loseAmount = (firstBuyPrice - nowPrice) / firstBuyPrice * firstBuyAmount;
        // 以目前市价上涨到期望市价的上涨幅度
        double x = (exceptionPrice - nowPrice) / nowPrice;
        //
        double s = (loseAmount - (firstBuyAmount - loseAmount) * x) / x;

        System.out.println(String.format("已亏损: %20f", loseAmount));
        System.out.println(String.format("持平需要上涨的幅度: %20f", x));
        System.out.println(String.format("持平需要继续投入金额: %20f", s));
    }
}
