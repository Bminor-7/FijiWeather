package cn.bminor7.fijiweather

import android.Manifest
import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import cn.bminor7.fijiweather.FijiWeatherApplication.Companion.context
import cn.bminor7.fijiweather.databinding.ActivityMainBinding
import cn.bminor7.fijiweather.logic.Repository
import com.baidu.location.*
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.map.MyLocationData
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var mLocationClient : LocationClient
    val myListener = MyLocationListener()
    lateinit var option : LocationClientOption

    lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navView: BottomNavigationView = binding.navView
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_weather, R.id.navigation_search, R.id.navigation_mine
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        //百度地图API使用
        LocationClient.setAgreePrivacy(true)
        mLocationClient = LocationClient(this)
        mLocationClient.registerLocationListener(myListener)

        //refesh

//        binding.refreshFra.setOnRefreshListener {
//            binding.refreshFra.isRefreshing = true
//            intent.getStringExtra("location_longitude")
//                ?.let { Repository.refreshWeather(it, intent.getStringExtra("location_latitude")!!)
//                    Log.i("refresh","刷新成功")}
//            binding.refreshFra.isRefreshing = false
//        }


        Log.i("locationTest","mLocationClient is :"+mLocationClient)
        initLocation()
        //隐藏状态栏
        val controller = ViewCompat.getWindowInsetsController(binding.root)
        controller?.hide(WindowInsetsCompat.Type.statusBars())

        //获取权限
        var permissionList : MutableList<String> = ArrayList()
        if(ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(permission.ACCESS_FINE_LOCATION)
        }

        if(ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(permission.ACCESS_COARSE_LOCATION)
        }

        if(ContextCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(permission.WRITE_EXTERNAL_STORAGE)
        }


        if (!permissionList.isEmpty()){
            val permissions = permissionList
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 2)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun initLocation(){
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

        Log.i("lbsdemotest","mLocationClient is "+mLocationClient);
        Log.i("lbsdemotest",""+option);
            Log.i("lbsdemotest","000");
            mLocationClient.setLocOption(option);
            mLocationClient.start();
    }


   inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {


            val locationText = StringBuilder()
            if (location != null) {
                locationText.append("维度：").append(location.latitude).append("\n")
                locationText.append("经度：").append(location.longitude).append("\n");
                locationText.append("定位精度：").append(location.radius).append("\n");
                locationText.append("及维度坐标类型：").append(location.coorType).append("\n");
                locationText.append("国家：").append(location.getCountry()).append("\n");
                locationText.append("省：").append(location.getProvince()).append("\n");
                locationText.append("市：").append(location.getCity()).append("\n");
                locationText.append("县：").append(location.getDistrict()).append("\n");
                locationText.append("镇：").append(location.getTown()).append("\n");
                locationText.append("街道：").append(location.getStreet()).append("\n");
                Log.i("locationTest","locationText is :"+locationText)


                //从SharePreferences中获取地理位置
                val prefs = getSharedPreferences("data", MODE_PRIVATE)
                val placeName = prefs.getString("name","")
                val location_longitude = prefs.getString("location_longitude","")
                val location_latitude = prefs.getString("location_latitude","")
                val flag = prefs.getString("flag","")
                if((placeName!=location.town+location.street)&&(location_longitude!=location.longitude.toString())){
                    val editor = getSharedPreferences("data", MODE_PRIVATE).edit()
                    editor.putString("name",location.town+location.street)
                    editor.putString("location_longitude",location.longitude.toString())
                    editor.putString("location_latitude",location.latitude.toString())
                    editor.putString("flag","0")
                    editor.apply()
                }

                Log.i("prefs is","flag1 is ="+flag)
                Log.i("prefs is","location_longitude is ="+location_longitude)
                Log.i("prefs is","location_latitude is ="+location_latitude)


                if(flag!="1"){
                val intent = Intent(context, MainActivity::class.java).apply {
                        putExtra("location_longitude", location.longitude.toString())
                        putExtra("location_latitude", location.latitude.toString())
                        putExtra("location_name", location.town+location.street)
                    }
                    Log.i("flag is","flag2 is ="+flag)

                    if(flag!="1"){
                        val editor = getSharedPreferences("data", MODE_PRIVATE).edit()
                        editor.putString("flag","1")
                        editor.apply()
                        startActivity(intent)
                        finish()
                        Log.i("flag is","flag3 is ="+flag)
                    }
                    Log.i("flag is","flag4 is ="+flag)
                }
            }
        }

    }
}

