package com.tz.basicsmvp.mvp.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.tz.basicsmvp.R
import com.tz.basicsmvp.mvp.base.BaseActivity
import com.tz.basicsmvp.utils.UserPreferences
import com.tz.basicsmvp.utils.core.listener.OnImageListener
import com.tz.basicsmvp.utils.core.listener.OnProgressListener
import com.tz.basicsmvp.utils.core.strategy.loadImage
import com.tz.basicsmvp.utils.core.strategy.loadProgress
import kotlinx.android.synthetic.main.activity_status_bar.*

class StatusBarActivity :BaseActivity(){

    companion object{

        fun enter(ctx: Context){
            val  i = Intent(ctx, StatusBarActivity::class.java)
            ctx.startActivity(i)
        }
    }

    override fun layoutType(): Int {
        return TYPE_FULL_SCREEN
    }

    override fun layoutId(): Int {
        return R.layout.activity_status_bar
    }

    override fun onFinishCreateView() {
        setStatusBarFontDark(false)
        initView()
    }

    fun initView(){
        //tv_progress.text
        var url = "http://d-pic-image.yesky.com/1080x-/uploadImages/2019/044/59/1113V6L3Q6TY.jpg"
        var src = R.mipmap.image15
        loadImage(url, no_status_image)
        tv_progress.text = UserPreferences.prefs.getString(UserPreferences.KEY_USER_NAME,"")
    }

    override fun doScene() {

    }


}