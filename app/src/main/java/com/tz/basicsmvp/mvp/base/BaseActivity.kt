package com.tz.basicsmvp.mvp.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.tz.basicsmvp.mvp.view.custom.MultipleStatusView
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import android.view.WindowManager
import com.tz.basicsmvp.utils.ScreenUtils


/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/18 14:04
 * @Package:
 * @Description: com.tz.basicsmvp.mvp.base
 **/
abstract class BaseActivity : AppCompatActivity(), IBaseActivity {

    companion object {
        const val TYPE_TITLE_NORMAL = 0
        const val TYPE_FULL_SCREEN = 1

        private const val TOOLBAR_PADDING_TOP = 25 //dp
        private const val TOOLBAR_HEIGHT = 73-25 //dp 83-25
    }

    val uiController: IBaseUIController by lazy { BaseUIController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
    }

    private fun initActivity() {
        super.setContentView(uiController.getRootView())

        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val localLayoutParams = window.attributes
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
        }
        when(layoutType()){
            TYPE_TITLE_NORMAL->{
                initToolBar()
            }
        }
        onFinishCreateView()
    }

    /**
     * 经测试不同手机厂商的StatusBarHeight都不尽相同
     * oppo               18dp
     * 小米                20dp
     * android4.4模拟器    25dp
     * android6.0模拟器    24dp
     * 但是此方式 status_bar_height 获取系统电量栏高度也不安全,只能等到Google修改API再改了
     * */
    private fun initToolBar(){
        uiController.getToolbar()?.run {
            this.setPadding(0,getStatusBarHeight(),0,0)
            this.layoutParams.height = (getStatusBarHeight() + ScreenUtils.dp2px(this@BaseActivity,TOOLBAR_HEIGHT.toFloat())).toInt()
            setSupportActionBar(this)
        }
    }


    protected fun getStatusView(): MultipleStatusView? {
        return uiController.getStatusView()
    }

    protected fun setTitle(s: String){
        uiController.setTitle(s)
    }

    fun getStatusBarHeight(): Int {
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        //LogUtils.d("CompatToolbar", "状态栏高度：" + px2dp(statusBarHeight.toFloat()) + "dp")
        return statusBarHeight
    }

    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     *
     * @param dark 状态栏字体和图标是否为深色
     */
    protected fun setStatusBarFontDark(dark: Boolean) {
        // 小米MIUI
        try {
            val window = window
            val clazz = getWindow().javaClass
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField =
                clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {       //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 魅族FlymeUI
        try {
            val window = window
            val lp = window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            if (dark) {
                value = value or bit
            } else {
                value = value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            window.attributes = lp
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // android6.0+系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        uiController.onDestroy()
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


