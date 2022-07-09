package cn.bminor7.fijiweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object FijiWeatherNetwork {
    private val placeService = ServiceCreator.create(PlaceService::class.java)
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()


    private val weatherService = ServiceCreator.create(WeatherService::class.java)
    suspend fun getRealtimeWeather(lng: String,lat : String) = weatherService.getRealtimeWeather(lng,lat).await()
    suspend fun getDailyWeather(lng: String,lat : String) = weatherService.getDailyWeather(lng,lat).await()
    suspend fun getMinutelyWeather(lng: String,lat : String) = weatherService.getMinutelyWeather(lng,lat).await()
    suspend fun getHourlyWeather(lng: String,lat : String) = weatherService.getHourlyWeather(lng,lat).await()

    private suspend fun <T> Call<T>.await() : T {
        return suspendCoroutine {  Continuation ->
            enqueue(object : Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body != null) Continuation.resume(body)
                    else Continuation.resumeWithException(RuntimeException("body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    Continuation.resumeWithException(t)
                }

            })

        }
    }

}