//package com.tz.basicsmvp.aop.pointcut;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.aspectj.lang.reflect.MethodSignature;
//
//import java.lang.reflect.Method;
//
///**
// * @ComputerCode: YD-YF-2015083113-1
// * @Author: TianZhen
// * @QQ: 959699751
// * @CreateTime: Created on 2019/2/15 17:44
// * @Package: cn.eclicks.drivingtest.pointcut
// * @Description: AOP
// **/
//@Aspect
//public class AspectJLogin {
//
//    @Pointcut("execution(@cn.eclicks.drivingtest.annotation.Login * *(..))")
//    public void pointcut(){
//
//    }
//
//    @Before("pointcut()")
//    public void before(JoinPoint joinPoint){
//
//    }
//
//    @Around("pointcut()")
//    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
////        if(LoginHelper.isLogin()){
//        if(false){
//            joinPoint.proceed();
//        }else{
//            try {
//                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//                Method methodPoint = signature.getMethod(); // 切点注释的方法对象
//                Class declaringType = signature.getDeclaringType(); // 方法所在类名：MainActivity
//                //解析方法上的注解
//                Method[] methods = declaringType.getDeclaredMethods();
//                for(Method method : methods){
//                    if(methodPoint.equals(method)){
////                        boolean methodHasAnno = method.isAnnotationPresent(Login.class);
////                        if(methodHasAnno){
////                            //得到注解
////                            Login methodAnno = method.getAnnotation(Login.class);
////                            //输出注解属性
////                            String desc = methodAnno.value();
////                            LoginType loginType = methodAnno.type();
////                            if(loginType == LoginType.COMPARE_TO_LOGIN){
////                                LoginHelper.ensureLogin(JiaKaoApplication.getAppContext());
////                            }
////                        }
//                    }
//                }
//            } catch (SecurityException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//    @After("pointcut()")
//    public void after(JoinPoint point) {
//
//    }
//
//    @AfterReturning("pointcut()")
//    public void afterReturning(JoinPoint point, Object returnValue) {
//
//    }
//
//    @AfterThrowing(value = "pointcut()", throwing = "ex")
//    public void afterThrowing(Throwable ex) {
//
//    }
//}
