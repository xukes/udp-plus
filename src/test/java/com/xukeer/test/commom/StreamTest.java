package com.xukeer.test.commom;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * @Author xqw
 * @Description
 * @Date 15:09 2021/4/6
 **/
public class StreamTest {

    @Test
    public void test1() {
        List<String> list = Arrays.asList("test", "origin", "start", "start");

        System.out.println(list.stream().collect(Collectors.toSet()).size());
        //System.out.println();
    }
}
