package com.tz.basicsmvp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import com.billy.android.swipe.SmartSwipeBack
import com.blankj.utilcode.util.LogUtils
import com.tz.basicsmvp.mvp.view.activity.MainActivity
import com.tz.basicsmvp.utils.core.ImageGo
import com.tz.basicsmvp.utils.core.strategy.ImageOptions
import com.tz.basicsmvp.utils.glide.GlideImageStrategy
import com.wanjian.sak.SAK
import com.wanjian.sak.config.Config
import kotlin.properties.Delegates

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/17 17:48
 * @Package: com.tz.basicsmvp
 * @Description:
 **/
class MyApp : Application() {

    var currentActivity: Activity? = null


    companion object {
        //late init or
        var context: Context by Delegates.notNull()
            private set

        @SuppressLint("StaticFieldLeak")
        var instance: MyApp? = null

        @JvmStatic
        val stackMap: LinkedHashMap<Int, Activity> by lazy { LinkedHashMap<Int, Activity>() }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        LogUtils.getConfig().isLogSwitch = BuildConfig.DEBUG
        this.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        initGreenDao()
        initSwipe()
        initGlide()
        initTestTools()
    }

    fun initTestTools(){
        SAK.init( this, Config.Build(this).build())
        //SAK.unInstall()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        MultiDex.install(this)
    }

    fun initGreenDao(){
//        DaoMaster.
//        CC.enableDebug(true)
    }

    fun initSwipe() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SmartSwipeBack.activityBezierBack(this,
                SmartSwipeBack.ActivitySwipeBackFilter{ return@ActivitySwipeBackFilter (it !is MainActivity) })
        } else {
            SmartSwipeBack.activitySlidingBack(this,
                SmartSwipeBack.ActivitySwipeBackFilter { return@ActivitySwipeBackFilter it !is MainActivity })
        }
    }

    fun initGlide(){
        ImageGo
            .setDebug(BuildConfig.DEBUG)
            .setStrategy(GlideImageStrategy())  // 图片加载策略
            .setDefaultBuilder(ImageOptions.Builder())  // 图片加载配置属性，可使用默认属性
    }

    private val mActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            activity?.run {
                currentActivity = activity
                stackMap[activity.hashCode()] = activity
            }
            LogUtils.i("onActivityCreated: ", activity?.componentName?.className)
        }

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity?) {
            activity?.run {
                if (stackMap.containsKey(activity.hashCode())) {
                    stackMap.remove(activity.hashCode())
                }
            }
            LogUtils.i("onActivityDestroyed: ", activity?.javaClass?.simpleName)
        }
    }
}