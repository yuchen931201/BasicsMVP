package com.tz.basicsmvp.mvp.base

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.tz.basicsmvp.mvp.view.custom.MultipleStatusView
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission


/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/18 14:04
 * @Package:
 * @Description: com.tz.basicsmvp.mvp.base
 **/
abstract class BaseActivity : AppCompatActivity() ,IBaseActivity {

    companion object {
        const val TYPE_TITLE_NORMAL = 0
        const val TYPE_FULL_SCREEN = 1
    }

    val uiController: IBaseUIController by lazy { BaseUIController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRootView()
    }

    override fun onDestroy() {
        super.onDestroy()
        uiController.onDestroy()
    }

    private fun setRootView() {
        uiController.initActivity()
        //doScene()
    }

    protected fun getStatusView(): MultipleStatusView? {
        return uiController.getStatusView()
    }

    /** 打开软键盘 */
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
    fun requestPermissionForResult(unauthorized: Action<List<String>>, @NonNull vararg permissions: String) {
        AndPermission.with(this).runtime().permission(permissions)
            .onGranted {}.onDenied(unauthorized).start()
    }

    fun goSysSetting(requestCode: Int) {
        AndPermission.with(this).runtime().setting().start(requestCode)
    }


}


