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
 * @Description: Persenter 负责逻辑的处理
 **/
class MainPagePersenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    private val mainPageModel by lazy { MainPageModel() }

    override fun doSceneGetData(city: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = mainPageModel.getHomePageData(city)
            .subscribe({ jsonBean ->
                mRootView?.let {
                    it.dismissLoading()
                    when {
                        jsonBean.status == 0 -> it.setMainPageData(jsonBean.result)
                        jsonBean.status == -1 -> it.showNoData()
                        else -> {//其它错误
                            it.showNoData()
                        }
                    }
                }
            }, { onError ->
                LogUtils.e(onError.message)
                mRootView?.let {
                    it.dismissLoading()
                    it.showNetError()
                }
            })
        //切记要添加管理队列
        addSubscription(disposable)
    }

    override fun doSceneLoadMore(page: Int) {
        //TODO do everything
        //doSceneGetData()
    }

}