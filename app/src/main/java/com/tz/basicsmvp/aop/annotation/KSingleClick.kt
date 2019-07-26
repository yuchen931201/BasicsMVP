package com.tz.basicsmvp.aop.annotation

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/26 18:03
 * @Package: cn.eclicks.drivingtest.annotation
 * @Description:
 **/
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class KSingleClick(val value: Long = 500//默认的点击间隔时间
)