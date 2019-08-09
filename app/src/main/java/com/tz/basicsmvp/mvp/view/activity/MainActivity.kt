package com.tz.basicsmvp.mvp.view.activity

import android.annotation.SuppressLint
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
//        text_view_s.setOnClickListener {
//            WeatherActivity.enter(this)
//        }
        mData = arrayListOf()
        mData?.run {
            for(i in 0..4 step 1 ){
                when(i){
                    0 -> this.add(MainData("查看上海天气"))
                    1 -> this.add(MainData("无状态栏"))
                }
            }
            mainAdapter = MainAdapter(R.layout.adapter_main_item,this)
            main_recycler.layoutManager = LinearLayoutManager(this@MainActivity)
            main_recycler.adapter = mainAdapter
//            tv_status_bar.setOnClickListener {
//                StatusBarActivity.enter(this@MainActivity)
//            }
        }
        UserPreferences(UserPreferences.KEY_USER_NAME,"123")
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
