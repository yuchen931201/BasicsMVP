package com.tz.basicsmvp.utils.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.tz.basicsmvp.utils.core.ImageGo
import java.io.*
import java.text.DecimalFormat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


object ImageUtils {

    private val mHandler: Handler = Handler(Looper.getMainLooper())

    /**
     * 是否是GIF图
     */
    fun isGif(any: Any?): Boolean {
        return if (any is String) {
            any.endsWith(ImageConstant.IMAGE_GIF, true)
        } else {
            false
        }
    }

    /**
     * 单位转换
     */
    fun dp2px(context: Context?, dipValue: Float): Float {
        if (context == null) {
            return dipValue
        }
        val scale = context.resources.displayMetrics.density
        return dipValue * scale + 0.5f
    }


    /**å
     * 运行在主线程
     */
    fun runOnUIThread(run: Runnable) {
        mHandler.post(run)
    }

    /**
     * 运行在子线程
     */
    fun runOnSubThread(run: Runnable) {
        singlePool.execute(run)
    }

    /**
     * 获取一个单线程池，所有任务将会被按照加入的顺序执行，免除了同步开销的问题
     */
    private val singlePool: ExecutorService
        get() = Executors.newSingleThreadExecutor()


    /**
     * 获取项目数据目录的路径字符串
     */
    private fun getAppDataPath(context: Context?): String {
        val dataPath: String = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || context == null) {
            (Environment.getExternalStorageDirectory()
                    .absolutePath
                    + File.separator
                    + context?.packageName)
        } else {
            (context.filesDir.absolutePath
                    + File.separator
                    + context.packageName)
        }
        createOrExistsDir(dataPath)
        return dataPath
    }

    /**
     * 获取图片存储的路径
     */
    fun getImageSavePath(context: Context?): String {
        val path = getAppDataPath(context) + File.separator + ImageConstant.IMAGE_PATH + File.separator
        createOrExistsDir(path)
        return path
    }


    /**　
     * 获取图片缓存目录
     */
    fun getImageCacheDir(context: Context?): File {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_UNMOUNTED) {
            // 没有存储卡
            File(context?.cacheDir, context?.packageName + "/imageCache")
        } else {
            // 有储存卡
            File(Environment.getExternalStorageDirectory(), context?.packageName + "/imageCache")
        }
    }


    /**
     * 复制文件
     */
    fun copyFile(srcFile: File?,
                 destFile: File?): Boolean {
        if (srcFile == null || destFile == null) return false
        // srcFile equals destFile then return false
        if (srcFile == destFile) return false
        // srcFile doesn't exist or isn't a file then return false
        if (!srcFile.exists() || !srcFile.isFile) return false

        var os: OutputStream? = null
        var stream: InputStream? = null
        return try {
            stream = FileInputStream(srcFile)
            os = BufferedOutputStream(FileOutputStream(destFile))
            val data = ByteArray(8192)
            var len = stream.read(data, 0, 8192)
            while (len != -1) {
                os.write(data, 0, len)
                len = stream.read(data, 0, 8192)
            }
            true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            false
        } finally {
            stream?.close()
            os?.close()
        }

    }

    private fun createOrExistsDir(dataPath: String) {
        val file = File(dataPath)
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    /**
     * 展示Toast
     */
    fun showToast(context: Context?, content: String) {
        if (context == null) {
            return
        }
        runOnUIThread(Runnable {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
        })
    }


    /**
     * 展示Toast
     */
    fun showToast(context: Context?, content: String,duration:Int) {
        if (context == null) {
            return
        }
        runOnUIThread(Runnable {
            Toast.makeText(context, content, duration).show()
        })
    }



    /**
     * 获取图片缓存的大小
     */
    fun getImageCacheSize(context: Context?): String {
        return try {
            val file = getImageCacheDir(context)
            var blockSize: Long = 0
            file.listFiles().forEach {
                blockSize += it.length()
            }
            formatFileSize(blockSize)
        } catch (e: Exception) {
            e.printStackTrace()
            "0M"
        }
    }

    /**
     * 转换文件大小
     */
    private fun formatFileSize(fileSize: Long): String {
        val df = DecimalFormat("#")
        val wrongSize = "0M"
        if (fileSize == 0L) {
            return wrongSize
        }
        return when {
            fileSize < 1024 -> df.format(fileSize.toDouble()) + "B"
            fileSize < 1048576 -> df.format(fileSize.toDouble() / 1024) + "KB"
            fileSize < 1073741824 -> df.format(fileSize.toDouble() / 1048576) + "MB"
            else -> df.format(fileSize.toDouble() / 1073741824) + "GB"
        }
    }


    /**
     * 检查是否有某一项权限
     */
    fun checkPermission(context: Context?, permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            context?.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else {
            context?.packageManager?.checkPermission(permission, context.packageName) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 打印日志
     */
    fun logD(msg: String) {
        if (ImageGo.isDebug()) {
            Log.d("ImageGo", "-----> $msg")
        }
    }

    /**
     * 打印日志
     */
    fun logE(msg: String) {
        if (ImageGo.isDebug()) {
            Log.e("ImageGo", "-----> $msg")
        }
    }
}