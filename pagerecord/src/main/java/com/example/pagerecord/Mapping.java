package com.example.pagerecord;

/**
 * Created by xiaoyu on 2018/3/28 0028.
 * <p>
 * key,value的一个包装类
 */

public class Mapping {
    public Class<?> activity;
    public String pageName;

    public Mapping(Class<?> activity, String pageName) {
        this.activity = activity;
        this.pageName = pageName;
    }

    public boolean keyEquals(Class<?> activity) {
        return activity.getCanonicalName().equals(this.activity.getCanonicalName());
    }
}
