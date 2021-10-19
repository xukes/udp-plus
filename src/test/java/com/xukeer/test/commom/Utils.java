package com.xukeer.test.commom;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/*
 * @author xqw
 * @description
 * @date 15:11 2021/9/30
 **/
public class Utils {
    public static List<String> readFile(String path) {
        List<String> list = new LinkedList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
