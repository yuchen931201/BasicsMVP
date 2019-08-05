package com.tz.basicsmvp.mvp.view.activity

import android.content.Context
import android.content.Intent
import com.tz.basicsmvp.R
import com.tz.basicsmvp.mvp.base.BaseActivity

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
    }

    override fun doScene() {

    }


}