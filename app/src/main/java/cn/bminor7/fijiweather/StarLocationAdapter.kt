package cn.bminor7.fijiweather

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


class StarLocationAdapter(private val fragment: Fragment, private val placeName: List<String>,
                          private val placeLng: List<String>,
                          private val placeLat: List<String>,
                          private val navController: NavController?
) : RecyclerView.Adapter<StarLocationAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.starPlaceName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.star_location_item, parent, false)

        val holder = ViewHolder(view)
        //点击跳转
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val name = placeName[position]
            val lng  = placeLng[position]
            val lat  = placeLat[position]
            val intent = Intent(parent.context, TempDataActivity::class.java).apply {
                putExtra("location_lng", lng)
                putExtra("location_lat", lat)
                putExtra("place_name", name)
            }
            fragment.startActivity(intent)
            Log.i("starLocation","location is "+name+lng+lat)
        }

        //长按删除
        holder.itemView.setOnLongClickListener{
            Toast.makeText(parent.context,"删除成功",Toast.LENGTH_SHORT).show()
            val position = holder.bindingAdapterPosition
            var temp = ""
            for (i in 0 .. placeName.size-1 ){
                if(i != position) {
                    temp += "#" + placeName[i] +"#" + placeLng[i] + "#" +placeLat[i]
                }
            }
            val editor = parent.context.getSharedPreferences("data",Context.MODE_PRIVATE).edit()
            editor.putString("starLocation",temp)
            editor.apply()
            navController?.navigate(R.id.navigation_mine)
            return@setOnLongClickListener true
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeName[position]
        holder.placeName.text = place
    }

    override fun getItemCount() = placeName.size

}
