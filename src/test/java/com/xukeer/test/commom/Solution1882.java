package com.xukeer.test.commom;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/*
 * @author xqw
 * @description
 *   现在给你两个长度均为N的整数数组 A 和 B。
 *   当(A[0]+...A[K-1])，(A[K]+...+A[N-1])，(B[0]+...+B[K-1]) 和 (B[K]+...+B[N-1])四个和值大小相等时，
 *   称索引K是一个公平索引。也就是说，索引K 可以使得A， B 两个数组被分成两个非空数组，这四个子数组的和值相等。
 *   例如，数组A = [4,-1,0,3]，B = [-2,5,0,3]，那么索引 K = 2是公平的，子数组的和相等：4+(-1) = 3; 0+3 = 3; -2 + 5 = 3 and 0 + 3 = 3。
 *   现在请你计算公平索引的个数。
 * @date 9:43 2021/9/30
 **/
public class Solution1882 {
    // 开始时间9点45
    public static void main(String[] args) {
       List<String> strLineList = Utils.readFile("D:\\tmp\\testdatav2_1882_data_3.in");

       List<List<Integer>> lll = new LinkedList<>();
       for(String str : strLineList) {
           str = str.substring(1, str.length() - 1);
           String[] s = str.split(",");
           List<Integer>A = new LinkedList<>();
           for(String si :s){
               A.add(Integer.parseInt(si));
           }
           lll.add(A);
       }
        int count = CountIndexes(lll.get(0),lll.get(1));
        long s = System.currentTimeMillis();
        System.out.println(count);
        System.out.println(System.currentTimeMillis()-s);
    }

    public static int CountIndexes(List<Integer> A, List<Integer> B) {
        return CountIndexesA(A,B);
    }

    public static int CountIndexesA(List<Integer> A,List<Integer> B) {
        long preA = 0;
        long sumA = A.stream().mapToLong(Integer::longValue).sum();
        long preB = 0;
        long sumB = B.stream().mapToLong(Integer::longValue).sum();

        int count=0;

        for (int i = 0, length = A.size(); i < length-1; i++) {
            int valA = A.get(i);
            int valB = B.get(i);
            preA = preA + valA;
            sumA = sumA - valA;

            preB = preB + valB;
            sumB = sumB - valB;

            if (preA == sumA && sumA==sumB && preB==sumB) {
                count++;
            }
        }
        return count;
    }



}
