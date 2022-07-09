package cn.bminor7.fijiweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.baidu.location.LocationClient
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer

class FijiWeatherApplication : Application() {
    companion object{
        const val TOKEN = "4cN1Ojqjl8Lc4Xcm"
        @SuppressLint("StaticFieldLeak")
        lateinit var context : Context
    }

    override fun onCreate() {
        super.onCreate()
        SDKInitializer.setAgreePrivacy(getApplicationContext(),true)
        SDKInitializer.initialize(getApplicationContext())
        SDKInitializer.setCoordType(CoordType.BD09LL)
        LocationClient.setAgreePrivacy(true)
        context = applicationContext
    }
}