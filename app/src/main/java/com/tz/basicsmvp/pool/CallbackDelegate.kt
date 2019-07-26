package cn.eclicks.drivingtest.pool

import java.util.concurrent.Executor

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/19 19:29
 * @Package: com.tz.autolayout.pool
 * @Description:
 **/
internal class CallbackDelegate<T> (private val callback: Callback?,
                           private val deliver: Executor?,private val  asyncCallback: AsyncCallback<T>? ) : Callback , AsyncCallback<T> {

    override fun onSuccess(t: T) {
        if (asyncCallback == null) return
        deliver?.execute {
            try {
                asyncCallback.onSuccess(t)
            } catch (t: Throwable) {
                onFailed(t)
            }
        }
    }

    override fun onFailed(throwable: Throwable) {
        if (asyncCallback == null) return
        deliver?.execute { asyncCallback.onFailed(throwable) }
    }


    override fun onError(threadName: String, t: Throwable) {
        onFailed(t)
        if (callback == null) return
        deliver?.execute { callback.onError(threadName, t) }
    }

    override fun onCompleted(threadName: String) {
        if (callback == null) return
        deliver?.execute { callback.onCompleted(threadName) }
    }

    override fun onStart(threadName: String) {
        if (callback == null) return
        deliver?.execute { callback.onStart(threadName) }
    }

}

