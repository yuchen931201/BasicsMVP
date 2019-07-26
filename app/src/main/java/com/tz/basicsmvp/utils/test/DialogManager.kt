package com.tz.basicsmvp.utils.test

import android.app.Activity
import android.os.Build
import android.text.TextUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.LogUtils
import com.tz.basicsmvp.MyApp
import com.tz.basicsmvp.mvp.view.activity.MainActivity
import java.util.*

/**
 * Author: tianzhen
 * Time: 2018/11/19
 * Description:  管理弹窗队列， 一个一个弹，可以设置目标Activities, 以及目标Fragments
 * E-mail:
 */
object DialogManager {

    private val dialogList = ArrayList<Node>()
    private val mLock = Any()

    var currentShowingDialog: DialogFragment? = null

    @JvmStatic
    @JvmOverloads
    fun addToQueue(code: Int, dialogFragment: DialogFragment?, activity: List<Class<out Activity>>? = null,
                   targetFragment: List<Class<out Fragment>>? = null,
                   singleInstance: Boolean = false, tag: String = "",
                   override: Boolean = false,
                   listener: InterceptLooperListener? = null) {

        if (dialogFragment == null) return
        val lifecycle = dialogFragment.lifecycle
        lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                pop(dialogFragment)
            }
        })

        val size = dialogList.size
        if (size > 0) {
            for (i in 0 until size) {
                val node = dialogList[i]
                // 对比大小，较大的话继续轮询，直接找到一个 >code的位置插入, 否则找不到插入到队尾
                if (node.code < code) {
                    if (i == size - 1) {
                        if (addAble(code, dialogFragment, activity, targetFragment, singleInstance, tag, override)) {
                            dialogList.add(Node.newInstance(code, dialogFragment, activity, targetFragment, singleInstance, tag, override, listener))
                        }
                        break
                    }
                } else {
                    if (addAble(code, dialogFragment, activity, targetFragment, singleInstance, tag, override)) {
                        if (node.dialogFragment == currentShowingDialog) {
                            //如果当前显示dialog 为 要被插队的 node， 那么插入到后面一个位置
                            if ((i + 1) >= size) {
                                //要插入的位置大于size, 加入到最后面
                                dialogList.add(Node.newInstance(code, dialogFragment, activity, targetFragment, singleInstance, tag, override, listener))
                            } else {
                                dialogList.add(i + 1, Node.newInstance(code, dialogFragment, activity, targetFragment, singleInstance, tag, override, listener))
                            }
                        } else {
                            //插队
                            dialogList.add(i, Node.newInstance(code, dialogFragment, activity, targetFragment, singleInstance, tag, override, listener))
                        }
                    }
                    break
                }
            }
        } else {
            //直接放到第一位
            dialogList.add(Node.newInstance(code, dialogFragment, activity, targetFragment, singleInstance, tag, override, listener))
        }

        pop()
    }


    /**
     * 如果单例，则检测后再检测是否添加，否则不添加,
     */
    fun addAble(code: Int, dialogFragment: DialogFragment,
                activity: List<Class<out Activity>>?, targetFragment: List<Class<out Fragment>>?,
                singleInstance: Boolean, tag: String, override: Boolean): Boolean {
        if (!singleInstance) return true
        val node = Node.newInstance(code, dialogFragment, activity, targetFragment, true, tag, override, null)
        if (dialogList.size > 0) {
            var overrideNode: Node? = null
            for (node1 in dialogList) {
                if (node == node1) {
                    overrideNode = node1
                }
            }
            return if (override) {
                //如果覆盖之前的弹窗，那么 移除 之前的弹窗，
                if (overrideNode != null) {
                    dialogList.remove(overrideNode)
                }
                true
            } else {
                //如果不覆盖
                overrideNode == null
                //已经有弹窗存在的情况，不添加,没有相同弹窗的情况下，添加
            }
        }
        return true
    }

    @JvmStatic
    fun removeDialogsByTag(tag: String) {
        val dialogListTem = dialogList.filter { it.tag != tag }
        dialogList.clear()
        dialogList.addAll(dialogListTem)
    }

    @JvmStatic
    @JvmOverloads
    fun pop(showingDialog: DialogFragment? = null) {
        if (showingDialog != null && currentShowingDialog === showingDialog) {
            currentShowingDialog = null
            if (dialogList.size > 0) {
                dialogList.removeAt(0)
            }
        }
        if (currentShowingDialog == null) {
            synchronized(mLock) {
                if (currentShowingDialog == null) {
                    if (dialogList.size > 0) {
                        if (!interceptPop()) {
                            //满足条件下 则弹出, 并且移除
                            val fragment = dialogList[0].dialogFragment
                            currentShowingDialog = fragment
                            val activity = MyApp.instance?.currentActivity as FragmentActivity
                            try {
                                if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        !activity.isDestroyed
                                    } else {
                                        !activity.isFinishing
                                    }
                                ) {
                                    fragment.show(activity.supportFragmentManager, dialogList[0].tag)
                                }
                            } catch (e: Exception) {
                                LogUtils.e(e.message)
                            }
                        } else {
                            if (dialogList.size > 1) {
                                if (dialogList[0].interceptLooperListener != null && dialogList[0].interceptLooperListener!!.interceptLooper()) {
                                    //如果不假如队列，那么移除，并且弹出后面的弹窗
                                    dialogList.removeAt(0)
                                    if (dialogList.size > 0) {
                                        pop()
                                    }
                                } else {
                                    // 被判断为不弹出的情况下，加入到队列最后面等待...
                                    val node = dialogList.removeAt(0)
                                    dialogList.add(node)
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    /*
     * 判断各种 弹窗在什么条件下不弹出
     * */
    fun interceptPop(): Boolean {
        if (dialogList.size > 0 && MyApp.instance?.currentActivity is FragmentActivity
                && MyApp.instance?.currentActivity !is MainActivity ){ // LoginWithCodeActivity) {


            if (dialogList[0].targetActivityList != null && dialogList[0].targetActivityList!!.size > 0) {
                //如果 有目标activity
                var catched = false
                //判断当前Activity 是否 在 集合中
                for (aClass in dialogList[0].targetActivityList!!) {
                    if (aClass.simpleName == MyApp.instance?.currentActivity?.javaClass?.simpleName) {
                        catched = true
                        break
                    }
                }

                if (!catched) return true // 如果不是在目标Activity， 那么就 不显示


                if (dialogList[0].targetFragment != null && dialogList[0].targetFragment!!.size > 0) {
                    //如果有目标fragment
                    catched = false
                    //获取当前Activity 类信息
                    if (MyApp.instance?.currentActivity is ShowDialogFragmentActivity) {
                        val activity = MyApp.instance?.currentActivity as ShowDialogFragmentActivity
                        if (activity.currentFragment != null) {
                            //获取当前Activity显示的Fragment
                            val fragmentClazz = activity.currentFragment
                            for (aClass in dialogList[0].targetFragment!!) {
                                // 遍历 目标Activity，
                                if (aClass.simpleName == fragmentClazz!!.simpleName) {
                                    //如果当前Activity显示的Fragment在目标Fragment中，那么就显示
                                    catched = true
                                }
                            }
                        }
                    } else {
                        throw IllegalStateException("Target activity must implements ShowDialogFragmentActivity!")
                    }

                    if (!catched) return true //如果不是目标Fragment ，那么不显示
                }


            }

            return false
        }
        return true
    }

    interface InterceptLooperListener {
        fun interceptLooper(): Boolean
    }

    private class Node private constructor(code: Int, dialogFragment: DialogFragment,
                                           targetActivity: List<Class<out Activity>>?,
                                           targetFragment: List<Class<out Fragment>>?,
                                           singleInstance: Boolean, tag: String,
                                           override: Boolean,
                                           looperListener: InterceptLooperListener?) {
        var code: Int = 0
            internal set//優先級越高，code 越小
        var dialogFragment: DialogFragment
            internal set
        var targetActivityList: List<Class<out Activity>>? = null
            internal set  // 目标Activity
        var targetFragment: List<Class<out Fragment>>? = null
            internal set
        var isSingleInstance: Boolean = false
            internal set
        var tag: String
            internal set
        internal var override = false
        var interceptLooperListener: InterceptLooperListener? = null
            internal set

        override fun equals(obj: Any?): Boolean {
            if (obj == null) return false
            return if (obj !is Node) {
                false
            } else {
                val node = obj as Node?
                !TextUtils.isEmpty(tag) && TextUtils.equals(node!!.tag, tag) && isSingleInstance
            }
        }


        init {
            this.code = code
            this.dialogFragment = dialogFragment
            this.targetActivityList = targetActivity
            this.targetFragment = targetFragment
            this.isSingleInstance = singleInstance
            this.tag = tag
            this.override = override
            this.interceptLooperListener = looperListener
        }

        companion object {

            /**
             * @param singleInstance 是否队列保持单例
             * @param code           优先级 ，数字越小，优先级越高
             * @param targetActivity 可以在哪些界面显示
             * @param targetFragment 可以在哪些界面的哪些Fragment显示
             * @param tag            singleInstance=true 的情况下  tag相同则不添加
             *
             *
             * ps： 主页只调用一次接口的 可以不用单例， 但是评价或者题库更新等 多触发条件的 需要单例模式，
             */
            fun newInstance(code: Int, dialogFragment: DialogFragment,
                            targetActivity: List<Class<out Activity>>?, targetFragment: List<Class<out Fragment>>?, singleInstance: Boolean, tag: String, override: Boolean, listener: InterceptLooperListener?): Node {
                return Node(code, dialogFragment, targetActivity, targetFragment, singleInstance, tag, override, listener)
            }
        }
    }

    interface ShowDialogFragmentActivity {
        val currentFragment: Class<out Fragment>?
    }

}
