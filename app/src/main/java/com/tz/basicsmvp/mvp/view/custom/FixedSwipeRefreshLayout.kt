package com.tz.basicsmvp.mvp.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/24 14:33
 * @Package: com.tz.basicsmvp.mvp.view.custom
 * @Description: 解决下拉刷新与ViewPager 滑动冲突的
 **/
class FixedSwipeRefreshLayout @JvmOverloads constructor(
    context : Context,
    attrs : AttributeSet): SwipeRefreshLayout(context,attrs) {

    var startY: Float = 0f
    var startX: Float = 0f

    // 记录viewPager是否拖拽的标记
    private var mIsVpDragger: Boolean = false
    private val mTouchSlop: Int by lazy {  ViewConfiguration.get(context).scaledTouchSlop; }

    init {

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ev?.run {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 记录手指按下的位置
                    startY = ev.y
                    startX = ev.x
                    // 初始化标记
                    mIsVpDragger = false
                }
                MotionEvent.ACTION_MOVE -> {
                    // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                    if (mIsVpDragger) {
                        return false
                    }

                    // 获取当前手指位置
                    val endY = ev.y
                    val endX = ev.x
                    val distanceX = Math.abs(endX - startX)
                    val distanceY = Math.abs(endY - startY)
                    // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                    if (distanceX > mTouchSlop && distanceX > distanceY) {
                        mIsVpDragger = true
                        return false
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                    // 初始化标记
                    mIsVpDragger = false
            }
        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return super.onInterceptTouchEvent(ev)
    }

}