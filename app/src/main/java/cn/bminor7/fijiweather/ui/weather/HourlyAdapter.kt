import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import cn.bminor7.fijiweather.R
import cn.bminor7.fijiweather.logic.model.HourlyResponse
import cn.bminor7.fijiweather.logic.model.getSky

class HourlyAdapter (private val fragment : Fragment, private val hourlyList : HourlyResponse.Hourly) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val houlyTime : TextView = view.findViewById(R.id.hourlyTime)
        val hourlySkyon : ImageView = view.findViewById(R.id.hourlySkyon)
        val hourlyTemp : TextView = view.findViewById(R.id.hourlyTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.houly_item,parent,false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val temperature = hourlyList.temperature[position]
        val skyon = hourlyList.skycon[position]
        val sky = getSky(skyon.value)
        holder.houlyTime.text = temperature.datetime.hours.toString()+":00"
        holder.hourlyTemp.text = temperature.value.toString()+"℃"
        holder.hourlySkyon.setImageResource(sky.icon)
    }

    override fun getItemCount() = hourlyList.temperature.size

}

