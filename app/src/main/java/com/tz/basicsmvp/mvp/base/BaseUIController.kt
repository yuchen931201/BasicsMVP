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
import com.tz.basicsmvp.mvp.base.BaseActivity.Companion.TYPE_TITLE_MAIN
import com.tz.basicsmvp.mvp.base.BaseActivity.Companion.TYPE_TITLE_NORMAL
import com.tz.basicsmvp.mvp.view.widget.MultipleStatusView
import com.tz.basicsmvp.utils.core.strategy.loadImage
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

    private var iv_left: ImageView? =null
    private var iv_right: ImageView? =null
    private var line_view: View? =null
    private var title_bar: View? =null
    private var tv_title: TextView? =null
    private var tool_bar: Toolbar? =null

    override fun getRootView() :View{
        val inflater = LayoutInflater.from(activity)
        val v: View
        when (activity.layoutType()) {
            TYPE_TITLE_NORMAL -> {
                v = inflater.inflate(R.layout.root_title_view, null)
                iv_left = v.findViewById(R.id.iv_left)
                loadImage(R.drawable.ic_left_black,iv_left)
                title_bar = v.findViewById(R.id.title_bar)
                iv_right = v.findViewById(R.id.iv_right)
                tv_title = v.findViewById(R.id.tv_title)
                tool_bar = v.findViewById(R.id.tool_bar)
                iv_left?.setOnClickListener {
                    activity.finish()
                }
            }
            TYPE_FULL_SCREEN -> {
                v = inflater.inflate(R.layout.root_full_screen_view, null)
            }
            TYPE_TITLE_MAIN ->{
                v = inflater.inflate(R.layout.root_title_view, null)
                iv_left = v.findViewById(R.id.iv_left)
                loadImage(R.drawable.ic_home_black_24dp,iv_left)
                iv_right = v.findViewById(R.id.iv_right)
                loadImage(R.drawable.ic_dehaze_black_24dp,iv_right)
                title_bar = v.findViewById(R.id.title_bar)
                line_view = v.findViewById(R.id.line_view)
                tv_title = v.findViewById(R.id.tv_title)
                tv_title?.text = "主页"
                tool_bar = v.findViewById(R.id.tool_bar)
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


    override fun setTitle(s: String){
        tv_title?.text = s
    }

    override fun setLeftImage(any: Any){
        loadImage(any,iv_left)
    }

    override fun setLeftClick(click: View.OnClickListener){
        iv_left?.setOnClickListener(click)
    }

    override fun setRightImage(any: Any){
        loadImage(any,iv_right)
    }

    override fun setRightClick(click: View.OnClickListener){
        iv_right?.setOnClickListener(click)
    }

    override fun setLineColor(color: Int){
        line_view?.setBackgroundColor(color)
    }

    override fun setToolbarColor(color: Int) {
        tool_bar?.setBackgroundColor(color)
    }

    override fun setTitleBarColor(color: Int) {
        title_bar?.setBackgroundColor(color)
    }

    override fun setStatusBarFontDark(dark: Boolean) {
        try {
            StatusBarUtil.setStatusBarDarkTheme(activity,dark)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun getStatusBarHeight(): Int {
        var statusBarHeight = 0
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = activity.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
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