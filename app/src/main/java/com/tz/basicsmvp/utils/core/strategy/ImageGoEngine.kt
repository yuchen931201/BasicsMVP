@file:JvmName("ImageGoEngine")
package com.tz.basicsmvp.utils.core.strategy

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.tz.basicsmvp.utils.core.ImageGo
import com.tz.basicsmvp.utils.core.listener.OnImageListener
import com.tz.basicsmvp.utils.core.listener.OnImageSaveListener
import com.tz.basicsmvp.utils.core.listener.OnProgressListener
import com.tz.basicsmvp.utils.core.utils.RoundType
import java.io.File


// －－－－－－－－－－提供图片加载相关的API－－－－－－－－－
// －－－－－－－－－－提供图片加载相关的API－－－－－－－－－
// －－－－－－－－－－提供图片加载相关的API－－－－－－－－－

//============================================================================
/**
 * 加载网络图片，可以配置加载监听，和其他Options配置项
 * @param any 图片资源
 * @param view 展示的View
 * @param listener 监听加载对象
 * @param placeHolder 占位图资源id
 * @param options 图片加载配置项
 */

fun loadImage(any: Any?, view: View?, listener: OnImageListener? = null, placeHolder: Int = 0, errorHolder: Int = 0, options: ImageOptions? = null) {
    val generateOptions = options ?: getDefaultBuilder()
            .setPlaceHolderResId(placeHolder)
            .setErrorResId(errorHolder)
            .build()
    getStrategy().loadImage(any, view, listener, generateOptions)
}

/**
 * 手动加载GIF图片，使用[loadImage]方法可以自动加载GIF图
 * @param any 图片资源
 * @param view 展示的View
 * @param listener 监听加载对象
 */
fun loadGif(any: Any?, view: View?, placeHolder: Int = 0, errorHolder: Int = 0, listener: OnImageListener? = null) {
    loadImage(any, view, listener = listener, options = getDefaultBuilder()
            .setAutoGif(true)
            .setPlaceHolderResId(placeHolder)
            .setErrorResId(errorHolder)
            .build())
}


//============================================================================
/**
 * 加载图片，带进度条
 * @param any 图片资源
 * @param view 展示的View
 * @param listener 加载监听
 */
fun loadProgress(any: Any?, view: View?, listener: OnProgressListener) {
    getStrategy().loadProgress(any, view, listener)
}


//============================================================================
/**
 * 异步加载图片资源，生成Bitmap对象
 * 可以在主线程直接调用
 * @param context 上下文
 * @param any 图片资源
 * @param listener　加载图片的回调
 */
fun loadBitmap(context: Context?, any: Any?, listener: OnImageListener) {
    getStrategy().loadBitmap(context, any, listener)
}


/**
 * 同步加载图片资源，生成Bitmap对象
 * 必须在子线程调用，并且处理异常
 * @param context 上下文
 * @param any 图片资源
 * @return Bitmap对象，可能为null
 */
fun loadBitmap(context: Context?, any: Any?): Bitmap? {
    return getStrategy().loadBitmap(context, any)
}


//============================================================================

/**
 * 加载圆形图片
 * @param any 图片资源
 * @param view　展示视图
 * @param borderWidth　边框的大小
 * @param borderColor　边框的颜色
 * @param listener　加载回调
 */
fun loadCircle(any: Any?, view: View?, borderWidth: Int = 0, borderColor: Int = 0, placeHolder: Int = 0, errorHolder: Int = 0, listener: OnImageListener? = null) {
    loadImage(any, view, listener = listener, options = getDefaultBuilder()
            .setCrossFade(false)
            .setCircle(true)
            .setPlaceHolderResId(placeHolder)
            .setErrorResId(errorHolder)
            .setCircleBorderColor(borderColor)
            .setCircleBorderWidth(borderWidth).build())
}


//============================================================================

/**
 * 加载圆角图片
 * @param any 图片链接
 * @param view 展示
 * @param roundRadius　圆角的角度
 * @param roundType　圆角图片的边向
 * @param listener 回调
 */
fun loadRound(any: Any?, view: View?, roundRadius: Int = 12, roundType: RoundType = RoundType.ALL, placeHolder: Int = 0, errorHolder: Int = 0, listener: OnImageListener? = null) {
    loadImage(any, view, listener = listener, options = getDefaultBuilder()
            .setCrossFade(false)
            .setPlaceHolderResId(placeHolder)
            .setErrorResId(errorHolder)
            .setRoundedCorners(true)
            .setRoundRadius(roundRadius)
            .setRoundType(roundType).build())
}


//============================================================================


/**
 * 加载高斯模糊图片
 * @param any　图片链接
 * @param view　展示
 * @param blurRadius　高斯模糊的度数
 * @param blurSampling　高斯模糊的半徑
 * @param listener 回调
 */
fun loadBlur(any: Any?, view: View?, blurRadius: Int = 25, blurSampling: Int = 1, placeHolder: Int = 0, errorHolder: Int = 0, listener: OnImageListener? = null) {
    loadImage(any, view, listener = listener, options = getDefaultBuilder()
            .setBlur(true)
            .setPlaceHolderResId(placeHolder)
            .setErrorResId(errorHolder)
            .setBlurSampling(blurSampling)
            .setBlurRadius(blurRadius).build())
}


//============================================================================

/**
 * 保存网络图片到本地，保存图片一定要自己检查是否有保存文件的权限
 * @param context　上下文
 * @param any　保存的图片资源
 * @param path 图片保存的路径
 * @param listener　图片保存的回调
 */
fun saveImage(context: Context?, any: Any?, path: String? = null, listener: OnImageSaveListener? = null) {
    getStrategy().saveImage(context, any, path, listener)
}

//============================================================================

/**
 * 清除图片的磁盘缓存
 * 必须在子线程调用
 * @param context 上下文
 */
fun clearImageDiskCache(context: Context?) {
    getStrategy().clearImageDiskCache(context)
}

/**
 * 清除图片的内存缓存
 * 必须在主线程调用
 * @param context 上下文
 */
fun clearImageMemoryCache(context: Context?) {
    getStrategy().clearImageMemoryCache(context)
}

/**
 * 获取手机磁盘图片缓存大小
 * @param context　上下文
 * @return 缓存大小，格式已经处理好了 例如：100M
 */
fun getCacheSize(context: Context?): String {
    return getStrategy().getCacheSize(context)
}

//============================================================================

/**
 * 恢复所有的图片加载任务，可以在页面或者列表可见时调用
 * @param context　上下文
 */
fun resumeRequests(context: Context?) {
    getStrategy().resumeRequests(context)
}

/**
 * 暂停所有的图片加载任务，可以在页面或者列表不可见的时候调用
 * @param context　上下文
 */
fun pauseRequests(context: Context?) {
    getStrategy().pauseRequests(context)
}


//============================================================================

/**
 * 下载网络图片到本地
 * 本方法在子线程执行
 * @param context 上线文
 * @param any　图片资源
 */
fun downloadImage(context: Context, any: Any?): File {
    return getStrategy().downloadImage(context, any)
}


//============================================================================
//============================================================================

/**
 * 获取默认的配置,可以手动配置
 */
private fun getDefaultBuilder(): ImageOptions.Builder {
    return if (ImageGo.getDefaultBuilder() != null) {
        ImageGo.getDefaultBuilder()!!
    } else getStrategy().getDefaultBuilder()
}

/**
 * 获取图片加载策略
 */
private fun getStrategy(): ImageStrategy {
    return ImageGo.getStrategy()
}


