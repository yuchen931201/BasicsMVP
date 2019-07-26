package com.tz.basicsmvp.mvp.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.tz.basicsmvp.mvp.view.custom.MultipleStatusView
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import io.reactivex.annotations.NonNull


/**
* @ComputerCode: tianzhen
* @Author: TianZhen
* @QQ: 959699751
* @CreateTime: Created on 2019/5/18 14:04
* @Package: 
* @Description: com.tz.basicsmvp.mvp.base
**/
abstract class BaseActivity : AppCompatActivity() {
    /**
     * 多种状态的 View 的切换
     */
    protected var mLayoutStatusView: MultipleStatusView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        initData()
        initView()
        start()
        initListener()
    }

    private fun initListener() {
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        start()
    }


    /**
     *  加载布局
     */
    abstract fun layoutId(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化 View
     */
    abstract fun initView()

    /**
     * 开始请求
     */
    abstract fun start()

    /**
     * 打开软键盘
     */
    fun openKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * 关闭软键盘
     */
    fun closeKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    /**
     * @param permissions Permission.Group.PHONE  or  Manifest.permission.CALL_PHONE
     * */
    fun requestPermissionForResult(unauthorized :Action<List<String>>,@NonNull vararg permissions :String ){
        AndPermission.with(this).runtime().permission(permissions)
            .onGranted{}.onDenied(unauthorized).start()
    }

    fun goSysSetting(requestCode: Int){
        AndPermission.with(this).runtime().setting().start(requestCode)
    }



}


