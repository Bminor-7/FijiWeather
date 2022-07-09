package cn.bminor7.fijiweather.logic.model

data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily,
                   val minutely: MinutelyResponse.Minutely,val hourly: HourlyResponse.Hourly)