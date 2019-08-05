package com.tz.basicsmvp.utils.swipe.x

import android.content.Context
import com.billy.android.swipe.SmartSwipe
import com.billy.android.swipe.SmartSwipeWrapper

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/8/5 14:29
 * @Package: com.tz.basicsmvp.utils.swipe.x
 * @Description:
 **/
class WrapperFactory : SmartSwipe.IWrapperFactory {
    override fun createWrapper(context: Context): SmartSwipeWrapper {
        return SmartSwipeWrapperX(context)
    }
}