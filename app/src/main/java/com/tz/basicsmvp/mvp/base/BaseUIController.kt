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