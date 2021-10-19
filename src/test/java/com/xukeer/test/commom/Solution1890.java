package com.xukeer.test.commom;

/*
 * @author xqw
 * @description 形成最小数
 * 给定一个仅包含I和D的模式串str。 I代表相邻项增加，D代表相邻项减少。 设计一种算法，返回符合该模式且字典序最小的字符串。字符串只包含1到9且不能重复
 *
 * 模拟法，字符串，亚马逊
 * @date 10:05 2021/10/12
 **/
public class Solution1890 {

    public static void main(String[] args) {
        String str = "DIDI";
        String s= formMinimumNumber(str);
        System.out.println(s);
    }


    private static String formMinimumNumber(String str) {
        return null;
    }

    //    public static String formMinimumNumber(String str) {
//        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
//        String result = "";
//        char[] strArr = new char[str.length()];
//        for (int i = 0; i < strArr.length; i++) {
//            strArr[i] = str.charAt(i);
//        }
//        char startC = strArr[0];
//
//        int[][] temp = new int[10][2];
//
//        int index =0;
//        temp[index][0]= temp[index][0] = startC == 'D' ? 1 : -1;
//        temp[index][1] = 2;
//
//
//        for (int i = 1; i < strArr.length; i++) {
//            if (strArr[i] != startC) {
//                startC = strArr[i];
//                index++;
//                temp[index][0] = - temp[index-1][0];
//            }
//            temp[index][1]++;
//        }
//
//        int count = -1;
//        for (int i = 0; i < temp.length; i++) {
//            int[] a= temp[i];
//            int d = a[0];
//            int f = a[1];
//
//            if (d == 1) {
//                // --
//                for(int j=f;j>0;j--) {
//                  int s =  arr[j+count];
//                    result += s;
//                }
//            } else {
//                // ++
//                for(int j=1;j<=f;j++) {
//                    int s =  arr[j+count];
//                    result += s;
//                }
//            }
//            count=count+f;
//        }
//
//        // Write your code here.
//        return result;
//    }
}
