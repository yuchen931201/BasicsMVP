package com.tz.basicsmvp.aop.pointcut;

import android.view.View;
import com.blankj.utilcode.util.LogUtils;
import com.tz.basicsmvp.aop.annotation.SingleClick;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/26 18:14
 * @Package: cn.eclicks.drivingtest.pointcut
 * @Description:
 **/
@Aspect
public class AspectJSingleClick {
    /**
     * 最近一次点击的时间
     */
    private static long mLastClickTime;
    /**
     * 最近一次点击的控件ID
     */
    private static int mLastClickViewId;

    @Pointcut("execution(@cn.eclicks.drivingtest.annotation.SingleClick * *(..))")
    public void pointcut(){

    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint){

    }

    @Around("pointcut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 取出方法的注解
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if (!method.isAnnotationPresent(SingleClick.class)) {
            return;
        }
        SingleClick singleClick = method.getAnnotation(SingleClick.class);
        if(singleClick.needView()){
            // 取出方法的参数
            View view = null;
            LogUtils.i("");
            for (Object arg : joinPoint.getArgs()) {
                if (arg instanceof View) {
                    view = (View) arg;
                    break;
                }
            }
            if (view == null) {
                return;
            }
            // 判断是否快速点击
            if (!isFastDoubleClick(view, singleClick.value())) {
                // 不是快速点击，执行原方法
                joinPoint.proceed();
            }
        }else{
            if (!isFastDoubleClick(singleClick.value())) {
                joinPoint.proceed();
            }
        }
    }

    @After("pointcut()")
    public void after(JoinPoint point) {

    }

    @AfterReturning("pointcut()")
    public void afterReturning(JoinPoint point, Object returnValue) {

    }

    @AfterThrowing(value = "pointcut()", throwing = "ex")
    public void afterThrowing(Throwable ex) {

    }
    /**
     * 是否是快速点击
     * @param v  点击的控件
     * @param intervalMillis  时间间期（毫秒）
     * @return  true:是，false:不是
     */
    public boolean isFastDoubleClick(View v, long intervalMillis) {
        int viewId = v.getId();
        long time = System.currentTimeMillis();
        long timeInterval = Math.abs(time - mLastClickTime);
        if (timeInterval < intervalMillis && viewId == mLastClickViewId) {
            return true;
        } else {
            mLastClickTime = time;
            mLastClickViewId = viewId;
            return false;
        }
    }

    /**
     * 是否是快速点击
     * @param intervalMillis  时间间期（毫秒）
     * @return  true:是，false:不是
     */
    public boolean isFastDoubleClick(long intervalMillis) {
        long time = System.currentTimeMillis();
        long timeInterval = Math.abs(time - mLastClickTime);
        if (timeInterval < intervalMillis ) {
            return true;
        } else {
            mLastClickTime = time;
            return false;
        }
    }
}
