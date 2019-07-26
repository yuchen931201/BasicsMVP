package com.tz.basicsmvp.mvp.model.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/18 15:12
 * @Package: com.tz.basicsmvp.mvp.model.bean
 * @Description:
 **/
data class MainPageBean (@SerializedName("banners") var banners: List<BannerItem>? = null,
@SerializedName("nickName")var nickName: String? = null) : Serializable {

    @SerializedName("names") val names: Array<String>? = null

    data class BannerItem( @SerializedName("ad_name")var ad_name: String,
                           @SerializedName("ad_id") var ad_id: String) : Serializable



}