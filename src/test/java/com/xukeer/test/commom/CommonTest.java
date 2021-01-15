package com.xukeer.test.commom;

import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @Author xqw
 * @Description
 * @Date 9:26 2021/1/12
 **/
public class CommonTest {


    @Test
    public void proxyTest(){
        UserDao userDao = (UserDao) Proxy.newProxyInstance(UserDao.class.getClassLoader(), new Class[]{UserDao.class}, new BaseInvocationHandler());
        UserInfo userInfo = new UserInfo();
        long s = System.currentTimeMillis();
        for(int k=0;k<100;k++) {
            userInfo.setAge(20);
            userInfo.setName("userInfo");
            userDao.selectUser(90, userInfo);
        }
        System.out.println(String.format("%d", System.currentTimeMillis()-s));

    }


    public static class SQLStatement {
        private List<String> map = new LinkedList<>();
        private String sql;

        public SQLStatement(String sql) throws Exception {
            init(sql);
        }
        private void init(String sql) throws Exception {
            this.sql = ParamParseFactory.parse(sql, paramName -> {
                map.add(paramName );
            });
        }

        public String getSql(){
            return this.sql;
        }

        public List<String> getParam(){
            return this.map;
        }
    }


    public static class ParamParseFactory {
        public static final Pattern PARAM_PATTERN = Pattern.compile("[$,#]\\{[-_.\\da-zA-Z]+}");

        public static String parse(String sql, ParamFoundHandler paramFoundHandler) throws Exception {
            Matcher matcher = PARAM_PATTERN.matcher(sql);
            while (matcher.find()) {
                sql = sql.replaceFirst(PARAM_PATTERN.pattern(), "?");
                String param = matcher.group();
                paramFoundHandler.foundParam(param.substring(2,param.length()-1));
            }
            return sql;
        }
    }

    public interface ParamFoundHandler {
        void foundParam(String paramName) throws Exception;
    }
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface IParam {
        String value();
    }


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface ISql {
        String value();
    }
}
