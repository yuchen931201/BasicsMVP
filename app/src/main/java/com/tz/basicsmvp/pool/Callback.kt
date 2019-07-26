package cn.eclicks.drivingtest.pool

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/19 19:25
 * @Package: com.tz.autolayout.pool
 * @Description:  A callback interface to notify user that the task's status.
 **/
interface Callback {
    /**
     * This method will be invoked when thread has been occurs an error.
     * @param threadName The running thread name
     * @param t The exception
     */
     fun onError(threadName: String, t: Throwable)

    /**
     * notify user to know that it completed.
     * @param threadName The running thread name
     */
     fun onCompleted(threadName: String)

    /**
     * notify user that task start running
     * @param threadName The running thread name
     */
     fun onStart(threadName: String)
}