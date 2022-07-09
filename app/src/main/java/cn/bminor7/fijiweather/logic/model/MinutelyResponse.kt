package cn.bminor7.fijiweather.logic.model

import com.google.gson.annotations.SerializedName

data class MinutelyResponse(val status : String,val result : Result){
    data class Result(val minutely : Minutely)
    data class Minutely(@SerializedName("precipitation_2h")val precipitation2h : List<Float>, val description :String)
}