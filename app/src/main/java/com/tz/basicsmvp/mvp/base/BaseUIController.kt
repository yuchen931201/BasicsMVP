package com.tz.basicsmvp.mvp.base

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class BaseUIController <T> constructor(t: T): IBaseUIController where T : Activity, T : IBaseActivity{

    private val activity: T by lazy { t }
    private var statusView: MultipleStatusView? = null

    override fun initActivity(){
        val inflater = LayoutInflater.from(activity)
        val v: View = when (activity.layoutType()) {
            TYPE_TITLE_NORMAL -> {
                inflater.inflate(R.layout.base_root_view, null)
            }
            TYPE_FULL_SCREEN ->{
                inflater.inflate(R.layout.base_root_view, null)
            }
            else -> {
                inflater.inflate(R.layout.base_root_view, null)
            }
        }
        statusView = v.findViewById(R.id.status_view)
        val rootView : ViewGroup = v.findViewById(R.id.root_view)
        rootView.addView(inflater.inflate(activity.layoutId(),null))
        activity.setContentView(v)
        activity.onFinishCreateView()
        activity.initData()
        initListener()
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