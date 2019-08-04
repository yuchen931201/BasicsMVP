package com.tz.basicsmvp.mvp.view.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.tz.basicsmvp.R
import com.tz.basicsmvp.mvp.Contract.WeatherContract
import com.tz.basicsmvp.mvp.base.BaseActivity
import com.tz.basicsmvp.mvp.model.bean.WeatherBean
import com.tz.basicsmvp.mvp.presenter.WeatherPersenter
import kotlinx.android.synthetic.main.activity_weather.*
import com.yanzhenjie.permission.Action

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/20 17:23
 * @Package: com.tz.basicsmvp.mvp.view.activity
 * @Description:
 **/
class WeatherActivity : BaseActivity(), WeatherContract.View {


    val mPersenter by lazy { WeatherPersenter() }

    init {
        mPersenter.attachView(this)
    }

    companion object {

        fun enter(ctx: Context) {
            val i = Intent(ctx, WeatherActivity::class.java)
            ctx.startActivity(i)
        }

    }

    override fun layoutType(): Int {
        return TYPE_TITLE_NORMAL
    }

    override fun layoutId(): Int { return R.layout.activity_weather }

    override fun onFinishCreateView() {
        setTitle("天气")
        doScene()
        //KLogChooseActivity.enterForResult()
    }

    override fun doScene() {
        mPersenter.doSceneGetData("上海")
    }

    override fun setData(data: WeatherBean) {
        getStatusView()?.showContent()
        tv_content.text = GsonUtils.toJson(data)
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun showNoData() {
        getStatusView()?.showEmpty()
    }

    override fun showNetError() {
        getStatusView()?.showEmpty()
    }

    fun onclick() {
        requestPermissionForResult(Action { strings ->
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
        }, Manifest.permission.CALL_PHONE)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPersenter.detachView()
    }


}