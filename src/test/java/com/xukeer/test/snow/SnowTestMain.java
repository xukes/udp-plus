package com.xukeer.test.snow;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xqw
 * @description
 * @date 14:36 2022/7/20
 **/
public class SnowTestMain {

    @Test
    public void main() {
        Set<Long> set = new HashSet<>();
        for (int i=0;i<2000000;i++){
//            long id = (long) (Math.random()*Long.MAX_VALUE);
            long id = Snowflake.nextId();
            set.add(id);
        }
        // 2 000 000
        System.out.println(set.size());
    }
}
