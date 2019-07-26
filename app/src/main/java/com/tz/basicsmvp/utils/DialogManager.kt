package com.tz.basicsmvp.utils

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.LogUtils
import java.lang.Exception
import java.util.*

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/15 13:20
 * @Package: com.fzcx.supercoach.assistant
 * @Description: Dialog管理类，所有弹窗都应该用此show，包含但不限于（广告运营弹窗，礼券红包弹窗，任务消息弹窗，权限弹窗，温馨提示...）
 *           Java   ->  DialogManager.Companion.getInstance().addDialog(null).show();
 *           Kotlin ->  DialogManager.getInstance().addDialog(null).show()
 * TODO (1)优先级权重 （2）固定时间限制次数  (3)是否将上一个弹框dismiss,dismiss后是否在合适时期再弹出 （4）弹出是否要对比当前界面context
 *
 **/
class DialogManager {

    private var queue: DialogQueue? = null
    private var currentDialog: Any? = null
    private var fm: FragmentManager? = null

    constructor() {
        queue = DialogQueue()
    }

    companion object {

        val LEVEL_WEIGHT_DEFAULT :Int = 0    // 默认权重
        val LEVEL_WEIGHT_COMMON  :Int = 1
        val LEVEL_WEIGHT_MIDDLE  :Int = 2
        val LEVEL_WEIGHT_HIGHEST :Int = 3

        fun getInstance(): DialogManager {
            return DialogManagerHolder.dm
        }
    }

    private object DialogManagerHolder {
        internal val dm = DialogManager()
    }

    fun addDialog(dialog: Any?): DialogManager {
        if (dialog is Dialog || dialog is DialogFragment) {
            queue?.offer(dialog)
        } else {
            throw Exception("must be dialog or dialogFragment !")
        }
        return getInstance()
    }

    fun addDialog(vararg dialogs: Any?): DialogManager {
        for(dialog in dialogs){
            if(dialog is Dialog || dialog is DialogFragment){
                queue?.offer(dialog)
            }else{
                throw Exception("must be dialog or dialogFragment !")
            }
        }
        return getInstance()
    }

    fun addDialogs(dialogs: List<Any>?): DialogManager {
        if (dialogs != null) {
            for (dialog in dialogs) {
                if (dialog is Dialog || dialog is DialogFragment) {
                    queue?.offer(dialog)
                } else {
                    throw Exception("must be dialog or dialogFragment !")
                }
            }
        }
        return getInstance()
    }

    fun show() {
        show(null)
    }

    fun show(fm: FragmentManager?) {
        fm?.run {
            getInstance()
            this@DialogManager.fm = this
        }
        if (currentDialog == null) {
            currentDialog = queue?.poll()
            if (currentDialog is Dialog) {
                (currentDialog as? Dialog)?.show()
                (currentDialog as? Dialog)?.setOnDismissListener {
                    currentDialog = null
                    show(fm)
                }
            } else if (currentDialog is DialogFragment) {
                val dia: DialogFragment? = (currentDialog as? DialogFragment)
                dia?.show(fm, dia.javaClass.simpleName)
                dia?.lifecycle?.addObserver(object: LifecycleObserver{
                    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                    fun onDestory(){

                    }
                })
                fm?.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                        super.onFragmentDestroyed(fm, f)
                        currentDialog = null
                        show(fm)
                    }
                }, true)
            }
        }else{
            LogUtils.i("已存在弹框！")
        }
    }
}


internal class DialogQueue {


    internal var list: Queue<Any> = LinkedList()

    internal var highest_list: Queue<Any> = LinkedList()
    internal var middle_list: Queue<Any> = LinkedList()
    internal var common_list: Queue<Any> = LinkedList()
    internal var default_list: Queue<Any> = LinkedList()

    internal fun offer(obj: Any?) {
        list.offer(obj)
    }

    internal fun poll(): Any? {
        return if (list.size > 0) {
            list.poll()
        } else null
    }
}