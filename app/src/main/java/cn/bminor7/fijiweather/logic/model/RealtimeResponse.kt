package cn.bminor7.fijiweather.logic.model

import com.google.gson.annotations.SerializedName

data class RealtimeResponse(val status : String,val result : Result){
    data class Result(val realtime : Realtime)
    data class Realtime(val skycon : String,val temperature : Float, @SerializedName("air_quality")val airQuality : AirQuality,@SerializedName("life_index")val lifeIndex : LifeIndex)
    data class AirQuality(val aqi : Aqi)
    data class Aqi(val chn : Float)
    data class LifeIndex(val ultraviolet : Ultraviolet)
    data class Ultraviolet(val index: Int,val desc : String)

}
