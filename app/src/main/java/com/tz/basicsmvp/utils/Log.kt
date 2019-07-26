package com.tz.basicsmvp.utils

import com.blankj.utilcode.util.LogUtils

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/17 17:49
 * @Package: com.tz.basicsmvp.utils
 * @Description:
 **/
class Log{

    fun e(vararg content:Any){
        LogUtils.e(content)
    }

    fun i(vararg content:Any){
        LogUtils.i(content)
    }

    fun w(vararg content:Any){
        LogUtils.w(content)
    }

    fun d(vararg content:Any){
        LogUtils.d(content)
    }

}