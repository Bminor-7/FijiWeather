package cn.bminor7.fijiweather.logic.model

import java.util.*

data class HourlyResponse (val status : String, val result : Result) {
    data class Result(val hourly: Hourly)
    data class Hourly(val temperature: List<Temperature>, val skycon: List<Skycon>)
    data class Temperature(val datetime: Date, val value: Float)
    data class Skycon(val value: String)
}