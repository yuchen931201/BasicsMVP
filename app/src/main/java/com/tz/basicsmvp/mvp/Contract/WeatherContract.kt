package com.tz.basicsmvp.mvp.Contract

import com.tz.basicsmvp.mvp.base.IBaseView
import com.tz.basicsmvp.mvp.base.IPresenter
import com.tz.basicsmvp.mvp.model.bean.WeatherBean

interface WeatherContract {

    interface View : IBaseView {
        fun setData(data: WeatherBean)
    }

    interface Persenter : IPresenter<View> {
        fun doSceneGetData(city: String)
    }

}