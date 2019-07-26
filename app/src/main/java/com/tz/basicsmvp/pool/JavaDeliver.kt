package cn.eclicks.drivingtest.pool

import java.util.concurrent.Executor

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/19 20:01
 * @Package: com.tz.autolayout.pool
 * @Description: The deliver for <b>Java Platform</b> by default.
 **/

internal class JavaDeliver : Executor {

    override fun execute(runnable: Runnable) {
        runnable.run()
    }

    companion object {
        fun getInstance(): JavaDeliver{
            return ExecutorHolder.instance
        }
    }

    private object ExecutorHolder{
        internal val instance:JavaDeliver = JavaDeliver()
    }

}