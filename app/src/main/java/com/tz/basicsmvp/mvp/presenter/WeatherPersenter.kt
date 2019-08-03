package com.tz.basicsmvp.mvp.presenter

import com.blankj.utilcode.util.LogUtils
import com.tz.basicsmvp.mvp.Contract.WeatherContract
import com.tz.basicsmvp.mvp.base.BasePresenter
import com.tz.basicsmvp.mvp.model.WeatherModel

class WeatherPersenter : BasePresenter<WeatherContract.View>(), WeatherContract.Persenter {


    private val model by lazy { WeatherModel() }

    override fun doSceneGetData(city: String) {
        checkViewAttached()
        mRootView?.showLoading()
        var disposable = model.getWeatherData(city).subscribe({ onNext ->
            mRootView?.let {
                it.dismissLoading()
                onNext?.run {
                    if (status == 0) {
                        it.setData(data = result)
                    } else {
                        it.showNoData()
                    }
                }
            }

        }, { onError ->
            LogUtils.e(onError.message)
            mRootView?.dismissLoading()
            mRootView?.showNetError()
        })
        addSubscription(disposable)
    }

}