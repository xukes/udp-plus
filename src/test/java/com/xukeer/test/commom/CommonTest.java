package com.xukeer.test.commom;

import org.junit.Test;

import java.lang.annotation.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @Author xqw
 * @Description
 * @Date 9:26 2021/1/12
 **/
public class CommonTest {


    @Test
    public void test() throws Exception {
        String sql = "select * from order where createdUser = ${curr_e-ntU1ser} and  depart = ${currentOrg}, ${curraentOrg}, ${curre2ntOrg}, ${current3Org}, ${current4Org}";

        SQLStatement sqlStatement = new SQLStatement(sql);


        UserDao userDao =new UserDao();
        int p = userDao.selectAllUser(1);
        int p2 = userDao.selectAllUser(2);

        Object o = sqlStatement.executeSql();
        System.out.println(o);

    }





    public static class UserDao {
        @ISql("")
        private int selectAllUser(@IParam("ar") Integer ar) {
            return -1;
        }
    }




    public static class SQLStatement {
        private Map<String, Object> map = new LinkedHashMap<>();
        private String sql;

        public SQLStatement(String sql) throws Exception {
            init(sql);
        }
        private void init(String sql) throws Exception {
            this.sql = ParamParseFactory.parse(sql, paramName -> {
                if (map.containsKey(paramName)) {
                    throw new Exception();
                }
                map.put(paramName, null);
            });
        }

        public Object executeSql() {
            System.out.println(sql);
            map.forEach((k,v)->{
                System.out.println(k);
                System.out.println("----------");
            });
            return null;
        }

    }

    public static class ParamParseFactory {
        public static final Pattern PARAM_PATTERN = Pattern.compile("\\$\\{[-_\\da-zA-Z]+}");

        public static String parse(String sql, ParamFoundHandler paramFoundHandler) throws Exception {
            Matcher matcher = PARAM_PATTERN.matcher(sql);
            while (matcher.find()) {
                sql = sql.replaceFirst(PARAM_PATTERN.pattern(), "?");
                paramFoundHandler.foundParam(matcher.group());
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
