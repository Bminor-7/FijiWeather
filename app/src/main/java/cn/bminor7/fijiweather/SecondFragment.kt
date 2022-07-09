package cn.bminor7.fijiweather

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bminor7.fijiweather.databinding.FragmentSecondBinding
import cn.bminor7.fijiweather.logic.Repository
import cn.bminor7.fijiweather.ui.place.PlaceAdapter
import cn.bminor7.fijiweather.ui.place.PlaceViewModel
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //map
    private lateinit var mMapView : MapView
    lateinit var mLocationClient : LocationClient
    val myListener = MyLocationListener()
    lateinit var option : LocationClientOption


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    val viewModel by lazy { ViewModelProvider(this)[PlaceViewModel::class.java]  }
    private lateinit var adapter : PlaceAdapter


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        mMapView = binding.mMapView
        mLocationClient = LocationClient(activity)
        option = LocationClientOption()
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setWifiCacheTimeOut(5*60*1000);
        option.setEnableSimulateGps(false);
        option.setIsNeedAddress(true);
        mLocationClient.locOption = option
        mLocationClient.registerLocationListener(myListener)
        mLocationClient.start()

        mMapView.visibility = View.VISIBLE

        val mapLongClick = BaiduMap.OnMapLongClickListener{
            val point = LatLng(it.latitude, it.longitude)
            //构建Marker图标
            val bitmap = BitmapDescriptorFactory
                .fromResource(resources.getIdentifier("ic_mark","drawable",
                    activity?.packageName ?: ""
                ))
            //构建MarkerOption，用于在地图上添加Marker
            val option: OverlayOptions = MarkerOptions()
                .position(point)
                .icon(bitmap)
            //在地图上添加Marker，并显示
            mMapView.map.addOverlay(option)

            binding.searchPlace.setOnClickListener {
                //Repository.refreshWeather(point.longitude.toString(),point.latitude.toString())
                val intent = Intent(context, TempDataActivity::class.java).apply {
                    putExtra("location_lng", point.longitude.toString())
                    putExtra("location_lat", point.latitude.toString())
                    putExtra("place_name", "标记地点")
                }
                startActivity(intent)
                activity?.finish()
            }
        }
        mMapView.map.setOnMapLongClickListener(mapLongClick)
        mMapView.map.setOnMapClickListener(MapOnClick())

        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this,viewModel.placeList)
        binding.recyclerView.adapter = adapter
        binding.searchPlaceEdit.addTextChangedListener{ editable ->
            val content = editable.toString()
            if(content.isEmpty()||content==""){
                mMapView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }else{
                viewModel.searchPlaces(content)
                mMapView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer{ result ->
            val places = result.getOrNull()
            Log.i("fijitest","places is $places")
            if(places !=null){
                binding.recyclerView.visibility=View.VISIBLE
                binding.bgImageView.visibility=View.GONE
                mMapView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })

    }

    override fun onResume() {
        mMapView.onResume()
        super.onResume()

    }

    override fun onPause() {
        mMapView.onPause()
        super.onPause()

    }

    override fun onDestroy() {
        mLocationClient.stop()
        mMapView.onDestroy()
        mMapView.map.setMyLocationEnabled(false)
        super.onDestroy()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            binding.bgImageView.visibility = View.GONE
            val locData = location?.let {
                MyLocationData.Builder()
                    .accuracy(it.radius)
                    .direction(it.direction)
                    .longitude(it.longitude)
                    .latitude( it.latitude)
                    .build()
            }
            mMapView.map.isMyLocationEnabled = true
            mMapView.map.setMyLocationData(locData)
        }
    }

    inner class MapOnClick : BaiduMap.OnMapClickListener{
        override fun onMapClick(p0: LatLng?) {
            mMapView.map.clear()
        }

        override fun onMapPoiClick(p0: MapPoi?) {
        }

    }
}
