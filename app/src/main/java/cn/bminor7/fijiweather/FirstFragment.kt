package cn.bminor7.fijiweather

import HourlyAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bminor7.fijiweather.databinding.FragmentFirstBinding
import cn.bminor7.fijiweather.logic.model.Weather
import cn.bminor7.fijiweather.logic.model.getSky
import cn.bminor7.fijiweather.ui.place.PlaceAdapter
import cn.bminor7.fijiweather.ui.place.PlaceViewModel
import cn.bminor7.fijiweather.ui.weather.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val viewModel by lazy { ViewModelProvider(this)[WeatherViewModel::class.java]  }

    private lateinit var adapter : HourlyAdapter

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(activity?.intent?.getStringExtra("location_lng")==null){

            val pref = activity?.getSharedPreferences("data",Context.MODE_PRIVATE)
            val placeName = pref?.getString("name","")
            val location_longitude = pref?.getString("location_longitude","")
            val location_latitude = pref?.getString("location_latitude","")
            if (location_longitude != null) {
                viewModel.locationLng = location_longitude
            }
            if (location_latitude != null) {
                viewModel.locationLat = location_latitude
            }
            if (placeName != null) {
                viewModel.placeName = placeName
            }

        }else{
            if (viewModel.locationLng.isEmpty()) {
                viewModel.locationLng = activity?.intent?.getStringExtra("location_lng") ?: ""
            }
            if (viewModel.locationLat.isEmpty()) {
                viewModel.locationLat = activity?.intent?.getStringExtra("location_lat") ?: ""
            }
            if (viewModel.placeName.isEmpty()) {
                viewModel.placeName = activity?.intent?.getStringExtra("place_name") ?: ""
            }
        }


        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            Log.i("locationTest","$weather")
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(activity, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
           binding.refresh.isRefreshing = false
        })
        refreshWeather()
    }

    fun refreshWeather(){
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showWeatherInfo(weather: Weather){

        binding.temperatureLayout.visibility = View.VISIBLE
        binding.placeName.text=viewModel.placeName
        binding.currentTemp.text="${weather.realtime.temperature.toInt()} ℃"
        binding.currentSky.text= getSky(weather.realtime.skycon).info
        binding.currentAQI.text="空气指数："+weather.realtime.airQuality.aqi.chn.toString()+
                " 紫外线等级："+weather.realtime.lifeIndex.ultraviolet.index

        binding.dailyLayout.visibility=View.VISIBLE
        binding.forecastLayout.removeAllViews()
        val days = weather.daily.skycon.size
        for (i in 0 until days) {
            val skycon = weather.daily.skycon[i]
            val temperature = weather.daily.temperature[i]
            val view = LayoutInflater.from(activity).inflate(R.layout.forecast_item, binding.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            binding.forecastLayout.addView(view)
        }


        binding.precipitationViewLayout.visibility = View.VISIBLE
        binding.description.text=weather.minutely.description
        binding.precipitationView.setData(weather.minutely.precipitation2h,binding.precipitationView.measuredHeight)
        binding.precipitationView.invalidate()

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.hourlyRecycler.layoutManager = layoutManager
        viewModel.hourly = weather.hourly
        adapter = HourlyAdapter(this,viewModel.hourly)
        binding.hourlyRecycler.adapter = adapter


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }



    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = true
            activity?.findNavController(R.id.nav_host_fragment_activity_main)?.navigate(R.id.navigation_weather)
            refreshWeather()
            Toast.makeText(context,"刷新成功！",Toast.LENGTH_SHORT).show()
            binding.refresh.isRefreshing = false
        }
    }

}