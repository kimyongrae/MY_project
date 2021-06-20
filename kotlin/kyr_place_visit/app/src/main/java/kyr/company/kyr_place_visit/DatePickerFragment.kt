package kyr.company.kyr_place_visit

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import kyr.company.r9_rev_b.common.AppContants
import java.util.*

class DatePickerFragment : DialogFragment(),DatePickerDialog.OnDateSetListener {

    interface OnInputDateSelected{
        fun sendDate(date : String , dateType : String)
    }

    public var mOnInputDateSelected : OnInputDateSelected? = null
    var dateType : String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        dateType=arguments?.getString("key")

        if(targetFragment is OnInputDateSelected){
            mOnInputDateSelected = targetFragment as OnInputDateSelected
           AppContants.println("확인......")
        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c : Calendar = Calendar.getInstance()
        var year : Int = c.get(Calendar.YEAR)
        var month : Int = c.get(Calendar.MONTH)
        var day : Int = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity!!,this,year,month,day)

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
/*        var fragment1 : Fragment1 = Fragment1()
        fragment1.processDatePickerResult(year,month,dayOfMonth)*/

        var NowMonth : Int =month+1
        var monthText : String ="";
        if(NowMonth<10){
            monthText = "0$NowMonth"
        }else{
            monthText="$NowMonth"
        }

        var day : String
        if(dayOfMonth<10){
            day = "0$dayOfMonth"
        }else{
            day="$dayOfMonth"
        }

        mOnInputDateSelected?.sendDate("$year-${monthText}-$day",dateType!!)

    }



}