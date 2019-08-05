package com.tz.basicsmvp.utils.glide.transform

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


/**
 * Glide绘制圆形图片，并且可以绘制圆形边框，指定边框颜色
 * @param borderColor 边框颜色
 * @param borderWidth 边框宽度
 *
 * 参考：[https://github.com/wasabeef/glide-transformations/blob/master/transformations/src/main/java/jp/wasabeef/glide/transformations/CropCircleTransformation.java]
 */
class CircleTransformation(private val borderWidth: Int = 0
                           , borderColor: Int = 0) : BitmapTransformation() {

    /**
     * 绘制边框的画笔
     */
    private var mBorderPaint = Paint()

    /**
     * 设置画笔的属性
     */
    init {
        mBorderPaint.isDither = true
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = borderColor
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = borderWidth.toFloat()
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
        return circleCrop(pool, toTransform)
    }

    private fun circleCrop(pool: BitmapPool, source: Bitmap): Bitmap? {
        // 获取资源的长宽,获取最小值 子位图的像素个数
        val size = Math.min(source.width, source.height)
        // 子位图第一个像素在源位图的X坐标
        val x = (source.width - size) / 2
        // 子位图第一个像素在源位图的y坐标
        val y = (source.height - size) / 2

        // 创建新位图 source 源位图
        val squared = Bitmap.createBitmap(source, x, y, size, size)
        // 返回一个正好匹配给定宽、高和配置的只包含透明像素的Bitmap
        // 如果BitmapPool中找不到这样的Bitmap，就返回null
        var result: Bitmap? = pool.get(size, size, Bitmap.Config.ARGB_8888)
        // 当返回null 时,创建给定宽、高和配置的新位图
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }

        if (result != null) {
            // 画图
            val canvas = Canvas(result)

            val paint = Paint()
            // 设置shader
            paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            // 抗锯齿
            paint.isAntiAlias = true

            val r = size / 2f
            // 用设置好的画笔绘制一个圆
            canvas.drawCircle(r, r, r, paint)

            val borderRadius = r - borderWidth / 2
            // 画边框
            canvas.drawCircle(r, r, borderRadius, mBorderPaint)
        }
        return result
    }


    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }

}