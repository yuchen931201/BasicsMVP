package com.tz.basicsmvp.mvp.view.activity

import android.Manifest
import android.annotation.SuppressLint
import com.blankj.utilcode.util.GsonUtils
import com.tz.basicsmvp.R
import com.tz.basicsmvp.mvp.Contract.MainContract
import com.tz.basicsmvp.mvp.base.BaseActivity
import com.tz.basicsmvp.mvp.model.bean.MainPageBean
import com.tz.basicsmvp.mvp.presenter.MainPagePersenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainContract.View{


    private val mPresenter by lazy { MainPagePersenter() }

    init {
        mPresenter.attachView(this)
    }

    override fun layoutType(): Int {
        return TYPE_FULL_SCREEN
    }

    override fun layoutId(): Int { return R.layout.activity_main }

    override fun onFinishCreateView() {
//        text_view_s.text = "我是测试文字，静态设置成功"
        //getStatusView()?.showEmpty()
        text_view_s.setOnClickListener {
            WeatherActivity.enter(this)
        }
        tv_status_bar.setOnClickListener {
            StatusBarActivity.enter(this)
        }
    }

    override fun doScene() {
//        mPresenter.doSceneGetData("上海")
    }

    @SuppressLint("SetTextI18n")
    override fun setMainPageData(mpb: MainPageBean) {
//        getStatusView()?.showContent()
//        text_view_s.text = "我是服务器返回文字，动态设置成功\n${GsonUtils.toJson(mpb)}"

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
