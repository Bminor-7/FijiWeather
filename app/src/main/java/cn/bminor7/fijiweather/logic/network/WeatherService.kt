package cn.bminor7.fijiweather.logic.network

import cn.bminor7.fijiweather.FijiWeatherApplication
import cn.bminor7.fijiweather.logic.model.DailyResponse
import cn.bminor7.fijiweather.logic.model.HourlyResponse
import cn.bminor7.fijiweather.logic.model.MinutelyResponse
import cn.bminor7.fijiweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("v2.6/${FijiWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<RealtimeResponse>

    @GET("v2.6/${FijiWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>

    @GET("v2.6/${FijiWeatherApplication.TOKEN}/{lng},{lat}/minutely.json")
    fun getMinutelyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<MinutelyResponse>

    @GET("v2.6/${FijiWeatherApplication.TOKEN}/{lng},{lat}/hourly.json")
    fun getHourlyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<HourlyResponse>

}