package com.xukeer.test.commom;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * @Author xqw
 * @Description
 * @Date 15:54 2021/1/15
 **/
public class BaseInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        int index = 0;
        CommonTest.ISql iSql = method.getAnnotation(CommonTest.ISql.class);
        if (iSql == null) {return null;}

        CommonTest.SQLStatement sqlStatement = new CommonTest.SQLStatement(iSql.value());

        int  paramSize = sqlStatement.getParam().size();
        Object[] paramObject = new Object[paramSize];
        String[] paramNameArr =new String[paramSize];
        sqlStatement.getParam().toArray(paramNameArr);

        Parameter[] parameterArr = method.getParameters();
        Map<String,Integer> paramIndexMap = new HashMap<>();
        for (int i = 0; i < parameterArr.length; i++) {
            Parameter parameter = parameterArr[i];
            String param = parameter.getAnnotation(CommonTest.IParam.class).value();
            paramIndexMap.put(param, i);
        }

        //System.out.println(sqlStatement.getSql());
        for(String param : paramNameArr) {
            if(param.contains(".")) {
                String[] as = param.split("\\.");
                Object obj =  args[paramIndexMap.get(as[0])];
                Field field = obj.getClass().getDeclaredField(as[1]);
                field.setAccessible(true);
                paramObject[index]= field.get(obj);

            } else{
                paramObject[index]= args[paramIndexMap.get(param)];
            }
            index++;
        }

     //   System.out.println(Arrays.toString(paramObject));
        return 100;
    }

}
