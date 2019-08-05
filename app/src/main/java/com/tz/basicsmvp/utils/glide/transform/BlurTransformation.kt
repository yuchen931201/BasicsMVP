package com.tz.basicsmvp.utils.glide.transform

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


/**
 * @author Pinger
 * @since 2018/7/17 下午10:31
 * 高斯模糊特效
 * 参考：[https://github.com/wasabeef/glide-transformations/blob/master/transformations/src/main/java/jp/wasabeef/glide/transformations/BlurTransformation.java]
 * @param radius 高斯的程度
 * @param sampling 比例
 */

class BlurTransformation(private val radius: Int = MAX_RADIUS, private val sampling: Int = DEFAULT_DOWN_SAMPLING) : BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
        val width = toTransform.width
        val height = toTransform.height
        val scaledWidth = width / sampling
        val scaledHeight = height / sampling

        val bitmap = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.scale(1 / sampling.toFloat(), 1 / sampling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        return FastBlur.blur(bitmap, radius, true)
    }


    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    }

    companion object {
        private const val MAX_RADIUS = 25
        private const val DEFAULT_DOWN_SAMPLING = 1
    }
}