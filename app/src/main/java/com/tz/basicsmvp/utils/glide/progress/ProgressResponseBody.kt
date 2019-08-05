package com.tz.basicsmvp.utils.glide.progress

import com.tz.basicsmvp.utils.core.listener.OnProgressListener
import com.tz.basicsmvp.utils.core.utils.ImageUtils
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException


/**
 * @author Pinger
 * @since 3/30/18 11:29 AM
 *
 * 图片加载的网络请求体，传入进度加载监听对象
 * @param responseBody 网络请求响应
 * @param progressListener 进度条监听
 */
class ProgressResponseBody(private val responseBody: ResponseBody?, val progressListener: OnProgressListener) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    /**
     * 总的字节长度
     */
    override fun contentLength(): Long {
        try {
            return responseBody?.contentLength() ?: 0
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return 0
    }

    override fun source(): BufferedSource? {
        if (responseBody == null) {
            return null
        }
        if (bufferedSource == null) {
            try {
                bufferedSource = Okio.buffer(source(responseBody.source()))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            // 接收的字节
            var totalBytesRead: Long = 0
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != (-1).toLong()) bytesRead else 0
                ImageUtils.runOnUIThread(Runnable {
                    progressListener.onProgress(totalBytesRead, contentLength(), bytesRead == (-1).toLong())
                })
                return bytesRead
            }
        }
    }

}