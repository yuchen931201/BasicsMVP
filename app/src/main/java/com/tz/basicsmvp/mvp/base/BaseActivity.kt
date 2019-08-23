package com.tz.basicsmvp.mvp.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.tz.basicsmvp.mvp.view.widget.MultipleStatusView
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
        const val TYPE_TITLE_MAIN = 2

        private const val TOOLBAR_HEIGHT = 48 + 1 //dp 由设计师决定, 1dp是title的线
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
            TYPE_TITLE_NORMAL,TYPE_TITLE_MAIN->{
                initToolBar()
            }
        }
        onFinishCreateView()
    }

    /**
     *
     * (toolbar适配):
     * 经测试不同手机商的StatusBarHeight都不尽相同,虽然values中已经设了固定dp,但为了适配所有手机(例如刘海屏),这里还需要动态设定
     *
     * (普通UI适配):
     * 这里在多说一句,如何与设计师的px做适配,其实只要设计师的画布为苹果2X图(即宽:750px) 例如一个控件设计师宽高为 200px * 100px
     * 在xml中我们可以设置为 100dp * 50dp
     *
     * (常见手机电量栏高度):
     * oppo               18dp
     * 小米                20dp
     * android4.4模拟器    25dp
     * android6.0模拟器    24dp
     *
     * 但是此方式{@Link BaseUIController#getStatusBarHeight} 获取系统电量栏高度也不安全,只能等到Google修改API再改了
     * */
    private fun initToolBar(){
        uiController.getToolbar()?.run {
            this.setPadding(0,uiController.getStatusBarHeight(),0,0)
            this.layoutParams.height = (uiController.getStatusBarHeight() +
                    ScreenUtils.dp2px(this@BaseActivity,TOOLBAR_HEIGHT.toFloat())).toInt()
            setSupportActionBar(this)
        }
    }


    protected fun getStatusView(): MultipleStatusView? {
        return uiController.getStatusView()
    }

    protected fun setTitle(s: String){
        uiController.setTitle(s)
    }

    fun setLeftImage(any: Any){
        uiController.setLeftImage(any)
    }

    fun setLeftClick(click: View.OnClickListener){
        uiController.setLeftClick(click)
    }

    protected fun setRightImage(any: Any){
        uiController.setRightImage(any)
    }

    protected fun setRightClick(click: View.OnClickListener){
        uiController.setRightClick(click)
    }

    protected fun setLineColor(color: Int){
        uiController.setLineColor(color)
    }

    protected fun setToolbarColor(color: Int){
        uiController.setToolbarColor(color)
    }

    protected fun setTitleBarColor(color: Int){
        uiController.setTitleBarColor(color)
    }

    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     * @param dark 状态栏字体和图标是否为深色
     */
    protected fun setStatusBarFontDark(dark: Boolean) {
        uiController.setStatusBarFontDark(dark)
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


