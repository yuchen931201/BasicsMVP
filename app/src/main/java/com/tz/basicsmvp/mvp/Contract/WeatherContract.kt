package com.tz.basicsmvp.mvp.Contract

import com.tz.basicsmvp.mvp.base.IBaseView
import com.tz.basicsmvp.mvp.base.IPresenter
import com.tz.basicsmvp.mvp.model.bean.WeatherBean


/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 23:59 2019-08-20
 * @Package:
 * @Description: 模型可重用
 **/
interface WeatherContract {

    interface View : IBaseView {
        fun setData(data: WeatherBean)
    }

    interface Persenter : IPresenter<View> {
        fun doSceneWeatherData(city: String)
    }

}