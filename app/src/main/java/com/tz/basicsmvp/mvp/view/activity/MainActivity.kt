package com.tz.basicsmvp.mvp.view.activity

import android.Manifest
import android.annotation.SuppressLint
import com.blankj.utilcode.util.LogUtils
import com.tz.basicsmvp.MyApp
import com.tz.basicsmvp.R
import com.tz.basicsmvp.mvp.Contract.MainContract
import com.tz.basicsmvp.mvp.base.BaseActivity
import com.tz.basicsmvp.mvp.model.bean.MainPageBean
import com.tz.basicsmvp.mvp.presenter.MainPagePersenter
import com.yanzhenjie.permission.Action
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainContract.View{


    private val mPresenter by lazy { MainPagePersenter() }

    init {
        mPresenter.attachView(this)
    }

    override fun layoutId(): Int { return R.layout.activity_main }

    override fun initData() {
        //联网发送请求
        mPresenter.doSceneGetData("123")
    }

    override fun initView() {
        text_view_s.text = "我是测试文字，静态设置成功"
    }

    override fun start() {
    }

    @SuppressLint("SetTextI18n")
    override fun setMainPageData(mpb: MainPageBean) {
        text_view_s.text = "我是服务器返回文字，动态设置成功${mpb.nickName}"
    }


    fun onclick(){
        requestPermissionForResult(Action{ strings->
            strings.run {
                if (isEmpty()) {
                    finish()
                    return@Action
                }
                LogUtils.e("on Denied Permission", "$strings is not OK ")
                for (i in strings.indices) {
                    val ps = strings[i]
                    if (Manifest.permission.CALL_PHONE.equals(ps, ignoreCase = true)) {
                        //电话权限被拒绝
                        //"您未开启拨打电话权限", "去开启", "取消"
                        goSysSetting(100)
                    }
                }
            }
        },Manifest.permission.CALL_PHONE )
    }


    override fun showNoData() {
    }

    override fun showNetError() {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}
