//package com.tz.basicsmvp.aop.pointcut
//
//import android.view.View
//import com.tz.basicsmvp.aop.annotation.KSingleClick
//import org.aspectj.lang.ProceedingJoinPoint
//import org.aspectj.lang.annotation.Around
//import org.aspectj.lang.annotation.Aspect
//import org.aspectj.lang.annotation.Pointcut
//import org.aspectj.lang.reflect.MethodSignature
//
///**
// * @ComputerCode: tianzhen
// * @Author: TianZhen
// * @QQ: 959699751
// * @CreateTime: Created on 2019/4/26 17:44
// * @Package: cn.eclicks.drivingtest.pointcut
// * @Description:
// **/
//@Aspect
//class AspectJSingleKClick {
//    /**
//     * 最近一次点击的时间
//     */
//    private var mLastClickTime: Long = 0
//    /**
//     * 最近一次点击的控件ID
//     */
//    private var mLastClickViewId: Int = 0
//
//    @Pointcut("execution(@cn.eclicks.drivingtest.annotation.KSingleClick * *(..))")
//    fun methodAnnotated() {
//    }
//
//    @Around("methodAnnotated()")
//    @Throws(Throwable::class)
//    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint) {
//        // 取出方法的参数
//        var view: View? = null
//        for (arg in joinPoint.args) {
//            if (arg is View) {
//                view = arg
//                break
//            }
//        }
//        if (view == null) {
//            return
//        }
//        // 取出方法的注解
//        val methodSignature = joinPoint.signature as MethodSignature
//        val method = methodSignature.method
//        if (!method.isAnnotationPresent(KSingleClick::class.java)) {
//            return
//        }
//        val singleClick = method.getAnnotation(KSingleClick::class.java)
//        // 判断是否快速点击
//        if (!isFastDoubleClick(view, singleClick.value)) {
//            // 不是快速点击，执行原方法
//            joinPoint.proceed()
//        }
//    }
//
//    /**
//     * 是否是快速点击
//     * @param v  点击的控件
//     * @param intervalMillis  时间间期（毫秒）
//     * @return  true:是，false:不是
//     */
//    fun isFastDoubleClick(v: View, intervalMillis: Long): Boolean {
//        val viewId = v.id
//        val time = System.currentTimeMillis()
//        val timeInterval = Math.abs(time - mLastClickTime)
//        if (timeInterval < intervalMillis && viewId == mLastClickViewId) {
//            return true
//        } else {
//            mLastClickTime = time
//            mLastClickViewId = viewId
//            return false
//        }
//    }
//}