package com.tz.basicsmvp.utils.core.strategy

import android.graphics.drawable.Drawable
import com.tz.basicsmvp.utils.core.utils.RoundType

/**
 * @author Pinger
 * @since 3/28/18 3:23 PM
 *
 * 图片加载库的配置，封装原始加载配置属性，进行转换
 */
class ImageOptions private constructor() {

    /**
     * 加载占位图资源ID，如果placeholder是0表示没有占位图
     */
    var placeHolderResId = 0

    /**
     * 加载占位图资源Drawable对象
     */
    var placeHolderDrawable: Drawable? = null

    /**
     * 错误占位图的资源ID
     */
    var errorResId = 0

    /**
     * 加载失败占位图资源Drawable对象
     */
    var errorDrawable: Drawable? = null

    /**
     * 是否渐隐加载
     */
    var isCrossFade = true

    /**
     * 是否根据Gif图片后缀来自动加载
     */
    var isAutoGif: Boolean = true


    /**
     * 是否跳过内存缓存
     */
    var skipMemoryCache: Boolean = false

    /**
     * 没有动画，默认是使用动画
     */
    var isDontAnim: Boolean = false


    /**
     * 磁盘缓存
     */
    var diskCacheStrategy = DiskCache.AUTOMATIC

    /**
     * 加载优先级
     */
    var priority = LoadPriority.NORMAL

    /**
     * 加载缩略图
     */
    var thumbnail: Float = 0f

    /**
     * 缩略图链接
     */
    var thumbnailUrl: String? = null

    /**
     * 图片的Tag
     */
    var tag: String? = null

    /**
     * 图片的尺寸
     */
    var size: OverrideSize? = null


    /**
     * 特效处理：圆形图片
     * Glide要将isCrossFade设置为false，不然会影响展示效果
     */
    var isCircle: Boolean = false

    /**
     * 圆形是否带边框
     */
    var circleBorderWidth: Int = 0

    /**
     * 圆形边框的颜色
     */
    var circleBorderColor: Int = 0

    /**
     * 模糊特效
     * Glide要将isCrossFade设置为false，不然会影响展示效果
     */
    var isBlur: Boolean = false

    /**
     * 设置高斯模糊度数，如果外面设置为0则使用默认值
     */
    var blurRadius: Int = 25

    /**
     * 高斯模糊半經
     */
    var blurSampling: Int = 25


    /**
     * 是否圆角
     * Glide要将isCrossFade设置为false，不然会影响展示效果
     */
    var isRoundedCorners: Boolean = false


    /**
     * 圆角的弧度
     */
    var roundRadius: Int = 0

    /**
     * 圆角的边向
     */
    var roundType: RoundType = RoundType.ALL


    /**
     * 内部类，生成图片的基本配置
     */
    class Builder {
        private val config = ImageOptions()

        fun setPlaceHolderResId(placeHolderResId: Int): Builder {
            // 移除默认的配置
            if (placeHolderResId != 0) {
                config.placeHolderResId = placeHolderResId
                config.placeHolderDrawable = null
            }
            return this
        }

        fun setPlaceHolderDrawable(placeHolderDrawable: Drawable?): Builder {
            if (placeHolderDrawable != null) {
                config.placeHolderDrawable = placeHolderDrawable
                config.placeHolderResId = 0
            }
            return this
        }

        fun setErrorResId(errorResId: Int): Builder {
            if (errorResId != 0) {
                config.errorResId = errorResId
                config.errorDrawable = null
            }
            return this
        }

        fun setErrorDrawable(errorDrawable: Drawable): Builder {
            config.errorDrawable = errorDrawable
            return this
        }

        fun setCrossFade(isCrossFade: Boolean): Builder {
            config.isCrossFade = isCrossFade
            return this
        }

        fun setAutoGif(isAutoGif: Boolean): Builder {
            config.isAutoGif = isAutoGif
            return this
        }

        fun setSkipMemoryCache(skipMemoryCache: Boolean): Builder {
            config.skipMemoryCache = skipMemoryCache
            return this
        }

        fun setDiskCacheStrategy(diskCacheStrategy: DiskCache): Builder {
            config.diskCacheStrategy = diskCacheStrategy
            return this
        }

        fun setTag(tag: String?): Builder {
            config.tag = tag
            return this
        }

        fun setPriority(priority: LoadPriority): Builder {
            config.priority = priority
            return this
        }

        fun setThumbnail(thumbnail: Float): Builder {
            config.thumbnail = thumbnail
            return this
        }

        fun setThumbnailUrl(thumbnailUrl: String): Builder {
            config.thumbnailUrl = thumbnailUrl
            return this
        }

        fun setOverideSize(width: Int, height: Int): Builder {
            config.size = OverrideSize(width, height)
            return this
        }

        fun setCircle(isCircle: Boolean): Builder {
            config.isCircle = isCircle
            return this
        }

        fun setCircleBorderWidth(width: Int): Builder {
            if (width != 0) {
                config.circleBorderWidth = width
            }
            return this
        }

        fun setCircleBorderColor(color: Int): Builder {
            if (color != 0) {
                config.circleBorderColor = color
            }
            return this
        }

        fun setBlur(blur: Boolean): Builder {
            config.isBlur = blur
            return this
        }

        fun setBlurRadius(blurRadius: Int): Builder {
            if (blurRadius != 0) {
                config.blurRadius = blurRadius
            }
            return this
        }


        fun setBlurSampling(blurSampling: Int): Builder {
            if (blurSampling != 0) {
                config.blurSampling = blurSampling
            }
            return this
        }

        fun setRoundedCorners(roundedCorners: Boolean): Builder {
            config.isRoundedCorners = roundedCorners
            return this
        }

        fun setRoundRadius(roundRadius: Int): Builder {
            if (roundRadius != 0) {
                config.roundRadius = roundRadius
            }
            return this
        }

        fun setRoundType(roundType: RoundType): Builder {
            config.roundType = roundType
            return this
        }


        fun setDontAnim(isDontAnim: Boolean): Builder {
            config.isDontAnim = isDontAnim
            return this
        }

        fun build(): ImageOptions {
            return config
        }
    }


    /**
     * 指定加载图片的大小
     * @param width 宽
     * @param height 高
     */
    class OverrideSize(val width: Int, val height: Int)

    /**
     * 硬盘缓存策略
     */
    enum class DiskCache(val strategy: Int) {
        /**
         * 没有缓存
         */
        NONE(1),

        /**
         * 根据原始图片数据和资源编码策略来自动选择磁盘缓存策略，需要写入权限
         */
        AUTOMATIC(2),

        /**
         * 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源
         */
        RESOURCE(3),

        /**
         * 在资源解码前就将原始数据写入磁盘缓存，需要写入权限
         */
        DATA(4),

        /**
         * 使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据，需要写入权限
         */
        ALL(5)
    }

    /**
     * 加载优先级策略
     * 指定了图片加载的优先级后，加载时会按照图片的优先级进行顺序加载
     * IMMEDIATE优先级时会直接加载，不需要等待
     */
    enum class LoadPriority(val priority: Int) {
        /**
         * 低优先级
         */
        LOW(1),
        /**
         * 普通优先级
         */
        NORMAL(2),
        /**
         * 高优先级
         */
        HIGH(3),

        /**
         * 立即加载，无需等待
         */
        IMMEDIATE(4)
    }
}