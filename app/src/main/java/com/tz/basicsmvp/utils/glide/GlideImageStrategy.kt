package com.tz.basicsmvp.utils.glide

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.tz.basicsmvp.utils.core.listener.OnImageListener
import com.tz.basicsmvp.utils.core.listener.OnImageSaveListener
import com.tz.basicsmvp.utils.core.listener.OnProgressListener
import com.tz.basicsmvp.utils.core.strategy.ImageOptions
import com.tz.basicsmvp.utils.core.strategy.ImageStrategy
import com.tz.basicsmvp.utils.core.utils.ImageConstant
import com.tz.basicsmvp.utils.core.utils.ImageUtils
import com.tz.basicsmvp.utils.glide.progress.ProgressEngine
import com.tz.basicsmvp.utils.glide.transform.BlurTransformation
import com.tz.basicsmvp.utils.glide.transform.CircleTransformation
import com.tz.basicsmvp.utils.glide.transform.RoundedCornersTransformation
import java.io.File


/**
 * @author Pinger
 * @since 3/28/18 2:16 PM
 *
 * 使用Glide加载图片策略
 * 更多Glide使用请看官方使用手册[https://muyangmin.github.io/glide-docs-cn/doc/caching.html]
 */
class GlideImageStrategy : ImageStrategy {

    /**
     * 返回自己
     */
    override fun getStrategy(): ImageStrategy {
        return this
    }

    /**
     * 获取默认的配置,可以手动配置
     * 使用默认的加载和加载失败的占位图
     * 设置缓存策略为默认策略
     * 设置加载优先级为普通优先级
     * 设置加载渐变动画
     * 设置自动加载Gif图
     */
    override fun getDefaultBuilder(): ImageOptions.Builder {
        return ImageOptions
                .Builder()
                .setPlaceHolderDrawable(ColorDrawable(Color.parseColor(ImageConstant.IMAGE_PLACE_HOLDER_COLOR)))
                .setDiskCacheStrategy(ImageOptions.DiskCache.AUTOMATIC)
                .setPriority(ImageOptions.LoadPriority.NORMAL)
                .setCrossFade(true)
                .setAutoGif(true)
    }

    /**
     * 加载进度条图片
     */
    override fun loadProgress(any: Any?, view: View?, listener: OnProgressListener) {
        if (view == null || any == null) {
            listener.onProgress(0, 0, false)
            ImageUtils.logD(ImageConstant.LOAD_NULL_CONTEXT_ANY)
            return
        }
        // 添加进度条监听
        ProgressEngine.addProgressListener(listener)
        loadImage(any, view, null, getDefaultBuilder().build())
    }


