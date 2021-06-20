package kyr.company.kyr_place_visit_ver3.map

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kyr.company.kyr_place_visit_ver3.R
import kyr.company.kyr_place_visit_ver3.common.AppContants
import kyr.company.kyr_place_visit_ver3.databinding.MapSearchDiaiogBinding


class Map_SearchDialog : DialogFragment(){

    private var listener: SearchListener? = null

    private lateinit var binding : MapSearchDiaiogBinding
    lateinit var imm: InputMethodManager

    companion object{

        fun newInstance(): Map_SearchDialog? {
            return Map_SearchDialog()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,
            R.style.FullscreenDialogTheme
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding= DataBindingUtil.inflate(inflater,
            R.layout.map_search_diaiog, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        imm=dialog!!.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        binding.SearchcardView.setBackgroundResource(R.drawable.search_view_border)

        init()
    }

    private fun init() {

        binding.searchBack.setOnClickListener {

            imm.hideSoftInputFromWindow(binding.search.windowToken,0)
            //뒤로가기
            dismiss()
        }

        //날짜 및 방문 예정 선택 상세화면
        binding.detailType.setOnClickListener{
            AppContants.println("detailtype 클릭됨")

            imm.hideSoftInputFromWindow(binding.search.windowToken,0)
            val dialog :Map_SearchDetailDialog = Map_SearchDetailDialog.newInstance()!!
            dialog.show(fragmentManager!!,"SearchDialog")

            dialog.setItemListner(object: Map_SearchDetailDialog.OnSearchDateSendListner{

                override fun sendDate(startdate: String, enddate: String, starttime: String, endtime: String) {

                    binding.startDate.text=startdate
                    binding.endDate.text=enddate
                    binding.startTime.text=starttime
                    binding.endTime.text=endtime
                    binding.wave.text="~"

                }
            })

        }

    }


    fun setCallback(listener: SearchListener?):Unit{
        this.listener = listener
    }


    public  interface SearchListener{
        fun onSend(search : String)
    }





}