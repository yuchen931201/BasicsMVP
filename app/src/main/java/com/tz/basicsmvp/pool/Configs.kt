package cn.eclicks.drivingtest.pool

import java.util.concurrent.Executor

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/19 19:47
 * @Package: com.tz.autolayout.pool
 * @Description:
 **/
internal class Configs {
    var name: String = ""// thread name
    var callback: Callback? = null// thread callback
    var delay: Long = 0// delay time
    var deliver: Executor? = null// thread deliver
    var asyncCallback: AsyncCallback<*>? = null
}