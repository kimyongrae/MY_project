package kyr.company.kyr_place_visit_ver3.calendar

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import kyr.company.kyr_place_visit_ver3.R


class Write_TitleDialog (val context: Context) {

    private val dialog = Dialog(context) //부모 액티비티의 context가 들어감

    private lateinit var btn_ok: Button
    private lateinit var btn_cancle: Button
    private lateinit var title: TextView

    private var listener : OntitleListner? = null

    interface OntitleListner {
        fun sendDate(title:String)
    }

    fun setItemListner(listner: OntitleListner){
        this.listener = listner
    }

    fun startDialog(){

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        dialog.setContentView(R.layout.write_title_custom_dialog) //다이얼로그에 사용할 xml 파일을 불러옴
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

        btn_ok = dialog.findViewById(R.id.ok)
        btn_cancle=dialog.findViewById(R.id.cancle)
        title=dialog.findViewById(R.id.title)


        //확인
        btn_ok.setOnClickListener{
            listener!!.sendDate(title.text.toString())
            dialog.dismiss()
        }
        
        //취소
        btn_cancle.setOnClickListener{
            dialog.dismiss()
        }



        dialog.show()

    }


}