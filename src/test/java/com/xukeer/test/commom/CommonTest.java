package com.xukeer.test.commom;

// import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @Author xqw
 * @Description
 * @Date 9:26 2021/1/12
 **/
public class CommonTest {
    public final static BaseInvocationHandler baseInvocationHandler = new BaseInvocationHandler();

    //@Test
    public void proxyTest() {
        UserDao userDao = (UserDao) Proxy.newProxyInstance(UserDao.class.getClassLoader(), new Class[]{UserDao.class}, baseInvocationHandler);
        UserInfo userInfo = new UserInfo();
        long s = System.currentTimeMillis();
        userInfo.setAge(20);
        userInfo.setName("test应用");
        for (int i = 0; i < 20; i++) {
            List<Map<String, Object>> mapList = userDao.selectUser(90, userInfo);
        }
        List<Map<String, Object>> mapList = userDao.selectUser(90, userInfo);
        mapList.forEach(m -> {
            m.forEach((key, val) -> {
                System.out.print(String.format("%20s|", key));
            });
            System.out.println();
            m.forEach((key, val) -> {
                System.out.print(String.format("%20s|", val));
            });
        });

        System.out.println();
        System.out.println(String.format("%d", System.currentTimeMillis() - s));
    }


    public static class SQLStatement {
        private List<String> map = new LinkedList<>();
        private String sql;
        private PreparedStatement preparedStatement;

        public SQLStatement(String sql) throws Exception {
            init(sql);
        }
        private void init(String sql) throws Exception {
            Connection connection = DriverManager.getConnection("jdbc:mysql://mysql.sjzx.test:3306/developer-platform","root","huke123456");

            this.sql = ParamParseFactory.parse(sql, paramName -> {
                map.add(paramName );
            });
            System.out.println(this.sql);
            this.preparedStatement = connection.prepareStatement(this.sql);
        }

        public String getSql(){
            return this.sql;
        }
        public PreparedStatement getPreparedStatement() {
            return preparedStatement;
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
