package cn.bminor7.fijiweather.ui.weather


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import cn.bminor7.fijiweather.R

class PrecipitationView(context: Context,attrs : AttributeSet) :View(context,attrs) {


    val listY = mutableListOf(0F)
    val listX = mutableListOf(0F)
    var height : Int? = null

    @RequiresApi(Build.VERSION_CODES.R)
    fun setData(list : List<Float>, height: Int){
        this.height =height
        Log.i("PrecipitationViewTest","height"+height)
        for (i in 0..list.size-1){
            this.listY.add((height-(list[i]*height)))
        }
        Log.i("PrecipitationViewTest","listY ="+listY)
        val widthPer= (getScreenWidth()-160f)/120
        for (i in 1..120 ){
            listX.add ((80+widthPer*i))
        }
        Log.i("PrecipitationViewTest","listX ="+listX)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun getScreenWidth() : Int{
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE)as WindowManager
        val width = windowManager.currentWindowMetrics.bounds.width()

        return width
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.setColor(Color.GRAY)
        paint.style=Paint.Style.STROKE

        if(listY.size>10&&height!=null){
            var path = Path()
            for(i in 1..118 step 2 ){
                if (canvas != null) {
                    path.moveTo(listX[i],listY[i])
                    path.quadTo(listX[i+1],listY[i+1],listX[i+2],listY[i+2])
                    //canvas.drawLine(listX[i], listY[i],listY[i+1],listY[i+1],paint)
                    //canvas.drawRect(listX[i], listY[i],listY[i+1], (height!!.toFloat()-1),paint)
                    canvas.drawPath(path,paint)
                    Log.i("PrecipitationViewTest","size"+listX.size)
//                    Log.i("PrecipitationViewTest","listX[i] ="+listX[i]+"listY[i] ="+listY[i])
//                    Log.i("PrecipitationViewTest","listX[i+1] ="+listX[i+1]+"listY[i+1] ="+listY[i+1])
//                    Log.i("PrecipitationViewTest","listX[i+2] ="+listX[i+2]+"listY[i+2] ="+listY[i+2])
                }
            }
        }

    }

    override fun invalidate() {
        super.invalidate()
    }
}



