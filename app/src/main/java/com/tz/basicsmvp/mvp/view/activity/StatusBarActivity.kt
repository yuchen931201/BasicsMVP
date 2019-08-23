package com.tz.basicsmvp.mvp.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.contrarywind.listener.OnItemSelectedListener
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

//        WheelView wheelView = findViewById(R.id.wheelview);

        wheel_view01.setCyclic(false)

        val mOptionsItems01 = arrayListOf<String>()
        mOptionsItems01.add("item0")
        mOptionsItems01.add("item1")
        mOptionsItems01.add("item2")
        mOptionsItems01.add("item3")
        mOptionsItems01.add("item4")
        mOptionsItems01.add("item5")
        mOptionsItems01.add("item6")
        mOptionsItems01.add("item7")
        mOptionsItems01.add("item8")
        mOptionsItems01.add("item9")
        wheel_view01.adapter = ArrayWheelAdapter(mOptionsItems01)
        wheel_view01.setOnItemSelectedListener { index -> Toast.makeText(this@StatusBarActivity, "aaa" + mOptionsItems01.get(index), Toast.LENGTH_SHORT).show(); };

        val mOptionsItems02 = arrayListOf<String>()
        mOptionsItems02.add("item10")
        mOptionsItems02.add("item11")
        mOptionsItems02.add("item12")
        mOptionsItems02.add("item13")
        mOptionsItems02.add("item14")
        mOptionsItems02.add("item15")
        mOptionsItems02.add("item16")
        mOptionsItems02.add("item17")
        mOptionsItems02.add("item18")
        mOptionsItems02.add("item19")
        wheel_view02.adapter = ArrayWheelAdapter(mOptionsItems02)
        wheel_view02.setOnItemSelectedListener { index -> Toast.makeText(this@StatusBarActivity, "aaa" + mOptionsItems02.get(index), Toast.LENGTH_SHORT).show(); };

    }

    override fun doScene() {

    }


}