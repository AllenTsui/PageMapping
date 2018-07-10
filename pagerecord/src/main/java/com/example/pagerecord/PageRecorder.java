package com.example.pagerecord;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 2018/3/28 0028.
 * <p>
 * PageInit com.xiaoyu.pagerRecord
 */

public class PageRecorder {
    private static List<Mapping> mappings = new ArrayList<>();

    private static void initIfNeed() {
        if (!mappings.isEmpty()) {
            return;
        }
        //反射初始化PagerInit，调用它的init方法
        try {
            Class pagerInitClass = Class.forName("com.example.pagerecord.PageInit");
            Method initMethod = pagerInitClass.getMethod("init");
            initMethod.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void map(Class<?> activity,String value){
        mappings.add(new Mapping(activity, value));
    }

    public static final String getPageName(Class<?> activity) {
        initIfNeed();
        for (int i = 0; i < mappings.size(); i++) {
            if (mappings.get(i).keyEquals(activity)) {
                return mappings.get(i).pageName;
            }
        }
        return "";
    }
}
