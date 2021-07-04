package kyr.company.kyr_place_visit_ver3.common

import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat

class AppContants {

    companion object
    {
        val TAG : String ="LOG"

        val dateFormat1 = SimpleDateFormat("YYYY-MM-dd")
        val dateFormat2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val PICTURE_REQUEST_CODE = 99;


        val metrics: DisplayMetrics = MyApplication.applicationContext().resources.displayMetrics
        val displayPixelWidth = metrics.widthPixels
        val displayPixelHeight = metrics.heightPixels

        val startDate:String ="Start"
        val endDate:String = "End"

        val Database_name="PlaceDB1"

        fun println(data : String){
            Log.d(TAG,data)
        }

        fun toast(data: String) {
            Toast.makeText(MyApplication.applicationContext(),data,Toast.LENGTH_SHORT).show()
        }



    }

}