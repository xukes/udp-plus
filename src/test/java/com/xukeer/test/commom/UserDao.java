package com.xukeer.test.commom;

import java.util.List;
import java.util.Map;

/*
 * @Author xqw
 * @Description
 * @Date 14:14 2021/1/15
 **/
public interface UserDao {
    @CommonTest.ISql("select * from order where createdUser = ${age} and  depart = ${name} and su=#{user.age} and s = #{user.name} and s = #{user.name} <test if te!=null></test>"  )
    int selectAllUser(@CommonTest.IParam("age") Integer age, @CommonTest.IParam("name") String name, @CommonTest.IParam("user") UserInfo user);

    @CommonTest.ISql("select app_name appName, app_ename appEname from developer_my_app where app_name = #{user.name}")
    List<Map<String, Object>> selectUser(@CommonTest.IParam("age") Integer age, @CommonTest.IParam("user") UserInfo user);
}
