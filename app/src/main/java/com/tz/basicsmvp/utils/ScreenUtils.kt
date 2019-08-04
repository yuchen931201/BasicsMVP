package com.tz.basicsmvp.utils

import android.content.Context

object ScreenUtils {

    fun px2dp(ctx: Context,pxVal: Float): Float {
        val scale = ctx.resources.displayMetrics.density
        return pxVal / scale
    }

    fun dp2px(ctx: Context,dpVal: Float): Float{
        val scale = ctx.resources.displayMetrics.density
        return dpVal * scale
    }

}