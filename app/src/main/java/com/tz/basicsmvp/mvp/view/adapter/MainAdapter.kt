package com.tz.basicsmvp.mvp.view.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tz.basicsmvp.R

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/8/6 19:33
 * @Package: com.tz.basicsmvp.mvp.view.adapter
 * @Description:
 **/
class MainAdapter<T, K : BaseViewHolder> : BaseQuickAdapter<T, K> {

    constructor(layoutResId: Int, data: List<T>) : super(layoutResId,data)

    override fun convert(helper: K, item: T) {
        helper.setText(R.id.tv_name,(item as MainData).name )
    }

}

data class MainData(val name:String)