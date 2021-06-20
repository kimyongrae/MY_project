package kyr.company.kyr_place_visit_ver2

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.DisplayMetrics
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kyr.company.kyr_place_visit_ver2.common.AppContants
import kyr.company.kyr_place_visit_ver2.databinding.Fragment1SearchDetailBinding
import java.util.*

class Fragment1SearchDetailDialog() : DialogFragment(),DatePickerFragment.OnInputDateSelected {

    private lateinit var binding: Fragment1SearchDetailBinding
    
    
    //데이터를 보내기 위해서 만듬
    interface OnSearchDateSendListner{
        fun sendDate(startdate :String,enddate :String,starttime :String,endtime :String)
    }

    fun setItemListner(listner: OnSearchDateSendListner){
        this.onSearchDateSendListner = listner
    }

    private var onSearchDateSendListner : OnSearchDateSendListner? = null

    
    //instace 호출을 통해서 dialogfragment  생성
    companion object {
        fun newInstance(): Fragment1SearchDetailDialog? {
            return Fragment1SearchDetailDialog()
        }
    }

    override fun onAttach(context: Context) {

        AppContants.println("onSearchDateSendListner 확인1......")

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
/*        //화면을 풀스크린으로
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme)*/
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        val params: WindowManager.LayoutParams = dialog!!.window!!.attributes

        val metrics: DisplayMetrics = context!!.resources.displayMetrics
        val displayPixelWidth = metrics.widthPixels
        val displayPixelHeight = metrics.heightPixels
        params.width = displayPixelWidth
        params.height = displayPixelHeight/10*7
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment1_search_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*        if(context is OnSearchDateSendListner){
            onSearchDateSendListner = context as OnSearchDateSendListner
            AppContants.println("onSearchDateSendListner 확인1234......")
        }*/

        init()
    }

    private fun init(): Unit {

        //뒤로가기 클릭시
        binding.back.setOnClickListener {
            //dialog fragment 닫기
            dismiss()
        }

        //시작 날짜
        binding.sDate.setOnClickListener {
            //스위치가 ON일때 달력을 띄움
            showDataPicker(AppContants.startDate)
        }

        //날짜 시간 초기화
        binding.sDateText.text = nowCalendar().toEditable()
        binding.eDateText.text = endCalendar().toEditable()
        binding.time.text="00:00~23:59".toEditable()

        //끝나는 날짜
        binding.eDate.setOnClickListener {
            showDataPicker(AppContants.endDate)
        }

        //시간을 클릭했을 경우 DialogOnclick  리스너로 부터 데이터를 가져옴
        binding.timeButton.setOnClickListener {
            val dialog: Fragment1CustomDialog = Fragment1CustomDialog(context!!)
            dialog.startDialog()
            dialog.DialogOkClickedListener { startTime, endTime ->

                val reg1 = Regex(".*[0-9].*")
                if (!(reg1.matches(startTime) && reg1.matches(endTime))) {
                    AppContants.toast("시간을 확인해주세요")
                    return@DialogOkClickedListener
                }
                binding.time.text = "${startTime}~${endTime}".toEditable()
            }
        }

/*        //전체를 클릭했을 경우
        binding.all.setOnClickListener {
            binding.calendarType.text="all"
            calndertype(binding.calendarType.text.toString())
        }
        
        //예정을 클릭했을 경우
       binding.expected.setOnClickListener {

           binding.calendarType.text="expected"
           calndertype(binding.calendarType.text.toString())
       }

        //방문을 클릭했을 경우
        binding.visited.setOnClickListener {
            binding.calendarType.text="visited"
            calndertype(binding.calendarType.text.toString())
        }*/

        //조회하기를 클릭한 경우
        binding.detailLookup.setOnClickListener {

            val time=binding.time.text.split("~")

            //조회하기 시작날짜,종료날짜,시작시간,종료시간,방문 타입,
            onSearchDateSendListner!!.sendDate(binding.sDateText.text.toString(),binding.eDateText.text.toString(),time[0],time[1])
            dismiss()
        }

    }

   /* private fun calndertype(data:String){

        when(data){

            "all"->{
                binding.all.setBackgroundResource(R.drawable.select_boarder)
                binding.expected.setBackgroundResource(R.drawable.none_select_boarder)
                binding.visited.setBackgroundResource(R.drawable.none_select_boarder)
            }

            "expected"->{
                binding.all.setBackgroundResource(R.drawable.none_select_boarder)
                binding.expected.setBackgroundResource(R.drawable.select_boarder)
                binding.visited.setBackgroundResource(R.drawable.none_select_boarder)
            }

            else->{
                binding.all.setBackgroundResource(R.drawable.none_select_boarder)
                binding.expected.setBackgroundResource(R.drawable.none_select_boarder)
                binding.visited.setBackgroundResource(R.drawable.select_boarder)
            }

        }

    }*/



    fun String.toEditable(): Editable {
        return Editable.Factory.getInstance().newEditable(this)
    }

    //날짜를 보여주는 다이얼로그 프래그 먼트 
    private fun showDataPicker(value: String) {
        val args = Bundle()
        args.putString("key", value)
        val newFragment: DialogFragment = DatePickerFragment()
        newFragment.arguments = args
        newFragment.setTargetFragment(this, 1)
        newFragment.show(fragmentManager!!, "datePicker")
    }
    
    //현재 날짜
    private fun nowCalendar() : String{
        val now : Long = System.currentTimeMillis()
        val mDate : Date = Date(now)
        val getTime: String =AppContants.dateFormat1.format(mDate)
        return  getTime
    }

    //해당월에 마지막일 가져오기
    private fun endCalendar() : String{
        val cal : Calendar = Calendar.getInstance() // 현재 시간을 받음

        val year : Int = cal.get(Calendar.YEAR)
        var month :Int = cal.get(Calendar.MONTH)+1
        val day : Int = cal.getActualMaximum(Calendar.DAY_OF_MONTH) //해당월의 마지막 날짜
        AppContants.println("해당년도:$year 해당월:$month 해당월의 마지막 날짜 $day")

        var monthText=""
        if(month<10){
            monthText = "0$month"
        }else{
            monthText="$month"
        }

        return "$year-$monthText-$day"
    }


    //다이얼로그 프래그먼트에서 전달받은 데이터
    override fun sendDate(date: String , dateType:String) {
        AppContants.println(date)

        when(dateType){
            AppContants.startDate -> {
                val compare:Int =date.compareTo(binding.eDateText.text.toString())
//                AppContants.println("어떤게 더크냐:$compare")
                if(compare>0){
                    AppContants.toast("시작일자를 다시 설정해주세요")
                    return
                }

                binding.sDateText.text=date.toEditable()
            }
            else->{

                val compare:Int =date.compareTo(binding.sDateText.text.toString())

                if(compare<0){
                    AppContants.toast("종료일자를 다시 설정해주세요")
                    return
                }

                binding.eDateText.text=date.toEditable()
            }
        }

    }


}