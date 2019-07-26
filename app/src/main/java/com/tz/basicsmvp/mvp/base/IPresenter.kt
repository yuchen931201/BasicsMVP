package com.tz.basicsmvp.mvp.base


/**
* @ComputerCode: tianzhen
* @Author: TianZhen
* @QQ: 959699751
* @CreateTime: Created on 2019/5/18 14:03
* @Package:  com.tz.basicsmvp.mvp.base
* @Description:
**/

interface IPresenter<in V: IBaseView> {

    /*固定试图*/
    fun attachView(mRootView: V)

    /*分离视图*/
    fun detachView()

}
