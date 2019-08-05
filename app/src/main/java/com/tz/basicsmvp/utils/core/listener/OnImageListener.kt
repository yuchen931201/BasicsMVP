package com.tz.basicsmvp.utils.core.listener

import android.graphics.Bitmap

/**
 * @author Pinger
 * @since 3/28/18 2:22 PM
 */
interface OnImageListener {

    /**
     * 图片加载成功
     * @param bitmap 加载成功生成的bitmap对象
     */
    fun onSuccess(bitmap: Bitmap?)

    /**
     * 图片加载失败
     * @param msg 加载失败的原因
     */
    fun onFail(msg: String?)
}