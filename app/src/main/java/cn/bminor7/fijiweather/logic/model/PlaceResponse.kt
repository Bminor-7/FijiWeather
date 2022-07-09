package cn.bminor7.fijiweather.logic.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status : String, val places : List<Place>)

data class Place(var name : String, val location : cn.bminor7.fijiweather.logic.model.Location, @SerializedName("formatted_address") val address:  String)

data class  Location(val lng : String, val lat : String)