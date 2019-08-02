package com.tz.basicsmvp.net.api

import com.tz.basicsmvp.mvp.base.BaseJsonBean
import com.tz.basicsmvp.mvp.model.bean.MainPageBean
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/5/16 16:09
 * @Package: com.tz.basicsmvp.net
 * @Description:
 **/
interface ApiService {

    @GET("weather/query?")
    fun getWeatherData(@Query("city") id:String, @Query("citycode") citycode:String = "", @Query("cityid") cityid:String = "", @Query("location") location:String="" ): Observable<BaseJsonBean<MainPageBean>>

    @POST("v2/avatar?")
    @FormUrlEncoded
    fun getHomePageData02(@Field("avatar") avatar:String ):Observable<MainPageBean>

    @POST("v2/avatar?")
    @FormUrlEncoded
    fun getHomePageDatd03(@FieldMap maps:Map<String,String>):Observable<MainPageBean>

}