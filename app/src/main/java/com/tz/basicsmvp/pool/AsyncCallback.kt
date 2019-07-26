package cn.eclicks.drivingtest.pool

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/19 18:36
 * @Package: com.tz.autolayout.pool
 * @Description:
 **/
interface AsyncCallback<T> {

    fun onSuccess(t:T)

    fun onFailed(throwable : Throwable)
}