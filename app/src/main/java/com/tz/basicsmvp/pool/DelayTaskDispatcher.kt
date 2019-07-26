package cn.eclicks.drivingtest.pool

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/19 19:48
 * @Package: com.tz.autolayout.pool
 * @Description: The class to launch the delay-task with a core Scheduled pool.
 **/
internal class DelayTaskDispatcher private constructor() {
    private val dispatcher: ScheduledExecutorService

    init {
        dispatcher = Executors.newScheduledThreadPool(1) { runnable ->
            val thread = Thread(runnable)
            thread.name = "Delay-Task-Dispatcher"
            thread.priority = Thread.MAX_PRIORITY
            thread
        }
    }

    fun postDelay(delay: Long, pool: ExecutorService, task: Runnable) {
        if (delay == 0L) {
            pool.execute(task)
            return
        }

        dispatcher.schedule({ pool.execute(task) }, delay, TimeUnit.MILLISECONDS)
    }

    companion object {
        private val instance = DelayTaskDispatcher()
        fun get(): DelayTaskDispatcher {
            return instance
        }
    }

}