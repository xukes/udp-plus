package com.xukeer.test.commom;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
 * @Author xqw
 * @Description 滑动窗口的最大值
 * @Date 15:45 2021/4/30
 **/
public class Solution362 {
    @Test
    public void test() {
        List<String> list = new LinkedList<>();
        String[] str = {"changePic", "changePic", "changeName", "changeNickName", "changePhone", "changeMail", "changePwd", "changeVersions", "login", "install", "coldBoot", "hotBoot", "changeLanguage", "communityLive", "recommendLive", "IMLivechangeName",
                "changeNickName", "changePhone", "changeMail", "changePwd", "changeVersions", "login", "install",
                "coldBoot", "hotBoot", "changeLanguage", "communityLive", "recommendLive", "IMLive"};
        list.addAll(Arrays.asList(str));

        List<String> list1 = new LinkedList<>();
        list1.add("add");
        list1.add("s");
        list1.add("x");
        list1.add("sdsadas");
        list1.add("string");

        boolean b = list.contains("hotBoot") && list1.contains("x");

        System.out.println(b);
    }

    public List<Integer> maxSlidingWindow(int[] nums, int k) {
        return null;
    }
}