    /**
     * 加载图片返回bitmap
     * 在主线程调用
     */
    override fun loadBitmap(context: Context?, any: Any?, listener: OnImageListener) {
        if (context == null || any == null) {
            listener.onFail(ImageConstant.LOAD_NULL_CONTEXT_ANY)
            ImageUtils.logD(ImageConstant.LOAD_NULL_CONTEXT_ANY)
            return
        }
        // submit方法要在子线程调用
        ImageUtils.runOnSubThread(Runnable {
            try {
                val bitmap = Glide
                        .with(context)
                        .asBitmap()
                        .load(any)
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                        .submit(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get()
                ImageUtils.runOnUIThread(Runnable {
                    listener.onSuccess(bitmap)
                })
            } catch (e: Exception) {
                ImageUtils.runOnUIThread(Runnable {
                    listener.onFail(ImageConstant.LOAD_ERROR)
                })
            }
        })
    }


    /**
     * 子线程同步获取Bitmap对象
     */
    override fun loadBitmap(context: Context?, any: Any?): Bitmap? {
        if (context == null || any == null) {
            ImageUtils.logD(ImageConstant.LOAD_NULL_CONTEXT_ANY)
            return null
        }
        // submit方法要在子线程调用
        return Glide
                .with(context)
                .asBitmap()
                .load(any)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get()
    }


    /**
     * 保存图片到本地
     * 可以在主线程调用
     */
    override fun saveImage(context: Context?, any: Any?, path: String?, listener: OnImageSaveListener?) {
        listener?.onSaveStart()
        if (context == null || any == null) {
            ImageUtils.logD(ImageConstant.SAVE_NULL_CONTEXT_ANY)
            listener?.onSaveFail(ImageConstant.SAVE_NULL_CONTEXT_ANY)
            return
        }

        if (!ImageUtils.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            listener?.onSaveFail(ImageConstant.SAVE_NOT_PERMISSION)
            return
        }
        ImageUtils.runOnSubThread(Runnable {
            try {
                // 图片后缀
                val suffix = if (ImageUtils.isGif(any)) {
                    System.currentTimeMillis().toString() + ImageConstant.IMAGE_GIF
                } else {
                    System.currentTimeMillis().toString() + ImageConstant.IMAGE_JPG
                }

                val filePath = if (TextUtils.isEmpty(path)) {
                    ImageUtils.getImageSavePath(context)
                } else path + File.separator

                // 保存的位置
                val destFile = File(filePath + suffix)
                // 要保存的原图
                val imageFile = downloadImage(context, any)
                // 进行保存
                val isCopySuccess = ImageUtils.copyFile(imageFile, destFile)

                // 最后通知图库更新
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(destFile)))
                // 主线程回调
                ImageUtils.runOnUIThread(Runnable {
                    if (isCopySuccess) {
                        if (listener == null) {
                            ImageUtils.showToast(context, ImageConstant.SAVE_PATH + filePath, Toast.LENGTH_LONG)
                        } else {
                            listener.onSaveSuccess(ImageUtils.getImageSavePath(context), suffix)
                        }
                    } else {
                        listener?.onSaveFail(ImageConstant.SAVE_FAIL)
                    }
                })
            } catch (e: Exception) {
                ImageUtils.runOnUIThread(Runnable {
                    listener?.onSaveFail(ImageConstant.SAVE_FAIL)
                    ImageUtils.logE(ImageConstant.SAVE_FAIL + ": " + e.message)
                })
            }
        })
    }


    /**
     * 清理磁盘缓存
     * 可以主线程调用
     */
    override fun clearImageDiskCache(context: Context?) {
        if (context != null) {
            Glide.get(context).clearDiskCache()
        } else {
            ImageUtils.logD(ImageConstant.CLEAR_NULL_CONTEXT)
        }
    }

    /**
     * 清除内存缓存
     * 只能在主线程调用
     */
    override fun clearImageMemoryCache(context: Context?) {
        if (context != null) {
            Glide.get(context).clearMemory()
        } else {
            ImageUtils.logD(ImageConstant.CLEAR_NULL_CONTEXT)
        }
    }


    /**
     * 获取本地缓存大小
     * 同步方法
     */
    override fun getCacheSize(context: Context?): String {
        return ImageUtils.getImageCacheSize(context)
    }

    /**
     * 重新加载
     */
    override fun resumeRequests(context: Context?) {
        if (context != null) {
            Glide.with(context).resumeRequests()
        }
    }


    /**
     * 暂停加载
     */
    override fun pauseRequests(context: Context?) {
        if (context != null) {
            Glide.with(context).pauseRequests()
        }
    }


    /**
     * 缓存图片文件
     */
    override fun downloadImage(context: Context, any: Any?): File {
        return Glide
                .with(context)
                .download(any)
                .submit()
                .get()
    }

    /**
     * Glide加载图片的主要方法
     * @param any 图片资源
     * @param view 图片展示控件
     * @param options 图片配置
     * @param listener 图片加载回调
     */
    override fun loadImage(any: Any?, view: View?, listener: OnImageListener?, options: ImageOptions) {
        // any和view判空
        if (view == null) {
            listener?.onFail(ImageConstant.LOAD_NULL_ANY_VIEW)
            ImageUtils.logD(ImageConstant.LOAD_NULL_ANY_VIEW)
            return
        }

        // context判空
        val context = view.context
        if (context == null) {
            listener?.onFail(ImageConstant.LOAD_NULL_CONTEXT)
            ImageUtils.logD(ImageConstant.LOAD_NULL_CONTEXT)
            return
        }


        try {
            // 是Gif图片，并且支持加载才去加载图片，不是Gif图不加载，不自动加载也不加载
            if (ImageUtils.isGif(any) && options.isAutoGif) {
                val gifBuilder = Glide.with(context).asGif().load(any)
                val builder = buildGift(context, any, options, gifBuilder, listener)

                // 使用clone方法复用builder，有缓存不会请求网络
                if (view is ImageView) {
                    builder.clone().apply(buildOptions(view, context, any, options)).into(view)
                } else throw IllegalStateException(ImageConstant.LOAD_ERROR_VIEW_TYPE)

            } else {
                val bitmapBuilder = Glide.with(context).asBitmap().load(any)
                val builder = buildBitmap(context, any, options, bitmapBuilder, listener)

                if (view is ImageView) {
                    builder.clone().apply(buildOptions(view, context, any, options)).into(view)
                } else throw IllegalStateException(ImageConstant.LOAD_ERROR_VIEW_TYPE)
            }
        } catch (e: Exception) {
            listener?.onFail(ImageConstant.LOAD_ERROR + "：" + e.message)
            if (view is ImageView) {
                view.setImageResource(options.errorResId)
            }
        }
    }


    /**
     * 设置bitmap属性
     */
    @SuppressLint("CheckResult")
    private fun buildBitmap(context: Context, obj: Any?, options: ImageOptions, bitmapBuilder: RequestBuilder<Bitmap>, listener: OnImageListener?): RequestBuilder<Bitmap> {
        var builder = bitmapBuilder
        // 渐变展示
        if (options.isCrossFade) {
            builder.transition(BitmapTransitionOptions.withCrossFade())
        }

        // 缩略图大小
        if (options.thumbnail > 0f) {
            builder.thumbnail(options.thumbnail)
        }

        // 缩略图请求
        if (!TextUtils.isEmpty(options.thumbnailUrl)) {
            val thumbnailBuilder = Glide.with(context).asBitmap().load(obj).thumbnail(Glide.with(context).asBitmap().load(options.thumbnailUrl))
            builder = thumbnailBuilder
        }

        // 加载监听
        builder.listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                listener?.onFail(e?.message)
                return false
            }

            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                listener?.onSuccess(resource)
                return false
            }
        })
        return builder
    }


    /**
     * 设置Gift属性
     */
    @SuppressLint("CheckResult")
    private fun buildGift(context: Context, obj: Any?, options: ImageOptions, gifBuilder: RequestBuilder<GifDrawable>, listener: OnImageListener?): RequestBuilder<GifDrawable> {
        var builder = gifBuilder

        // 缩略图大小
        if (options.thumbnail > 0f) {
            builder.thumbnail(options.thumbnail)
        }

        // 缩略图请求
        if (!TextUtils.isEmpty(options.thumbnailUrl)) {
            val thumbnailBuilder = Glide.with(context).asGif().load(obj).thumbnail(Glide.with(context).asGif().load(options.thumbnailUrl))
            builder = thumbnailBuilder
        }

        // 加载监听
        builder.listener(object : RequestListener<GifDrawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                listener?.onFail(e?.message)
                return false
            }

            override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                listener?.onSuccess(resource?.firstFrame)
                return false
            }
        })

        return builder
    }

    /**
     * 设置图片加载选项，返回请求对象
     */
    @SuppressLint("CheckResult")
    private fun buildOptions(view: View, context: Context, obj: Any?, options: ImageOptions): RequestOptions {
        val reqOptions = RequestOptions()

        // 设置缓存策略，设置缓存策略要先判断是否有读写权限，如果没有权限，但是又设置了缓存策略则会加载失败
        val strategy = if (ImageUtils.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            when (options.diskCacheStrategy.strategy) {
                ImageOptions.DiskCache.NONE.strategy -> DiskCacheStrategy.NONE
                ImageOptions.DiskCache.AUTOMATIC.strategy -> DiskCacheStrategy.AUTOMATIC
                ImageOptions.DiskCache.RESOURCE.strategy -> DiskCacheStrategy.RESOURCE
                ImageOptions.DiskCache.DATA.strategy -> DiskCacheStrategy.DATA
                ImageOptions.DiskCache.ALL.strategy -> DiskCacheStrategy.ALL
                else -> DiskCacheStrategy.RESOURCE
            }
        } else {
            DiskCacheStrategy.RESOURCE
        }
        reqOptions.diskCacheStrategy(strategy)


        // 设置加载优先级
        val priority = when (options.priority.priority) {
            ImageOptions.LoadPriority.LOW.priority -> Priority.LOW
            ImageOptions.LoadPriority.NORMAL.priority -> Priority.NORMAL
            ImageOptions.LoadPriority.HIGH.priority -> Priority.HIGH
            else -> Priority.NORMAL
        }
        reqOptions.priority(priority)

        // 内存缓存跳过
        reqOptions.skipMemoryCache(options.skipMemoryCache)

        // 加载中占位图
        if (options.placeHolderResId != 0) {
            reqOptions.placeholder(options.placeHolderResId)
        } else if (options.placeHolderDrawable != null) {
            reqOptions.placeholder(options.placeHolderDrawable)
        }
        // 加载错误和链接为null都设置相同的占位图
        if (options.errorResId != 0) {
            reqOptions.error(options.errorResId)
        } else if (options.errorDrawable != null) {
            reqOptions.error(options.errorDrawable)
        }

        // 无动画
        if (options.isDontAnim) {
            reqOptions.dontAnimate()
        }

        // Tag
        val tag = options.tag
        if (tag != null) {
            reqOptions.signature(ObjectKey(tag))
        } else {
            reqOptions.signature(ObjectKey(obj.toString()))
        }

        // 设置固定的宽高
        if (options.size != null) {
            reqOptions.override(options.size!!.width, options.size!!.height)
        }

        // 设置transform
        // 是否设置圆行特效
        if (options.isCircle) {
            reqOptions.transform(CircleTransformation(options.circleBorderWidth, options.circleBorderColor))
        }

        // 设置高斯模糊特效
        if (options.isBlur) {
            reqOptions.transform(BlurTransformation(options.blurRadius, options.blurSampling))
        }

        // 是否设置圆角特效
        if (options.isRoundedCorners) {
            var transformation: BitmapTransformation? = null
            // 圆角特效受到ImageView的scaleType属性影响
            if (view is ImageView && (view.scaleType == ImageView.ScaleType.FIT_CENTER ||
                            view.scaleType == ImageView.ScaleType.CENTER_INSIDE ||
                            view.scaleType == ImageView.ScaleType.CENTER ||
                            view.scaleType == ImageView.ScaleType.CENTER_CROP)) {
                transformation = CenterCrop()
            }
            if (transformation == null) {
                reqOptions.transform(RoundedCornersTransformation(options.roundRadius, options.roundType))
            } else {
                reqOptions.transforms(transformation, RoundedCornersTransformation(options.roundRadius, options.roundType))
            }
        }
        return reqOptions
    }

}

