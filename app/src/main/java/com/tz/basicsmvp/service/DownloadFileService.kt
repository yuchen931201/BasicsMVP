package com.tz.basicsmvp.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.collection.ArrayMap
import cn.eclicks.drivingtest.pool.*
import com.blankj.utilcode.util.LogUtils
import com.tz.basicsmvp.utils.SharePreferencesHelper
import java.util.concurrent.Future

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/22 9:47
 * @Package: cn.eclicks.drivingtest.download
 * @Description:
 **/
class DownloadFileService : Service() {

    companion object{

        val ACTION_START = "cn.eclicks.drivingtest.download.DownloadFileService.start"
        val ACTION_STOP = "cn.eclicks.drivingtest.download.DownloadFileService.stop"
        val ACTION_REMOVE = "cn.eclicks.drivingtest.download.DownloadFileService.remove"

        val EXTRA_DATA = "extra_data"

        val NAME_RUNNABLE_TASK = "Runnable task"
        val NAME_CALLABLE_TASK = "Callable task"
        val NAME_ASYNC_TASK = "Async task"

        fun startDownload(ctx: Context?, bundle: PoolBundle?){
            var intent = Intent(ctx,DownloadFileService::class.java)
            intent.action = (ACTION_START)
            intent.putExtra(EXTRA_DATA,bundle)
            ctx?.startService(intent)
        }

        fun stopDownload(ctx: Context?, bundle: PoolBundle?){
            var intent = Intent(ctx,DownloadFileService::class.java)
            intent.action = (ACTION_STOP)
            intent.putExtra(EXTRA_DATA,bundle)
            ctx?.startService(intent)
        }

        fun removeDownload(ctx: Context?, bundle: PoolBundle?){
            var intent = Intent(ctx,DownloadFileService::class.java)
            intent.action = (ACTION_REMOVE)
            intent.putExtra(EXTRA_DATA,bundle)
            ctx?.startService(intent)
        }
    }


    val binder: LocalBinder = LocalBinder()
    internal var executor: EasyThread? = null
    internal var mRequests: ArrayMap<String, Future<PoolBundle>> = ArrayMap()
    internal var mTasks: ArrayMap<String,NormalTask> = ArrayMap()

    override fun onCreate() {
        super.onCreate()
        executor = EasyThread.Builder
                .createFixed(4)
                .setPriority(Thread.MAX_PRIORITY)
                .setCallback(LogCallback())
                .build()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.run {
            this.action?.let {
                val bundle :PoolBundle = this.getParcelableExtra(EXTRA_DATA)
                bundle?.run {
                    when(it){
                        ACTION_START ->{
                            handleStart(bundle)
                        }
                        ACTION_STOP->{
                            handleStop(bundle)
                        }
                        ACTION_REMOVE->{
                            handleRemove(bundle)
                        }
                        else -> {
                            LogUtils.e("action error")
                        }
                    }
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun handleStart(bundle: PoolBundle?){
        bundle?.run{
            this.unique?.let {
                var task: NormalTask?
                if(this.ctx == null){
                    this.ctx = this@DownloadFileService
                }
                if(!mTasks.containsKey(it)){ //开始
                    task = NormalTask(this)
                    mTasks[it] = task
                }else{ //继续
                    task = mTasks[it]
                    if(task?.getStatus() == NormalTask.Status.Error || task?.getStatus() == NormalTask.Status.None
                            || task?.getStatus() == NormalTask.Status.Pause) {
                        task?.setStatus(NormalTask.Status.Wait)
                    }else{ }
                }
                task?.notifyBroadcast()
                //下载前必须要初始化数据库缓存，置空
//                KVHelper.getInstance().addValue(CommonPreferences.FILE_HEADER + this.saveFileName,"")
//                PreferencesFactory.getCommonPref().putString(CommonPreferences.FILE_HEADER+ this.saveFileName,"")
                //SharePreferencesHelper<String>("","")
                if(!mRequests.containsKey(it)){//新添加一个下载任务
                    mRequests[it] = callableTask(task!!)
                }
            }
        }
    }

    private fun handleStop(bundle: PoolBundle?){
        bundle?.unique?.let {
            if(mRequests.containsKey(it)){
                var future = mRequests[it]
                future?.run {
                    if(!isCancelled){
                        cancel(true)
                    }
                    mRequests.remove(it)
                }
            }
            if(mTasks.containsKey(it)){
                var task = (mTasks[it])
                task?.setStatus(NormalTask.Status.Pause)
                task?.notifyBroadcast()
            }
        }
    }

    private fun handleRemove(bundle :PoolBundle?){
        bundle?.unique?.let {
            if(mRequests.containsKey(it)){
                var future = mRequests[it]
                future?.run {
                    if(!isCancelled){
                        cancel(true)
                    }
                    mRequests.remove(it)
                }
            }
            if(mTasks.containsKey(it)){
                mTasks.remove(it)
            }
        }
    }

    private fun callableTask(task :NormalTask):Future<PoolBundle>{
        var future :Future<PoolBundle>? = null
        executor?.setName(NAME_CALLABLE_TASK)?.run {
            task?.let {
                future = this.submit(it)
            }
        }
        return future!!
    }

    private fun syncCallableTask(bundle :PoolBundle){
        executor?.setName(NAME_CALLABLE_TASK)?.run {
            var future :Future<PoolBundle> = this.submit(NormalTask(bundle))
            try {
                var bundle :PoolBundle = future.get()
            }catch (e:Exception){
                e.printStackTrace()
                LogUtils.e("bundle 获取异常！！！")
            }
        }
    }

    private fun asyncTask(bundle :PoolBundle){//异步执行
         executor?.setName(NAME_ASYNC_TASK)?.async(NormalTask(bundle), object: AsyncCallback<PoolBundle> {
            override fun onSuccess(bundle: PoolBundle) {

            }

            override fun onFailed(throwable: Throwable) {

            }

        })
    }

    private fun runnableTask(bundle :PoolBundle){
        executor?.setName(NAME_RUNNABLE_TASK)?.execute(NormalTask(bundle))
    }

    inner class LocalBinder : Binder() {
        val service: DownloadFileService by lazy {this@DownloadFileService}
    }

    inner class LogCallback: Callback{
        override fun onError(threadName: String, t: Throwable) {
            LogUtils.e(String.format("[任务%s]/[回调线程%s]执行失败: %s", threadName, Thread.currentThread(), t.message),t)
        }

        override fun onCompleted(threadName: String) {
            LogUtils.i(String.format("[任务%s]/[回调线程%s]执行完毕：", threadName, Thread.currentThread()))
        }

        override fun onStart(threadName: String) {
            LogUtils.i(String.format("[任务%s]/[回调线程%s]开始执行：", threadName, Thread.currentThread()))
        }
    }

}