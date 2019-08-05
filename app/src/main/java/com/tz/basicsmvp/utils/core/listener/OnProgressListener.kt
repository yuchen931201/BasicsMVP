package com.tz.basicsmvp.utils.core.listener

/**
 * @author Pinger
 * @since 3/30/18 11:12 AM
 * 加载图片的监听
 */
interface OnProgressListener {

    /**
     * 下载的进度
     * @param bytesRead 当前下载的字节数
     * @param contentLength 总的字节数
     * @param isFinish 是否下载完成
     */
    fun onProgress(bytesRead: Long, contentLength: Long, isFinish: Boolean)
}