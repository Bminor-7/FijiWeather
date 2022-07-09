package cn.bminor7.fijiweather.logic

import android.util.Log
import androidx.lifecycle.liveData
import cn.bminor7.fijiweather.logic.model.Place
import cn.bminor7.fijiweather.logic.model.Weather
import cn.bminor7.fijiweather.logic.network.FijiWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException

object Repository {
    fun searchPlaces(query : String) = liveData(Dispatchers.IO){
        val result = try{
            val placeResponse = FijiWeatherNetwork.searchPlaces(query)
            if(placeResponse.status == "ok"){
                val places = placeResponse.places
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response is $(placeResponse.status)"))
            }

        }catch (e : Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }

    fun refreshWeather(lng : String,lat : String) = liveData(Dispatchers.IO) {
        val result = try {
            coroutineScope {
                val deferredRealtime = async {
                    FijiWeatherNetwork.getRealtimeWeather(lng,lat)
                }
                val deferredDaily = async {
                    FijiWeatherNetwork.getDailyWeather(lng,lat)
                }
                val deferredMinutely= async {
                    FijiWeatherNetwork.getMinutelyWeather(lng,lat)
                }
                val deferredgetHourly = async {
                    FijiWeatherNetwork.getHourlyWeather(lng,lat)
                }

                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                val minutelyResponse = deferredMinutely.await()
                val hourlyResponse = deferredgetHourly.await()
                if(realtimeResponse.status=="ok"&&dailyResponse.status=="ok"
                    &&minutelyResponse.status=="ok"&&hourlyResponse.status=="ok"){

                    val weather = Weather(realtimeResponse.result.realtime,
                        dailyResponse.result.daily,minutelyResponse.result.minutely,
                        hourlyResponse.result.hourly)
                    Result.success(weather)
                }
                else{
                    Result.failure(RuntimeException("realtimeResponse is $(realtimeResponse.status)" +
                            "dailyResponse is $(dailyResponse.status)"+"minutelyResponse is $(minutelyResponse.status)"+
                            "hourlyResponse is $(hourlyResponse.status)"))
                }
            }
        }catch (e : Exception){
            Result.failure<Weather>(e)
        }
        emit(result)
    }
}