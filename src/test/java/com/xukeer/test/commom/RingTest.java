package com.xukeer.test.commom;

/*
 * @author xqw
 * @description
 * @date 16:11 2021/11/18
 **/
public class RingTest {
    public static void main(String[] args) {
        long t = System.currentTimeMillis();
        long i=0;
        while (t+1000>System.currentTimeMillis()){
            i++;
        }
        System.out.println(i);
    }


    private class Waiter {
        private long time;



    }

}
