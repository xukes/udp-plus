package com.xukeer.test.commom;

import java.util.*;

/**
 * @author xqw
 * @Description 最小子串覆盖  同向双指针法
 * 给定两个字符串 source 和 target. 求 source 中最短的包含 target 中每一个字符的子串.
 * 如果没有答案, 返回 "".
 * 保证答案是唯一的.
 * target 可能包含重复的字符, 而你的答案需要包含至少相同数量的该字符.
 * 0 <= len(source) <= 1000000
 * 0 <= len(target) <= 1000000
 * @date 10:26 2021/12/24
 **/
public class Solution32 {
    public static void main(String[] args) {
        List<String> strLineList = Utils.readFile("F:/data/tmp/testdatav2_32_data_21.in");
        String source = strLineList.get(0);
        String target = strLineList.get(1);
        long str = System.currentTimeMillis();
        String result = minWindow(source, target);
        System.out.println(System.currentTimeMillis() - str);
        System.out.println(result);
    }

    public static String minWindow(String source, String target) {
        if (source.equals("") || target.equals("")) {
            return "";
        }

        char[] td = new char[256];
        char[] sd = new char[256];

        for (int i = 0; i < target.length(); i++) {
            td[target.charAt(i)]++;
        }

        int start = 0;
        int end = source.length();
        int found = 0;
        int minLength = source.length();
        int first = -1;


        for (int i = 0; i < source.length(); i++) {
            sd[source.charAt(i)]++;
            if (sd[source.charAt(i)] <= td[source.charAt(i)]) {
                found++;
            }

            if (found == target.length()) {
                while (start < i && sd[source.charAt(start)] > td[source.charAt(start)]) {
                    sd[source.charAt(start)]--;
                    start++;
                }
                if (i + 1 - start <= minLength) {
                    minLength = i - start + 1;
                    first = start;
                    end = i + 1;
                }
                sd[source.charAt(start)]--;
                found--;
                start++;
            }
        }

        if (first == -1) {
            return "";
        } else {
            return source.substring(first, end);
        }
    }

}
