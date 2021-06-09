package com.xukeer.test.commom;

/*
 * @Author xqw
 * @Description 会议室
 * 你有一个当前会议列表intervals，里面表明了每个会议的开始和结束时间，以及一些会议室rooms。
 * 现在有一系列会议ask需要加入，逐个判断他们能否被安排进当前的会议列表中而不产生冲突。
 * 一个会议室在同一时间只能进行一场会议。每个询问都相互独立
 * 例 1:
 * 输入:
 * Intervals:[[1,2],[4,5],[8,10]],rooms = 1,ask: [[2,3],[3,4]]
 * 输出:
 * [true,true]
 * 解释:
 * 对于[2,3]的询问，我们可以安排一个会议室room0。
 * 以下是room0的会议列表：
 * [[1,2],[2,3],[4,5],[8,10]]
 * 对于[3,4]的询问，我们可以安排一个会议室room0。
 * 以下是room0的会议列表：
 * [[1,2],[3,4],[4,5],[8,10]]
 * @Date 11:02 2021/6/4
 **/
public class Solution1897 {
    public static void main(String[] args) {
        int rooms = 3;
        int[][] intervals = {{1, 3}, {4, 6}, {6, 8}, {9, 11}, {6, 9}, {1, 3}, {4, 10}};
        int[][] ask = {{1, 9}, {2, 6}, {7, 9}, {3, 5}, {3, 9}, {2, 4}, {7, 10}, {5, 9}, {3, 10}, {9, 10}, {9, 10}};

        boolean[] result = meetingRoomIII(intervals, rooms, ask);

        for (boolean b : result) {
            System.out.println(b);
        }
    }

    /**
     * find word solution class student men human
     */
    public static boolean[] meetingRoomIII(int[][] intervals, int rooms, int[][] ask) {
        java.util.Map<String, Boolean> resultMap = new java.util.HashMap<>();
        boolean[] result = new boolean[ask.length];
        int[] freeTimes = new int[50001];
        int[] freeTime = new int[50001];

        for (int[] arr : intervals) {
            int start = arr[0];
            int end = arr[1];
            for (int r = start; r < end; r++) {
                freeTime[r]++;
            }
        }
        for (int i = 0; i < 50001; i++) {
            if (freeTime[i] >= rooms) {
                freeTimes[i] = 1;
            }
        }
        int i=0;
        for (int[] arr : ask) {
            int start = arr[0];
            int end = arr[1];

            String key = start + "-" + end;

            if (resultMap.containsKey(key)) {
                result[i] = resultMap.get(key);
                i++;
                continue;
            }
            for (int r = start; r < end; r++) {
                if (freeTimes[r] == 1) {
                    result[i] = false;
                    resultMap.put(key, false);
                    break;
                }
                if (r == end - 1 && freeTimes[r] == 0) {
                    result[i] = true;
                    resultMap.put(key, true);
                }
            }
            i++;
        }

        return result;
    }


}
