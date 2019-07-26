package com.tz.basicsmvp.net.glide

import android.content.Context
import androidx.annotation.NonNull
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.module.AppGlideModule


/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/27 14:37
 * @Package: com.tz.basicsmvp.net.glide
 * @Description:
 **/
@GlideModule
class MyAppGlideModule : AppGlideModule(){


    override fun applyOptions(@NonNull context: Context, @NonNull builder: GlideBuilder) {

        //下面3中设置都可自定义大小，以及完全自定义实现
        //内存缓冲
        val calculator = MemorySizeCalculator.Builder(context)
            .setMemoryCacheScreens(2f)
            .setBitmapPoolScreens(3f)
            .build()
        builder.setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))

        //Bitmap 池
        builder.setBitmapPool(LruBitmapPool(calculator.bitmapPoolSize.toLong()))

        //磁盘缓存
        val diskCacheSizeBytes = 1024 * 1024 * 100  //100 MB
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes.toLong()))
    }

}