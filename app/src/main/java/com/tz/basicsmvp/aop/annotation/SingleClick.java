package com.tz.basicsmvp.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/26 18:15
 * @Package: cn.eclicks.drivingtest.annotation
 * @Description:
 **/
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface SingleClick {
    long value() default 500;//默认的点击间隔时间
    boolean needView() default true;//是否需要对view进行null判断
}
