package com.tz.basicsmvp.mvp.presenter

import com.blankj.utilcode.util.LogUtils
import com.tz.basicsmvp.mvp.Contract.MainContract
import com.tz.basicsmvp.mvp.base.BasePresenter
import com.tz.basicsmvp.mvp.model.MainPageModel

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/18 16:38
 * @Package: com.tz.basicsmvp.mvp.presenter
 * @Description:
 **/
class MainPagePersenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    private val mainPageModel by lazy {
        MainPageModel()
    }

    override fun doSceneGetData(id: String) {
        checkViewAttached()
        val disposable = mainPageModel.getHomePageData(id)
            .subscribe({ mainBean ->
                mRootView?.apply {
                    setMainPageData(mainBean)
                }
            }, { onError ->
                LogUtils.e(onError.message)
                mRootView?.apply {
                    showNetError()
                }
            })
        //切记要添加管理队列
        addSubscription(disposable)
    }

    override fun doSceneLoadMore(page: Int) {
        //TODO do everything
    }

}