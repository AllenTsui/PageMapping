package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xiaoyu on 2018/3/28 0028.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface PageModules {
    String[] value();
}
