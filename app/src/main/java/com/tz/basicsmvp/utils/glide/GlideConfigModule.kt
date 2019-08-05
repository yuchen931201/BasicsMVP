package com.tz.basicsmvp.utils.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.tz.basicsmvp.utils.core.utils.ImageUtils
import com.tz.basicsmvp.utils.glide.progress.ProgressEngine
import java.io.InputStream


/**
 * @author Pinger
 * @since 3/30/18 12:56 PM
 *
 * Glide 的Module配置，设置glide的全局属性
 */
@GlideModule
class GlideConfigModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        /**
         * 替换Glide的请求方式，因为要监听图片的加载进度，使用okhttp来加载
         */
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(ProgressEngine.getOkHttpClient()))
    }

    /**
     * 配置Glide常用属性
     */
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // 设置Glide缓存目录
        val cacheDir = ImageUtils.getImageCacheDir(context)

        val cache = DiskLruCacheWrapper.create(cacheDir, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE.toLong())
        builder.setDiskCache { cache }

        // 设置memory和Bitmap池的大小
        val calculator = MemorySizeCalculator.Builder(context).build()
        val defaultMemoryCacheSize = calculator.memoryCacheSize
        val defaultBitmapPoolSize = calculator.bitmapPoolSize

        val customMemoryCacheSize = (1.2 * defaultMemoryCacheSize).toInt()
        val customBitmapPoolSize = (1.2 * defaultBitmapPoolSize).toInt()

        builder.setMemoryCache(LruResourceCache(customMemoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(customBitmapPoolSize.toLong()))
    }
}