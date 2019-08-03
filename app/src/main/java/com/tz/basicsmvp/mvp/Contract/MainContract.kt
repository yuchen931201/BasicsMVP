package com.tz.basicsmvp.mvp.Contract

import com.tz.basicsmvp.mvp.base.IBaseView
import com.tz.basicsmvp.mvp.base.IPresenter
import com.tz.basicsmvp.mvp.model.bean.MainPageBean

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/18 14:49
 * @Package: com.tz.basicsmvp.mvp.Contract
 * @Description: 契约 , 用来定义Presenter中要做的场景内容例如联网, 同时也是用来定义 Activity View 中要做的UI展示
 **/
interface MainContract {

    interface View : IBaseView{
        fun setMainPageData(mpb: MainPageBean)
    }

    interface Presenter: IPresenter<View>{

        fun doSceneGetData(id: String)

        fun doSceneLoadMore(page: Int)
    }

}