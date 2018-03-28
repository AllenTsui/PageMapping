package com.example.pagerecord;

import android.app.Activity;

/**
 * Created by xiaoyu on 2018/3/28 0028.
 * <p>
 * key,value的一个包装类
 */

public class Mapping {
    public Class<? extends Activity> activity;
    public String pageName;

    public Mapping(Class<? extends Activity> activity, String pageName) {
        this.activity = activity;
        this.pageName = pageName;
    }

    public boolean keyEquals(Class<? extends Activity> activity) {
        return activity.getCanonicalName().equals(this.activity.getCanonicalName());
    }
}
