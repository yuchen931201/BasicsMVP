package com.tz.basicsmvp.mvp.base

import android.os.Bundle
//import android.support.annotation.LayoutRes
import androidx.annotation.LayoutRes
//import android.support.v4.app.Fragment
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import com.tz.basicsmvp.MyApp
import com.tz.basicsmvp.mvp.view.custom.MultipleStatusView
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
//import io.reactivex.annotations.NonNull

/**
* @ComputerCode: tianzhen
* @Author: TianZhen
* @QQ: 959699751
* @CreateTime: Created on 2019/5/18 14:04
* @Package:
* @Description: com.tz.basicsmvp.mvp.base
**/

 abstract class BaseFragment: Fragment(){

    /**
     * 视图是否加载完毕
     */
    private var isViewPrepare = false
    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false
    /**
     * 多种状态的 View 的切换
     */
    protected var mLayoutStatusView: MultipleStatusView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(),null)
    }



    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewPrepare = true
        initView()
        lazyLoadDataIfPrepared()
        //多种状态切换的view 重试点击事件
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        lazyLoad()
    }


    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun getLayoutId():Int

    /**
     * 初始化 ViewI
     */
    abstract fun initView()

    /**
     * 懒加载
     */
    abstract fun lazyLoad()

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * @param permissions Permission.Group.PHONE  or  Manifest.permission.CALL_PHONE
     * */
    fun requestPermissionForResult(unauthorized : Action<List<String>>, @NonNull vararg permissions :String ){
        AndPermission.with(this).runtime().permission(permissions)
            .onGranted{}.onDenied(unauthorized).start()
    }

    fun goSysSetting(requestCode: Int){
        AndPermission.with(this).runtime().setting().start(requestCode)
    }
}
