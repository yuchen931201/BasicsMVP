package com.tz.basicsmvp.mvp.view.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.tz.basicsmvp.R
import java.util.ArrayList

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/18 10:46
 * @Package: com.tz.basicsmvp.mvp.view.custom
 * @Description:  页面占位布局，加载中。。。 加载失败。。。 无网络。。。 无数据。。。
 **/
class MultipleStatusView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {
        @JvmField
        val STATUS_CONTENT = 0x00
        @JvmField
        val STATUS_LOADING = 0x01
        @JvmField
        val STATUS_EMPTY = 0x02
        @JvmField
        val STATUS_ERROR = 0x03
        @JvmField
        val STATUS_NO_NETWORK = 0x04
        @JvmStatic
        private val NULL_RESOURCE_ID = -1
        @JvmStatic
        private val DEFAULT_LAYOUT_PARAMS = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
    }

    private var mEmptyView: View? = null
    private var mErrorView: View? = null
    private var mLoadingView: View? = null
    private var mNoNetworkView: View? = null
    private var mContentView: View? = null
    private var mEmptyViewResId: Int = 0
    private var mErrorViewResId: Int = 0
    private var mLoadingViewResId: Int = 0
    private var mNoNetworkViewResId: Int = 0
    private var mContentViewResId: Int = 0

    private var mViewStatus: Int = 0
    private var mInflater: LayoutInflater? = null
    private var mOnRetryClickListener: OnClickListener? = null

    private val mOtherIds = ArrayList<Int>()

    init {
        val ta: TypedArray? = context?.obtainStyledAttributes(attrs!!, R.styleable.MultipleStatusView, defStyleAttr, 0)
        mEmptyViewResId = ta?.getResourceId(R.styleable.MultipleStatusView_app_emptyView, R.layout.view_empty)!!
        mErrorViewResId = ta.getResourceId(R.styleable.MultipleStatusView_app_errorView, R.layout.view_error)
        mLoadingViewResId = ta.getResourceId(R.styleable.MultipleStatusView_app_loadingView, R.layout.view_loading)
        mNoNetworkViewResId = ta.getResourceId(R.styleable.MultipleStatusView_app_noNetworkView, R.layout.view_no_network)
        mContentViewResId = ta.getResourceId(R.styleable.MultipleStatusView_app_contentView, NULL_RESOURCE_ID)
        ta.recycle()
        mInflater = LayoutInflater.from(getContext())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        showContent()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clear(mEmptyView, mLoadingView, mErrorView, mNoNetworkView)
        mOtherIds.clear()
        if (null != mOnRetryClickListener) {
            mOnRetryClickListener = null
        }
        mInflater = null
    }

    /**
     * 获取当前状态
     */
    fun getViewStatus(): Int {
        return mViewStatus
    }

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    fun setOnRetryClickListener(onRetryClickListener: OnClickListener) {
        this.mOnRetryClickListener = onRetryClickListener
        //让重试按钮，自动显示或隐藏
        //requestLayout()
    }

    /**
     * 显示空视图
     */
    fun showEmpty() {
        showEmpty(mEmptyViewResId, DEFAULT_LAYOUT_PARAMS)
    }

    /**
     * 显示空视图
     * @param layoutId 自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showEmpty(layoutId: Int, layoutParams: ViewGroup.LayoutParams) {
        inflateView(layoutId)?.let { showEmpty(it, layoutParams) }
    }

    /**
     * 显示空视图
     * @param view 自定义视图
     * @param layoutParams 布局参数
     */
    fun showEmpty(view: View, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Empty view is null!")
        mViewStatus = STATUS_EMPTY
        if (null == mEmptyView) {
            mEmptyView = view
            val emptyRetryView = mEmptyView?.findViewById<View>(R.id.empty_retry_view)
            if (null != mOnRetryClickListener && null != emptyRetryView) {
                emptyRetryView.visibility = View.VISIBLE
                emptyRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mEmptyView?.id?.let { mOtherIds.add(it) }
            addView(mEmptyView, 0, layoutParams)
        }
        mEmptyView?.id?.let { showViewById(it) }
    }

    /**
     * 显示错误视图
     */
    fun showError() {
        showError(mErrorViewResId, DEFAULT_LAYOUT_PARAMS)
    }

    /**
     * 显示错误视图
     * @param layoutId 自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showError(layoutId: Int, layoutParams: ViewGroup.LayoutParams) {
        inflateView(layoutId)?.let { showError(it, layoutParams) }
    }

    /**
     * 显示错误视图
     * @param view 自定义视图
     * @param layoutParams 布局参数
     */
    fun showError(view: View, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Error view is null!")
        mViewStatus = STATUS_ERROR
        if (null == mErrorView) {
            mErrorView = view
            val errorRetryView = mErrorView?.findViewById<View>(R.id.error_retry_view)
            if (null != mOnRetryClickListener && null != errorRetryView) {
                errorRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mErrorView?.id?.let { mOtherIds.add(it) }
            addView(mErrorView, 0, layoutParams)
        }
        mErrorView?.id?.let { showViewById(it) }
    }

    /**
     * 显示加载中视图
     */
    fun showLoading() {
        showLoading(mLoadingViewResId, DEFAULT_LAYOUT_PARAMS)
    }

    /**
     * 显示加载中视图
     * @param layoutId 自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showLoading(layoutId: Int, layoutParams: ViewGroup.LayoutParams) {
        inflateView(layoutId)?.let { showLoading(it, layoutParams) }
    }

    /**
     * 显示加载中视图
     * @param view 自定义视图
     * @param layoutParams 布局参数
     */
    fun showLoading(view: View, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Loading view is null!")
        mViewStatus = STATUS_LOADING
        if (null == mLoadingView) {
            mLoadingView = view
            mLoadingView?.id?.let { mOtherIds.add(it) }
            addView(mLoadingView, 0, layoutParams)
        }
        mLoadingView?.id?.let { showViewById(it) }
    }

    /**
     * 显示无网络视图
     */
    fun showNoNetwork() {
        showNoNetwork(mNoNetworkViewResId, DEFAULT_LAYOUT_PARAMS)
    }

    /**
     * 显示无网络视图
     * @param layoutId 自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showNoNetwork(layoutId: Int, layoutParams: ViewGroup.LayoutParams) {
        inflateView(layoutId)?.let { showNoNetwork(it, layoutParams) }
    }

    /**
     * 显示无网络视图
     * @param view 自定义视图
     * @param layoutParams 布局参数
     */
    fun showNoNetwork(view: View, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "No network view is null!")
        mViewStatus = STATUS_NO_NETWORK
        if (null == mNoNetworkView) {
            mNoNetworkView = view
            val noNetworkRetryView = mNoNetworkView?.findViewById<View>(R.id.no_network_retry_view)
            if (null != mOnRetryClickListener && null != noNetworkRetryView) {
                noNetworkRetryView.visibility = View.VISIBLE
                noNetworkRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mNoNetworkView?.id?.let { mOtherIds.add(it) }
            addView(mNoNetworkView, 0, layoutParams)
        }
        mNoNetworkView?.id?.let { showViewById(it) }
    }

    /**
     * 显示内容视图
     */
    fun showContent() {
        mViewStatus = STATUS_CONTENT
        if (null == mContentView && mContentViewResId != NULL_RESOURCE_ID) {
            mContentView = mInflater?.inflate(mContentViewResId, null)
            addView(mContentView, 0, DEFAULT_LAYOUT_PARAMS)
        }
        showContentView()
    }

    private fun showContentView() {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.visibility = if (mOtherIds.contains(view.id)) View.GONE else View.VISIBLE
        }
    }

    private fun inflateView(layoutId: Int): View? {
        return mInflater?.inflate(layoutId, null)
    }

    private fun showViewById(viewId: Int) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.visibility = if (view.id == viewId) View.VISIBLE else View.GONE
        }
    }

    private fun checkNull(any: Any?, hint: String) {//`object`
        if (null == any) {
            throw NullPointerException(hint)
        }
    }

    private fun clear(vararg views: View?) {
        views.let {
            for (view in it) {
                view?.run {
                    removeView(this)
                }
            }
        }
    }


}