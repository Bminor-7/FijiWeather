package cn.bminor7.fijiweather.ui.place

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import cn.bminor7.fijiweather.FirstFragment
import cn.bminor7.fijiweather.MainActivity
import cn.bminor7.fijiweather.R
import cn.bminor7.fijiweather.TempDataActivity
import cn.bminor7.fijiweather.logic.model.Place

class PlaceAdapter(private val fragment : Fragment, private val placeList : List<Place>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
        val placeButton : ImageButton = view.findViewById(R.id.placeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)

        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val place = placeList[position]
            val intent = Intent(parent.context, TempDataActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
                putExtra("flag","1")
            }
            fragment.startActivity(intent)
        }

        //添加收藏地址
        holder.placeButton.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val sp = parent.context.getSharedPreferences("data",Context.MODE_PRIVATE)
            val temp = sp.getString("starLocation","")
            var flag = true
            val str = temp?.split("#")
            if (str != null) {
                for (i in 0..str.size-1){
                    if (placeList[position].name == str[i]){
                        flag = false
                    }
                }
            }

            if (flag){
                val editor = parent.context.getSharedPreferences("data",Context.MODE_PRIVATE).edit()
                editor.putString("starLocation",temp+"#"+placeList[holder.bindingAdapterPosition].name+"#"+placeList[holder.
                bindingAdapterPosition].location.lng+"#"+placeList[holder.bindingAdapterPosition].location.lat)
                editor.apply()
                Toast.makeText(parent.context,"添加成功",Toast.LENGTH_SHORT).show()
                Log.i("starLocation","temp is "+temp)
            }
            else Toast.makeText(parent.context,"添加失败 已收藏。。",Toast.LENGTH_SHORT).show()

        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount() = placeList.size
}
