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
 * @Description:
 **/
interface MainContract {

    interface View : IBaseView{

        fun setMainPageData(mpb: MainPageBean)
        fun showNoData()
        fun showNetError()
    }

    interface Presenter: IPresenter<View>{

        fun doSceneGetData(id: String)

        fun doSceneLoadMore(page: Int)
    }

}