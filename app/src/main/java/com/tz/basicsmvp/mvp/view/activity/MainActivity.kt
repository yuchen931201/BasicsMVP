package com.tz.basicsmvp.mvp.view.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseViewHolder
import com.tz.basicsmvp.R
import com.tz.basicsmvp.mvp.Contract.MainContract
import com.tz.basicsmvp.mvp.base.BaseActivity
import com.tz.basicsmvp.mvp.model.bean.MainPageBean
import com.tz.basicsmvp.mvp.presenter.MainPagePersenter
import com.tz.basicsmvp.mvp.view.adapter.MainAdapter
import com.tz.basicsmvp.mvp.view.adapter.MainData
import com.tz.basicsmvp.utils.UserPreferences
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainContract.View{


    private val mPresenter by lazy { MainPagePersenter() }
    private var mainAdapter:MainAdapter<MainData, BaseViewHolder>? = null
    private var mData:MutableList<MainData>? = null

    init {
        mPresenter.attachView(this)
    }

    override fun layoutType(): Int {
        return TYPE_FULL_SCREEN
    }

    override fun layoutId(): Int { return R.layout.activity_main }

    override fun onFinishCreateView() {
//        text_view_s.text = "我是测试文字，静态设置成功"
        //getStatusView()?.showEmpty()
        mData = arrayListOf()
        mData?.run {
            for(i in 0..24 step 1 ){
                when(i){
                    0 -> this.add(MainData("查看上海天气$i",R.mipmap.image01))
                    1 -> this.add(MainData("无状态栏$i",R.mipmap.image02))
                    2 -> this.add(MainData("test$i",R.mipmap.image03))
                    3 -> this.add(MainData("test$i",R.mipmap.image04))
                    4 -> this.add(MainData("test$i",R.mipmap.image05))
                    5 -> this.add(MainData("test$i",R.mipmap.image06))
                    6 -> this.add(MainData("test$i",R.mipmap.image07))
                    7 -> this.add(MainData("test$i",R.mipmap.image08))
                    8 -> this.add(MainData("test$i",R.mipmap.image09))
                    9 -> this.add(MainData("test$i",R.mipmap.image10))
                    10 -> this.add(MainData("test$i",R.mipmap.image11))
                    11 -> this.add(MainData("test$i",R.mipmap.image12))
                    12 -> this.add(MainData("test$i",R.mipmap.image13))
                    13 -> this.add(MainData("test$i",R.mipmap.image14))
                    14 -> this.add(MainData("test$i",R.mipmap.image15))
                    15 -> this.add(MainData("test$i",R.mipmap.image16))
                    16 -> this.add(MainData("test$i",R.mipmap.image17))
                    17 -> this.add(MainData("test$i",R.mipmap.image18))
                    18 -> this.add(MainData("test$i",R.mipmap.image19))
                    19 -> this.add(MainData("test$i",R.mipmap.image20))
                    20 -> this.add(MainData("test$i",R.mipmap.image21))
                    21 -> this.add(MainData("test$i",R.mipmap.image22))
                    22 -> this.add(MainData("test$i",R.mipmap.image23))
                    23 -> this.add(MainData("test$i",R.mipmap.image24))
                    24 -> this.add(MainData("test$i",R.mipmap.image25))
                }
            }
            mainAdapter = MainAdapter(R.layout.adapter_main_item,this)
            setAdapterOnclick()
            main_recycler.layoutManager = LinearLayoutManager(this@MainActivity)
            main_recycler.adapter = mainAdapter
        }
        UserPreferences(UserPreferences.KEY_USER_NAME,"123")
    }

    private fun setAdapterOnclick() {
        mainAdapter?.setOnItemChildClickListener { adapter, view, position ->
            when(position){
                0-> WeatherActivity.enter(this@MainActivity)
                1-> StatusBarActivity.enter(this@MainActivity)
            }
        }
       val header :View = LayoutInflater.from(this).inflate(R.layout.adapter_header,null)
        mainAdapter?.addHeaderView(header)
    }

    override fun doScene() {
//        mPresenter.doSceneGetData("上海")
    }

    @SuppressLint("SetTextI18n")
    override fun setMainPageData(mpb: MainPageBean) {
//        getStatusView()?.showContent()
//        text_view_s.text = "我是服务器返回文字，动态设置成功\n${GsonUtils.toJson(mpb)}"

    }

    override fun showNoData() {
    }

    override fun showNetError() {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}
