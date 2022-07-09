package cn.bminor7.fijiweather

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bminor7.fijiweather.databinding.FragmentThirdBinding
import cn.bminor7.fijiweather.ui.place.PlaceAdapter

class ThirdFragment : Fragment() {

    private var _binding : FragmentThirdBinding? =null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    lateinit var adapter : StarLocationAdapter

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        //
        val sp = activity?.getSharedPreferences("data",Context.MODE_PRIVATE)
        val str = sp?.getString("starLocation","")
        val strs = str?.split("#")
        val placeName = mutableListOf<String>()
        val placeLng = mutableListOf<String>()
        val placeLat = mutableListOf<String>()
        if (strs != null) {
            for (i in 1..strs.size-1 step 3){
                placeName.add(strs[i])
                placeLng.add(strs[i+1])
                placeLat.add(strs[i+2])
            }
            Log.i("starLocation","placeName"+placeName)
            Log.i("starLocation","placeLng"+placeLng)
            Log.i("starLocation","placeLat"+placeLat)

            val navController = activity?.findNavController(R.id.nav_host_fragment_activity_main)
            val layoutManager = LinearLayoutManager(activity)
            binding.starLocation.layoutManager = layoutManager
            adapter = StarLocationAdapter(this,placeName,placeLng,placeLat,navController)
            binding.starLocation.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null;
    }


}