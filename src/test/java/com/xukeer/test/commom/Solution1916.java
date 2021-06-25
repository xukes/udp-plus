package com.xukeer.test.commom;

//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;

import java.io.*;
import java.util.stream.Collectors;

/*
 * @Author xqw
 * @Description 亮起时间最长的灯
 * 有一排 26 个彩灯，编号从 0 到 25，现在给出了一系列控制指令来控制这些彩灯的开关。
 * 一开始这些彩灯都是关闭的，然后指令将逐条发出。
 * 在每条指令operation[i]中含有两个整数 operation[i][0], operation[i][1]。
 * 在接收到一条指令时，标号为 operation[i][0] 的彩灯会亮起，直到第 operation[i][1] 秒的时候熄灭。
 * 当灯熄灭后，下一条指令将会发出。也就是说，任何时候只会有一盏灯亮着。
 * 其中第一条指令将在第0秒的时候发出，并被立刻执行。
 * 你的任务是找到哪个彩灯单次亮起的时间最长
 *
 * @Date 11:05 2021/6/9
 **/
public class Solution1916 {
    public static void main(String[] args) {
        java.util.List<java.util.List<Integer>> list = new java.util.LinkedList<>();
//        list.add(java.util.stream.Stream.of(6,5).collect(java.util.stream.Collectors.toList()));
//        list.add(java.util.stream.Stream.of(17,12).collect(java.util.stream.Collectors.toList()));
//        list.add(java.util.stream.Stream.of(24,14).collect(java.util.stream.Collectors.toList()));
//        list.add(java.util.stream.Stream.of(10,26).collect(java.util.stream.Collectors.toList()));
//        list.add(java.util.stream.Stream.of(16,30).collect(java.util.stream.Collectors.toList()));
//        list.add(java.util.stream.Stream.of(3,36).collect(java.util.stream.Collectors.toList()));
//        list.add(java.util.stream.Stream.of(14,72).collect(java.util.stream.Collectors.toList()));
//        list.add(java.util.stream.Stream.of(18,82).collect(java.util.stream.Collectors.toList()));
//        list.add(java.util.stream.Stream.of(15,87).collect(java.util.stream.Collectors.toList()));
//        list.add(java.util.stream.Stream.of(2,97).collect(java.util.stream.Collectors.toList()));


//        list.add(java.util.stream.Stream.of(0,2).collect(java.util.stream.Collectors.toList()));
//        list.add(java.util.stream.Stream.of(1,5).collect(java.util.stream.Collectors.toList()));
//        list.add(java.util.stream.Stream.of(0,9).collect(java.util.stream.Collectors.toList()));
//        list.add(java.util.stream.Stream.of(2,15).collect(java.util.stream.Collectors.toList()));

        String file = readFile("D:\\tmp\\testdatav2_1916_data_4.in");
        file = file.substring(2, file.length() - 2);

        String[] arr = file.split("\\]\\,\\[");
        for (String str : arr) {
            String[] b = str.split(",");
            list.add(java.util.stream.Stream.of(Integer.parseInt(b[0]), Integer.parseInt(b[1])).collect(java.util.stream.Collectors.toList()));
        }
        char result = longestLightingTime(list);
        System.out.println(result);
    }


    public static String readFile(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static char longestLightingTime(java.util.List<java.util.List<Integer>> operation) {
        Integer result = 0;
        Integer last = 0;
        int max = 0;
        for (java.util.List<Integer> l : operation) {
            int interval = l.get(1) - last;
            if (interval > max) {
                max = interval;
                result = l.get(0);
            }
            last = l.get(1);
        }

        return (char) ('a' + result);
    }
}

