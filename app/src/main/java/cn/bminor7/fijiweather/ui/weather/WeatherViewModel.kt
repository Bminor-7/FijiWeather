package cn.bminor7.fijiweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cn.bminor7.fijiweather.logic.Repository
import cn.bminor7.fijiweather.logic.model.HourlyResponse
import cn.bminor7.fijiweather.logic.model.Location

class WeatherViewModel : ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = ""
    var locationLat = ""
    var placeName   = ""

     lateinit var hourly : HourlyResponse.Hourly

    val weatherLiveData = Transformations.switchMap(locationLiveData){location ->
        Repository.refreshWeather(location.lng,location.lat)
    }

    fun refreshWeather(lng : String, lat : String){
        locationLiveData.value=Location(lng,lat);
    }
}