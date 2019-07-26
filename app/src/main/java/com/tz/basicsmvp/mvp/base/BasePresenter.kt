package com.tz.basicsmvp.mvp.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
* @ComputerCode: tianzhen
* @Author: TianZhen
* @QQ: 959699751
* @CreateTime: Created on 2019/5/18 14:02
* @Package:
* @Description: 在页面被销毁时，应该停止请求， 这里用CompositeDisposable来管理Disposable，
 * 分析源码，发现实际上在 RxJava2CallAdapterFactory 中 disposable 已经调用了call.cancel，
 * 所以 compositeDisposable.clear() 防止了页面卸载时，网络请求依然持有页面对象造成的 内存泄漏
 **/
open class BasePresenter<T : IBaseView> : IPresenter<T> {

    var mRootView: T? = null
        private set

    private var compositeDisposable = CompositeDisposable()


    override fun attachView(mRootView: T) {
        this.mRootView = mRootView
    }

    override fun detachView() {
        mRootView = null

         //保证activity结束时取消所有正在执行的订阅
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }

    }

    private val isViewAttached: Boolean
        get() = mRootView != null

    fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private class MvpViewNotAttachedException internal constructor() : RuntimeException("Please call IPresenter.attachView(IBaseView) before" + " requesting data to the IPresenter")

}