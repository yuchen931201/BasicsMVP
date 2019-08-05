package com.tz.basicsmvp.utils.core.strategy


import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.tz.basicsmvp.utils.core.listener.OnImageListener
import com.tz.basicsmvp.utils.core.listener.OnImageSaveListener
import com.tz.basicsmvp.utils.core.listener.OnProgressListener
import java.io.File

/**
 * @author Pinger
 * @since 3/28/18 2:17 PM
 *
 * 图片加载策略基类，定义接口
 * 只定义最全的接口
 */

interface ImageStrategy {

    /**
     * 内部获取策略模式
     */
    fun getStrategy(): ImageStrategy

    /**
     * 获取默认的图片配置
     */
    fun getDefaultBuilder(): ImageOptions.Builder

    /**
     * 加载网络图片，支持png，jpeg,jpg,gif等格式
     * @param any 图片资源，可以是Url,File,Bitmap,URI,ResID,Drawable
     * @param view 图片展示
     * @param listener 图片加载监听
     */
    fun loadImage(any: Any?, view: View?, listener: OnImageListener?, options: ImageOptions)


    /**
     * 加载图片资源，异步获取Bitmap对象，可以在主线程调用
     * @param context 上下文
     * @param any 图片资源
     * @param listener　加载图片的回调
     */
    fun loadBitmap(context: Context?, any: Any?, listener: OnImageListener)


    /**
     * 加载图片，同步返回Bitmap对象，必须在子线程调用，并且自己捕获异常
     */
    fun loadBitmap(context: Context?, any: Any?): Bitmap?


    /**
     * 加载图片，带有进度条
     */
    fun loadProgress(any: Any?, view: View?, listener: OnProgressListener)


    /**
     * 保存网络图片到本地
     * @param context　上下文
     * @param any　保存的图片资源
     * @param listener　图片保存的回调
     */
    fun saveImage(context: Context?, any: Any?, path: String?, listener: OnImageSaveListener?)

    /**
     * 清除图片的磁盘缓存
     * 必须在子线程调用
     * @param context 上下文
     */
    fun clearImageDiskCache(context: Context?)

    /**
     * 清除图片的内存缓存
     * 必须在主线程调用
     * @param context 上下文
     */
    fun clearImageMemoryCache(context: Context?)

    /**
     * 获取手机磁盘图片缓存大小
     * @param context　上下文
     * @return 缓存大小，格式已经处理好了 例如：100M
     */
    fun getCacheSize(context: Context?): String

    /**
     * 恢复所有的图片加载任务，可以在页面或者列表可见时调用
     * @param context　上下文
     */
    fun resumeRequests(context: Context?)

    /**
     * 暂停所有的图片加载任务，可以在页面或者列表不可见的时候调用
     * @param context　上下文
     */
    fun pauseRequests(context: Context?)

    /**
     * 下载网络图片到本地
     * 本方法在子线程执行
     * @param context 上线文
     * @param any　图片的url
     */
    fun downloadImage(context: Context, any: Any?): File
}
