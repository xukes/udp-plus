package com.xukeer.test.lianxi;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xqw
 * @description
 * @date 11:32 2022/7/25
 **/
public class AXTest {
    private static Map<Character, List<Character>> map = new HashMap<>();
    static {
        map.put('a', Stream.of('x').collect(Collectors.toList()));
        map.put('c', Stream.of('x','y').collect(Collectors.toList()));
    }

    public static void main(String[] args) {
        List<Character> aTeam = Stream.of('a','b','c').collect(Collectors.toList());
        List<Character> bTeam = Stream.of('x','y','z').collect(Collectors.toList());
        Character[][] result =  getList(aTeam,bTeam);
        assert result != null;
        for(Character[] arr:result){
            System.out.println(String.format("%s,%s",arr[0],arr[1]));
        }
    }

    public static Character[][] getList(List<Character> aTeam, List<Character> bTeam) {
        int length = aTeam.size();
        List<Character[]> resultList = new LinkedList<>();
        for (Character aT : aTeam) {
            for (int j = 0; j < length; j++) {
                Character bT = bTeam.get(j);
                if (map.containsKey(aT) && map.get(aT).contains(bT)) {
                    continue;
                }
                Character[] characters = {aT, bT};
                resultList.add(characters);
            }
        }
        Character[][] temp = new Character[3][];
        int resultListLength = resultList.size();
        for (int i = 0; i < resultListLength - 2; i++) {
            for (int j = i + 1; j < resultListLength - 1; j++) {
                for (int k = j + 1; k < resultListLength; k++) {
                    temp[0] = resultList.get(i);
                    temp[1] = resultList.get(j);
                    temp[2] = resultList.get(k);
                    if (isRight(temp)) {
                        return temp;
                    }
                }
            }
        }
        return null;
    }

    private static boolean isRight(Character[][] temp) {
        Set<Character> set = new HashSet<>();
        for (Character[] characters : temp) {
            if (set.contains(characters[0]) || set.contains(characters[1])) {
                return false;
            }
            set.add(characters[0]);
            set.add(characters[1]);
        }
        return true;
    }
}
