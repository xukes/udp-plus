package com.xukeer.test.commom;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @Author xqw
 * @Description
 * @Date 9:26 2021/1/12
 **/
public class CommonTest {
    @Test
    public void test(){
        Pattern pattern = Pattern.compile("\\$\\{[-_\\da-zA-Z]+}");
        Matcher matcher = pattern.matcher("select * from order where createdUser = ${curr_e-ntU1ser} and  depart = ${currentOrg}");

        List<String> matchStrs = new ArrayList<>();

        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            matchStrs.add(matcher.group());//获取当前匹配的值
        }

        matchStrs.forEach(System.out::println);
    }

}
