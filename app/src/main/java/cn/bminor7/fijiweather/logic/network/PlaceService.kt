package cn.bminor7.fijiweather.logic.network

import cn.bminor7.fijiweather.FijiWeatherApplication
import cn.bminor7.fijiweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService  {
    //@GET("v2.6/place?token=${FijiWeatherApplication.TOKEN}&lang=zh_CN")
    @GET("v2/place?token=${FijiWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}