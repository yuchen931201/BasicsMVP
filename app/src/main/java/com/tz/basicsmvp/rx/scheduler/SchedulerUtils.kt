package com.tz.basicsmvp.rx.scheduler

import com.hazz.kotlinmvp.rx.scheduler.IoMainScheduler

/**
* @ComputerCode: tianzhen
* @Author: TianZhen
* @QQ: 959699751
* @CreateTime: Created on 2019/5/17 19:57
* @Package:
* @Description:
**/
object SchedulerUtils {

    fun <T> ioToMain(): IoMainScheduler<T> {
        return IoMainScheduler()
    }
}
