package cn.eclicks.drivingtest.pool

import java.util.concurrent.Callable

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/19 20:05
 * @Package: com.tz.autolayout.pool
 * @Description:   ???????????????
 **/

internal class RunnableWrapper<T>(configs: Configs?) : Runnable {

    private var name: String = ""
    private var delegate: CallbackDelegate<T>? = null
    private var runnable: Runnable? = null
    private var callable: Callable<T>? = null

    init {
        this.name = configs?.name.toString()
        configs?.run {
            this@RunnableWrapper.delegate = CallbackDelegate(this?.callback,this?.deliver,
                this?.asyncCallback as AsyncCallback<T>?)
        }
    }

    fun setRunnable(runnable: Runnable): RunnableWrapper<T> {
        this.runnable = runnable
        return this
    }

    fun setCallable(callable: Callable<T>): RunnableWrapper<T> {
        this.callable = callable
        return this
    }

    override fun run() {
        val current = Thread.currentThread()
        Tools.resetThread(current, name, delegate)
        delegate?.onStart(name)

        // avoid NullPointException
        if (runnable != null) {
            runnable!!.run()
        } else if (callable != null) {
            try {
                val result = callable!!.call()
                delegate?.onSuccess(result)
            } catch (e: Exception) {
                delegate?.onError(name, e)
            }

        }
        delegate?.onCompleted(name)
    }
}
