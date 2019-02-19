package com.quanyan.yhy.libanalysis;

import java.util.HashMap;

/**
 * 一些事件需要的参数常量 Key,Value
 * Created by Jiervs on 2018/6/8.
 */

public class AnArgs {
    public static final String ID = "id";
    public static final String OPERATION_CONTENT = "operationContent";
    public static final String BANNER_ID = "bannerId";
    public static final String URL = "url";
    public static final String TITLE = "title";

    private static AnArgs args;
    public static AnArgs Instance(){
        args = new AnArgs();
        map = new HashMap<>();
        return args;
    }

    private static HashMap<String,String> map ;
    public HashMap<String, String> getMap() {
        return map;
    }

    public AnArgs build(String key, String value){
        map.put(key,value);
        return args;
    }

}
