package com.tz.basicsmvp.mvp.base

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/8/2 14:15
 * @Package: com.tz.basicsmvp.mvp.base
 * @Description:
 **/
data class BaseJsonListBean<T>(val result: List<T>, val status:Int, val msg:String)