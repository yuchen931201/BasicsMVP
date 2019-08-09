package com.tz.basicsmvp.mvp.view.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tz.basicsmvp.R
import com.tz.basicsmvp.utils.core.listener.OnImageListener
import com.tz.basicsmvp.utils.core.strategy.ImageOptions
import com.tz.basicsmvp.utils.core.strategy.loadBlur
import com.tz.basicsmvp.utils.core.strategy.loadImage
import com.tz.basicsmvp.utils.core.utils.ImageConstant

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/8/6 19:33
 * @Package: com.tz.basicsmvp.mvp.view.adapter
 * @Description:
 **/
class MainAdapter<T, K : BaseViewHolder> : BaseQuickAdapter<T, K> {

    constructor(layoutResId: Int, data: List<T>) : super(layoutResId, data)

    override fun convert(helper: K, item: T) {
        val idata = (item as MainData)
        helper.setText(R.id.tv_name, idata.name)
        val image_bg = helper.getView<ImageView>(R.id.image_bg)
        loadImage(idata.resId, image_bg)
        helper.addOnClickListener(R.id.card_view)
    }

}

data class MainData(val name: String, val resId: Int)