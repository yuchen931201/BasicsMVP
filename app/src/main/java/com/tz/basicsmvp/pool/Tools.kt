package cn.eclicks.drivingtest.pool

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/19 20:32
 * @Package: com.tz.autolayout.pool
 * @Description:
 **/

internal object Tools {

    var isAndroid: Boolean = false// Flag: is on android platform

    /**
     * Reset thread name and set a UnCaughtExceptionHandler to wrap callback to notify user when occurs a exception
     * @param thread The thread who should be reset.
     * @param name  non-null, thread name
     * @param callback a callback to notify user.
     */
    fun resetThread(thread: Thread, name: String, callback: Callback?) {
        thread.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { t, e ->
            callback?.onError(name, e)
        }
        thread.name = name
    }

    fun sleepThread(time: Long) {
        if (time <= 0) {
            return
        }

        try {
            Thread.sleep(time)
        } catch (e: InterruptedException) {
            throw RuntimeException("Thread has been interrupted", e)
        }

    }

    fun isEmpty(data: CharSequence?): Boolean {
        return data == null || data.length == 0
    }

    init {
        try {
            Class.forName("android.os.Build")
            isAndroid = true
        } catch (e: Exception) {
            isAndroid = false
        }

    }
}