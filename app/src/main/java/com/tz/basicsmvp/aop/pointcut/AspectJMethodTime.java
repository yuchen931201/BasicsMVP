package com.tz.basicsmvp.aop.pointcut;

import android.os.SystemClock;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @ComputerCode: YD-YF-2015083113-1
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/2/15 15:18
 * @Package: com.example.tianzhen.myapplication.annotation
 * @Description:  execution(<@注解类型模式>? <修饰符模式>? <返回类型模式> <方法名模式>(<参数模式>) <异常模式>?)
 * "execution(int com.lqr.*(..))" 匹配com.lqr包下所有返回类型是int的方法
 * "execution(* com.lqr..*to(..))" 匹配com.lqr包下及其子包中以"to"结尾的方法
 **/
@Aspect
public class AspectJMethodTime {

    /**
     * @descreption measure method time
     * */
    @Pointcut("execution(@com.example.tianzhen.myapplication.annotation.MethodTime * *(..))")
    public void pointcut(){

    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        System.out.println("@Before");
    }

    @Around("pointcut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("@Around");
        long beginTime = SystemClock.currentThreadTimeMillis();
        joinPoint.proceed();
        long endTime = SystemClock.currentThreadTimeMillis();
        long dx = endTime - beginTime;
        System.out.println("耗时：" + dx + "ms");
    }

    @After("pointcut()")
    public void after(JoinPoint point) {
        System.out.println("@After");
    }

    @AfterReturning("pointcut()")
    public void afterReturning(JoinPoint point, Object returnValue) {
        System.out.println("@AfterReturning");
    }

    @AfterThrowing(value = "pointcut()", throwing = "ex")
    public void afterThrowing(Throwable ex) {
        System.out.println("@AfterThrowing");
    }

    private void getParamsValue(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String name = signature.getName(); // 方法名：getParamsValue
        Method method = signature.getMethod(); // 方法：public void com.example.tianzhen.myapplication.annotation.test(org.aspectj.lang.JoinPoint)
        Class returnType = signature.getReturnType(); // 返回值类型：void
        Class declaringType = signature.getDeclaringType(); // 方法所在类名：MainActivity
        String[] parameterNames = signature.getParameterNames(); // 参数名：JoinPoint
        Class[] parameterTypes = signature.getParameterTypes(); // 参数类型：JoinPoint
    }
}
