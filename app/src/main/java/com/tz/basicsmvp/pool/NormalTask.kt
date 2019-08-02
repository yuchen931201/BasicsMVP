package com.tz.basicsmvp.pool

import cn.eclicks.drivingtest.pool.PoolBundle
import cn.eclicks.drivingtest.pool.Result
import com.blankj.utilcode.util.LogUtils
import com.tz.basicsmvp.net.download.DownloadUtils
import com.tz.basicsmvp.service.DownloadFileService
import com.tz.basicsmvp.utils.CommonPreferences
import com.tz.basicsmvp.utils.SharePreferencesHelper
import com.tz.basicsmvp.utils.StorageUtils
//import org.greenrobot.eventbus.EventBus
import java.io.File
import java.util.concurrent.Callable

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/22 12:52
 * @Package: cn.eclicks.drivingtest.pool
 * @Description:
 **/
class NormalTask(private val params: PoolBundle) :Runnable,Callable<PoolBundle>{

    internal var mStatus: Status? = null
    internal var mProgress: Int = 0

    override fun run() {
        params.ctx
    }

    @Throws(Exception::class)
    override fun call(): PoolBundle {
        var result = Result()

        //判断sd卡的可用性
        if (!StorageUtils.hasAvaiableSpace(params.fileSize)) {
            mStatus = Status.Error
            DownloadFileService.stopDownload(params.ctx,params)
            notifyBroadcast()
            result.isSuccess = false
            params.result = result
            return params
        }
//        try {
            val cacheDirectory = StorageUtils.getVideoDirectory(params.ctx)
            val dest = File(cacheDirectory, File(params.saveFileName).name)
            if (dest.exists()) {//不使用断点下载
                dest.delete()
            }
            mStatus = Status.Downloading

            DownloadUtils.downloadFile(params.ctx, params.downloadUrl, dest,
                    object : DownloadUtils.DownloadProgressListener {

                        var preProgress = 0

                        override fun onProgress(url: String, filePath: String, progress: Int) {
                            LogUtils.i("Download: progress=" + progress + ",url=" + url)
                            mProgress = progress
                            if (progress >= 100) {
                                mStatus = Status.Finished
                                DownloadFileService.removeDownload(params.ctx, params)
                                result.isSuccess = true
                                params.progress = progress
                                params.result = result
                                notifyBroadcast()
                            } else {
                                if (progress - preProgress >= 2) {
//                                    L.d(String.format(Locale.getDefault(), "%s----progress: %d", this.toString(), progress))
                                    preProgress = progress
                                    params.progress = progress
                                    notifyBroadcast()
                                }
                            }
                        }
                    })
            return params
//        } catch (e: Exception) {
//            mStatus = Status.Error
//            DownloadFileService.stopDownload(params.ctx, params)
//            notifyBroadcast()
//        }
//        return params
    }

    fun getStatus(): Status {
        return mStatus!!
    }

    fun setStatus(status: Status) {
        this.mStatus = status
    }

    fun notifyBroadcast(){
        params?.result.run {
            var result : Result? = ( this as? Result)
            result?.let {
                if(it.isSuccess){
                    params.saveFileName?.let { it1 ->
                        //KVHelper.getInstance().addValue(CommonPreferences.FILE_HEADER + params.saveFileName,params.saveFileName)
                        //PreferencesFactory.getCommonPref().putString(CommonPreferences.FILE_HEADER+ params.saveFileName,params.saveFileName)
                        CommonPreferences(SharePreferencesHelper.FILE_HEADER+ params.saveFileName,it1)
                    }
                }
            }
        }
        //EventBus.getDefault().post(params)
    }

    enum class Status constructor(internal var value: Int) {
        None(0), Finished(200), Error(232), Downloading(225), Pause(226), Wait(227);

        fun value(): Int {
            return value
        }

        companion object {
            private val NONE = 0
            fun fromInt(value: Int): Status {
                when (value) {
                    200 -> return Finished
                    232 -> return Error
                    225 -> return Downloading
                    226 -> return Pause
                    227 -> return Wait
                    else -> return None
                }
            }
        }
    }
}