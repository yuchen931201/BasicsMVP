package com.tz.basicsmvp.mvp.base

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.tz.basicsmvp.mvp.view.custom.MultipleStatusView

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/8/2 16:23
 * @Package: com.tz.basicsmvp.mvp.base
 * @Description: 所有BaseUIController中的方法,想要对外公开,都在这里定义接口,否则都是私有方法,无论是BaseActivity还是其子类,都不可操作BaseUIController
 *      保证BaseUIController的逻辑独立,处理通用操作
 **/
interface IBaseUIController {

    fun getStatusView(): MultipleStatusView?

    fun setTitle(s: String)

    fun getToolbar(): Toolbar?

    fun getRootView(): View

    fun onDestroy()

}
