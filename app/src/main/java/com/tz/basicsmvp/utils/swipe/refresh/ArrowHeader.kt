package com.tz.basicsmvp.utils.swipe.refresh

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.billy.android.swipe.SmartSwipeRefresh
import com.tz.basicsmvp.R
import com.wuyr.arrowdrawable.ArrowDrawable

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/8/5 13:39
 * @Package: com.tz.basicsmvp.utils.swipe.refresh
 * @Description:
 **/

class ArrowHeader @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr), SmartSwipeRefresh.SmartSwipeRefreshHeader {

    var drawable: ArrowDrawable? = null
        private set
    var initializer: IArrowInitializer? = null
    private val bowColor: Int
    private val arrowColor: Int
    private val stringColor: Int
    private val lineColor: Int
    private val bowLength: Int

    internal var measured: Boolean = false

    private val hitAnimationDuration: Float
        get() = drawable!!.getHitDuration() + drawable!!.getSkewDuration() * 8 + 400

    private val missAnimationDuration: Int
        get() = drawable?.missDuration as Int + 100

    interface IArrowInitializer {
        fun onArrowInit(arrowHeader: ArrowHeader, arrowDrawable: ArrowDrawable)
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ArrowHeader, defStyleAttr, 0)
        bowColor = a.getColor(R.styleable.ArrowHeader_bowColor, Color.GRAY)
        arrowColor = a.getColor(R.styleable.ArrowHeader_arrowColor, Color.GRAY)
        stringColor = a.getColor(R.styleable.ArrowHeader_stringColor, Color.GRAY)
        lineColor = a.getColor(R.styleable.ArrowHeader_lineColor, Color.GRAY)
        bowLength = a.getDimensionPixelSize(R.styleable.ArrowHeader_bowLength, 0)
        a.recycle()
    }

    override fun getView(): View {
        return this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!measured) {
            measured = true
            val _int : Int =  if (bowLength > 0) bowLength else (measuredWidth * .3f).toInt()
            drawable = ArrowDrawable.create( this, measuredWidth, measuredHeight, _int )
            if (bowColor != 0) {
                drawable?.bowColor = bowColor
            }
            if (arrowColor != 0) {
                drawable?.setArrowColor(arrowColor)
            }
            if (stringColor != 0) {
                drawable?.setStringColor(stringColor)
            }
            if (lineColor != 0) {
                drawable?.setLineColor(lineColor)
            }
            drawable?.reset()
            if (initializer != null) {
                initializer?.onArrowInit(this, drawable!!)
            }
        }
    }

    override fun onInit(horizontal: Boolean) {}

    override fun onStartDragging() {}

    override fun onReset() {
        if (drawable != null) {
            drawable?.reset()
        }
    }

    override fun onProgress(dragging: Boolean, progress: Float) {
        val value: Float
        if (progress <= .5f) {
            value = progress / 2
        } else if (progress <= .75f) {
            value = progress - .25f
        } else {
            value = (progress - .5f) * 2
        }
        drawable?.setProgress(value)
        invalidate()
    }

    override fun onFinish(success: Boolean): Long {
        if (success) {
            drawable?.hit()
            return hitAnimationDuration.toLong()
        } else {
            drawable?.miss()
            return missAnimationDuration.toLong()
        }
    }

    override fun onDataLoading() {
        drawable?.fire()
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable != null) {
            drawable?.draw(canvas)
        }
        invalidate()
    }
}