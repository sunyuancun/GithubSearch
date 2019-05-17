package com.syc.githubsearch.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/5/17.
 */

public class MapUtils {

    //根据最大value寻找key
    public static ArrayList<String> getMaxKeys(HashMap<String,Integer> map,Integer value){
        ArrayList<String> keys = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if(value == entry.getValue()){
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    //遍历寻找最大value
    public static Object gerMaxValue(HashMap<String, Integer> map) {
        if (map.size() == 0)
            return null;
        Collection<Integer> c = map.values();
        Object[] obj = (Object[]) c.toArray();
        Arrays.sort(obj);
        return obj[obj.length - 1];
    }


}
