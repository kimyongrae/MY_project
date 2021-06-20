package kyr.company.kyr_place_visit_ver2

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import kyr.company.kyr_place_visit_ver2.common.AppContants
import java.util.*


class Fragment1CustomDialog (val context: Context) {

    private val dialog = Dialog(context) //부모 액티비티의 context가 들어감

    private  lateinit var startTimeLayout : CardView
    private  lateinit var endTimeLayout : CardView

    private lateinit var start_Time: TextView
    private lateinit var end_Time : TextView

    private lateinit var btn_ok : CardView
    private lateinit var btn_cancle : CardView

    private lateinit var start_drop : ImageView
    private lateinit var end_drop : ImageView

    private lateinit var listener : MyDialogOKClickedListener

    fun startDialog(){

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        dialog.setContentView(R.layout.fragment1_custom_dialog) //다이얼로그에 사용할 xml 파일을 불러옴
//        dialog.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dialog.window?.setGravity(Gravity.CENTER);

        val metrics: DisplayMetrics = context.resources.displayMetrics
        val displayPixelWidth = metrics.widthPixels
        val displayPixelHeight = metrics.heightPixels

        var yh = displayPixelHeight/100*60
//        val params: WindowManager.LayoutParams = dialog.window!!.attributes
        val params: WindowManager.LayoutParams = WindowManager.LayoutParams()
        params.copyFrom(dialog.window?.attributes)
        params.width = displayPixelWidth/100*70
        params.height = displayPixelHeight/100*30
//        params.y=yh
        dialog.window!!.attributes = params

        startTimeLayout = dialog.findViewById(R.id.start_time)
        endTimeLayout = dialog.findViewById(R.id.end_time)

        startTimeLayout.setBackgroundResource(R.drawable.card_view_border)
        endTimeLayout.setBackgroundResource(R.drawable.card_view_border)

        //시작 시간
        start_Time=dialog.findViewById(R.id.start_text)
        //종료 시간
        end_Time=dialog.findViewById(R.id.end_text)

        btn_ok = dialog.findViewById(R.id.btnOk)
        btn_cancle=dialog.findViewById(R.id.btnCanCle)

        start_drop= dialog.findViewById(R.id.start_drop)
        end_drop= dialog.findViewById(R.id.end_drop)

        //확인
        btn_ok.setOnClickListener{

            listener.onOKClicked(start_Time.text.toString(),end_Time.text.toString())

            dialog.dismiss()
        }
        
        //취소
        btn_cancle.setOnClickListener{
            dialog.dismiss()
        }

        //시작시간 설정
        startTimeLayout.setOnClickListener{
            getTime(context,1)
        }
        
        //끝나는 시간 설정
        endTimeLayout.setOnClickListener{
            getTime(context,2)
        }


        dialog.show()

    }

    fun getTime(context: Context, flag : Int){

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            var nowHour : String = ""
            var nowMinute : String =""

            nowHour = if(hour<10){
                "0$hour"
            }else{
                "$hour"
            }

            nowMinute = if(minute<10){
                "0$minute"
            }else{
                "$minute"
            }

            when(flag){

                1->{
                    val time:String = "$nowHour:$nowMinute"
                    val reg1 = Regex(".*[0-9].*")

                    val compare = time.compareTo(end_Time.text.toString())

                    if(compare>0){
                        AppContants.toast("시작 시간을 확인 해주세요")
                        return@OnTimeSetListener
                    }

                    start_Time.text = time
                    start_Time.textSize = 15F
                    start_drop.visibility= View.GONE
                }
                else->{
                    val reg1 = Regex(".*[0-9].*")
                    val time:String = "$nowHour:$nowMinute"

                    if(!reg1.matches(start_Time.text.toString())){
                        AppContants.toast("시작시간을 먼저 설정해주세요")
                        return@OnTimeSetListener
                    }

                    val compare = time.compareTo(start_Time.text.toString())
                    if(compare<0){
                        AppContants.toast("종료 시간을 확인해주세요")
                        return@OnTimeSetListener
                    }
                    
                    end_Time.text=time
                    end_Time.textSize = 15F
                    end_drop.visibility= View.GONE
                }
            }


        }

//        val dialog: TimePickerDialog =TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
//        dialog.show()

        val timePickerDialog : TimePickerDialog = TimePickerDialog(context,timeSetListener,cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),false)

        timePickerDialog.show()
    }

    fun DialogOkClickedListener(listener : (String,String) -> Unit){
        this.listener = object : MyDialogOKClickedListener {
            override fun onOKClicked(StartTime: String, EndTime: String) {
                listener(StartTime,EndTime)
            }
        }
    }


    interface MyDialogOKClickedListener {
        fun onOKClicked(StartTime : String , EndTime : String )
    }


}