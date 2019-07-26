package com.tz.basicsmvp.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ComputerCode: YD-YF-2015083113-1
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/2/15 17:40
 * @Package: cn.eclicks.drivingtest.annotation
 * @Description: AOP
 **/
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
    LoginType type() default LoginType.COMPARE_TO_LOGIN;
    int num() default 0;
    String value() default "";
}
