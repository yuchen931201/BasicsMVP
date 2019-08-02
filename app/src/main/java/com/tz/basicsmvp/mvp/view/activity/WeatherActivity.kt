package com.tz.basicsmvp.mvp.view.activity

import com.tz.basicsmvp.R
import com.tz.basicsmvp.mvp.base.BaseActivity

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/20 17:23
 * @Package: com.tz.basicsmvp.mvp.view.activity
 * @Description:
 **/
class WeatherActivity : BaseActivity(){
    override fun layoutType(): Int {
        return TYPE_TITLE_NORMAL
    }

    override fun onFinishCreateView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun doScene() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun layoutId(): Int { return R.layout.activity_weather }

    override fun initData() {
//        KLogChooseActivity.enterForResult()
    }



}