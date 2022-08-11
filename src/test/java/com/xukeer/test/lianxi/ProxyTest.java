package com.xukeer.test.lianxi;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/*
 * @author xqw
 * @description
 * @date 9:32 2021/12/6
 **/
public class ProxyTest {
    public static void main(String[] args) throws Throwable {

        InvocationHandler invocationHandler = (o, method, objects) -> method.invoke(o,objects);
        ProTest proTest = new ProTest();
        Class<ProTest> proTestClass= ProTest.class;
        Method method = proTestClass.getMethod("hello", String.class);
        Object[] objects ={"2132"};

        System.out.println(1);
        invocationHandler.invoke(proTest, method, objects);
        System.out.println(2);

    }
}
