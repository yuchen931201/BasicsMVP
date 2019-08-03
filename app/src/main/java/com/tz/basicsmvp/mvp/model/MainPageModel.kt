package com.tz.basicsmvp.mvp.model

import com.tz.basicsmvp.mvp.base.BaseJsonBean
import com.tz.basicsmvp.mvp.model.bean.MainPageBean
import com.tz.basicsmvp.net.RetrofitFactory
import com.tz.basicsmvp.rx.scheduler.SchedulerUtils
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/18 16:26
 * @Package: com.tz.basicsmvp.mvp.model
 * @Description: Model 负责数据模型的处理
 **/
class MainPageModel {

    fun getHomePageData(city: String): Observable<BaseJsonBean<MainPageBean>> {
        return RetrofitFactory.service.getMainData(city)
            .subscribeOn(Schedulers.io())
            .compose(SchedulerUtils.ioToMain())
    }

}