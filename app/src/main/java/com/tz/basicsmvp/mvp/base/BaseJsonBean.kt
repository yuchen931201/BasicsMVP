package com.tz.basicsmvp.mvp.base

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/8/2 14:14
 * @Package: com.tz.basicsmvp.mvp.base
 * @Description:
 **/
data class BaseJsonBean<T>(val result: T, val status:Int, val msg:String)