package kyr.company.r9_rev_b.common

import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat

class AppContants {

    companion object
    {
        val TAG : String ="LOG"

        val dateFormat1 = SimpleDateFormat("YYYY-MM-dd")
        val dateFormat2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


        val startDate:String ="Start"
        val endDate:String = "End"

        fun println(data : String){
            Log.d(TAG,data)
        }

        fun toast(data: String) {
            Toast.makeText(MyApplication.applicationContext(),data,Toast.LENGTH_SHORT).show()
        }

    }

}