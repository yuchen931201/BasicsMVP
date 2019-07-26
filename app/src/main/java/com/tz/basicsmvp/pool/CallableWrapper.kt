package cn.eclicks.drivingtest.pool

import java.util.concurrent.Callable

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/19 19:12
 * @Package: com.tz.autolayout.pool
 * @Description:
 **/
internal class CallableWrapper<T>(configs: Configs?, private val proxy: Callable<T>?) : Callable<T> {

    private var name: String = ""
    private var callback: Callback? = null

    init {
        this.name = configs?.name.toString()
        this.callback = CallbackDelegate(configs?.callback, configs?.deliver, configs?.asyncCallback)
    }

    @Throws(Exception::class)
    override fun call(): T? {
        Tools.resetThread(Thread.currentThread(), name, callback)
        callback?.onStart(name)

        // avoid NullPointException
        val t = proxy?.call()
        callback?.onCompleted(name)
        return t
    }
}