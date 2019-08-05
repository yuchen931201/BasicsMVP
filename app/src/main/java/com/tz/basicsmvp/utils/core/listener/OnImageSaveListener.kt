package com.tz.basicsmvp.utils.core.listener

/**
 * @author Pinger
 * @since 18-4-23 下午8:48
 * 保存图片到本地的监听
 */
interface OnImageSaveListener {

    /**
     * 保存图片开始
     */
    fun onSaveStart()

    /**
     * 图片保存成功
     * @param path 图片保存的路径
     */
    fun onSaveSuccess(path: String?,fileName:String)

    /**
     * 图片保存失败
     * @param msg 图片保存失败的原因
     */
    fun onSaveFail(msg: String?)
}