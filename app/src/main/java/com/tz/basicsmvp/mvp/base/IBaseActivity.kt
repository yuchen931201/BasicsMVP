package com.tz.basicsmvp.mvp.base

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/8/2 17:00
 * @Package: com.tz.basicsmvp.mvp.base
 * @Description: 子类 activity 的基础功能扩展 , 也是连接BaseActivity与BaseUIController的桥梁
 **/
interface IBaseActivity {
    fun layoutId(): Int

    fun layoutType(): Int

    fun onFinishCreateView()

    fun initData()

    fun doScene()
}