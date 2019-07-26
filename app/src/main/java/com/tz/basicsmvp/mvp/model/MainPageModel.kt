package com.tz.basicsmvp.mvp.model

import com.tz.basicsmvp.rx.scheduler.SchedulerUtils
import com.tz.basicsmvp.mvp.model.bean.MainPageBean
import com.tz.basicsmvp.net.RetrofitFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/18 16:26
 * @Package: com.tz.basicsmvp.mvp.model
 * @Description:
 **/
class MainPageModel {

    fun getHomePageData(id: String): Observable<MainPageBean>{
        return RetrofitFactory.service.getHomePageData(id)
            .subscribeOn(Schedulers.io())
            .compose(SchedulerUtils.ioToMain())
    }

}