package cn.eclicks.drivingtest.pool

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/19 18:33
 * @Package: com.tz.autolayout.pool
 * @Description:
 **/
class AndroidDeliver : Executor{

    private val main = Handler(Looper.getMainLooper())

    companion object{

        fun getInstance(): AndroidDeliver {
            return ExecutorHolder.instance
        }

    }

    private object ExecutorHolder{
       internal val instance = AndroidDeliver()
    }

    override fun execute(runnable: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
            return
        }
        main.post { runnable.run() }
    }
}