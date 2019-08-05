package com.tz.basicsmvp.mvp.base

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.tz.basicsmvp.R
import com.tz.basicsmvp.mvp.base.BaseActivity.Companion.TYPE_FULL_SCREEN
import com.tz.basicsmvp.mvp.base.BaseActivity.Companion.TYPE_TITLE_NORMAL
import com.tz.basicsmvp.mvp.view.custom.MultipleStatusView
import com.tz.basicsmvp.utils.statusBar.StatusBarUtil

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/8/2 16:30
 * @Package: com.tz.basicsmvp.mvp.base
 * @Description:
 **/
class BaseUIController<T> constructor(t: T) : IBaseUIController where T : Activity, T : IBaseActivity {

    private val activity: T by lazy { t }
    private var statusView: MultipleStatusView? = null

    private var iv_left_back: ImageView? =null
    private var tv_title: TextView? =null
    private var tool_bar: Toolbar? =null

    override fun getRootView() :View{
        val inflater = LayoutInflater.from(activity)
        val v: View
        when (activity.layoutType()) {
            TYPE_TITLE_NORMAL -> {
                v = inflater.inflate(R.layout.root_title_view, null)
                iv_left_back = v.findViewById(R.id.iv_left_back)
                tv_title = v.findViewById(R.id.tv_title)
                tool_bar = v.findViewById(R.id.tool_bar)
                iv_left_back?.setOnClickListener {
                    activity.finish()
                }
            }
            TYPE_FULL_SCREEN -> {
                v = inflater.inflate(R.layout.root_full_screen_view, null)
            }
            else -> {
                v = inflater.inflate(R.layout.root_title_view, null)
            }
        }
        statusView = v.findViewById(R.id.status_view)
        val rootView: ViewGroup = v.findViewById(R.id.root_view)
        rootView.addView(inflater.inflate(activity.layoutId(), null).apply {
            layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        })
        initListener()
        return v
    }

    override fun setStatusBarFontDark(dark: Boolean) {
        try {
            StatusBarUtil.setStatusBarDarkTheme(activity,dark)
        }catch (e: Exception){
            e.printStackTrace()
        }

//        // 小米MIUI
//        try {
//            val window = activity.window
//            val clazz = activity.window.javaClass
//            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
//            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
//            val darkModeFlag = field.getInt(layoutParams)
//            val extraFlagField =
//                clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
//            if (dark) {    //状态栏亮色且黑色字体
//                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
//            } else {       //清除黑色字体
//                extraFlagField.invoke(window, 0, darkModeFlag)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        // 魅族FlymeUI
//        try {
//            val window = activity.window
//            val lp = window.attributes
//            val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
//            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
//            darkFlag.isAccessible = true
//            meizuFlags.isAccessible = true
//            val bit = darkFlag.getInt(null)
//            var value = meizuFlags.getInt(lp)
//            if (dark) {
//                value = value or bit
//            } else {
//                value = value and bit.inv()
//            }
//            meizuFlags.setInt(lp, value)
//            window.attributes = lp
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        // android6.0+系统
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (dark) {
//                activity.window.decorView.systemUiVisibility =
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            }
//        }
    }

    override fun getStatusBarHeight(): Int {
        var statusBarHeight = 0
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = activity.resources.getDimensionPixelSize(resourceId)
        }
        //LogUtils.d("CompatToolbar", "状态栏高度：" + px2dp(statusBarHeight.toFloat()) + "dp")
        return statusBarHeight
    }

    override fun setTitle(s: String){
        tv_title?.text = s
    }

    override fun getToolbar(): Toolbar?{
        return tool_bar
    }

    override fun getStatusView(): MultipleStatusView? {
        return statusView
    }

    private fun initListener() {
        getStatusView()?.setOnClickListener(mRetryClickListener)//MultipleStatusView多种状态的 View 的切换
    }

    private val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        activity.doScene()
    }

    override fun onDestroy() {
        // 做一些回收的操作
    }

}