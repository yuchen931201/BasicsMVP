package com.tz.basicsmvp.mvp.model.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WeatherBean(
    @SerializedName("city") var city: String? = null,
    @SerializedName("cityid") var cityid: String? = null,
    @SerializedName("citycode") var citycode: String? = null,
    @SerializedName("week") var week: String? = null,
    @SerializedName("weather") var weather: String? = null,
    @SerializedName("temp") var temp: String? = null,
    @SerializedName("temphigh") var temphigh: String? = null,
    @SerializedName("templow") var templow: String? = null,
    @SerializedName("img") var img: String? = null,
    @SerializedName("humidity") var humidity: String? = null,
    @SerializedName("pressure") var pressure: String? = null,
    @SerializedName("windspeed") var windspeed: String? = null,
    @SerializedName("winddirect") var winddirect: String? = null,
    @SerializedName("windpower") var windpower: String? = null,
    @SerializedName("updatetime") var updatetime: String? = null,

    @SerializedName("index") var index: List<IndexItem>? = null,
    @SerializedName("aqi") var aqi: APIItem? = null
) : Serializable {

    @SerializedName("names")
    val names: Array<String>? = null

    data class IndexItem(
        @SerializedName("iname") var iname: String,
        @SerializedName("ivalue") var ivalue: String, @SerializedName("detail") var detail: String
    ) : Serializable

    data class APIItem(
        @SerializedName("so2") var so2: String
        , @SerializedName("so224") var so224: String
        , @SerializedName("primarypollutant") var primarypollutant: String
        , @SerializedName("quality") var quality: String
        , @SerializedName("timepoint") var timepoint: String
        , @SerializedName("aqiinfo") var aqiinfo: AqiInfo
    ) : Serializable {
        data class AqiInfo(
            @SerializedName("level") var level: String,
            @SerializedName("color") var color: String,
            @SerializedName("affect") var affect: String,
            @SerializedName("measure") var measure: String
        ) : Serializable
    }
}