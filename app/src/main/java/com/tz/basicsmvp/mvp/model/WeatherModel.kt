package com.tz.basicsmvp.mvp.model

import com.tz.basicsmvp.mvp.base.BaseJsonBean
import com.tz.basicsmvp.mvp.model.bean.WeatherBean
import com.tz.basicsmvp.net.RetrofitFactory
import com.tz.basicsmvp.rx.scheduler.SchedulerUtils
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


class WeatherModel {

    fun getWeatherData(city: String): Observable<BaseJsonBean<WeatherBean>> {
        return RetrofitFactory.service.getWeatherData(city)
            .subscribeOn(Schedulers.io())
            .compose(SchedulerUtils.ioToMain())
    }
}