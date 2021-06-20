package kyr.company.kyr_place_visit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kyr.company.kyr_place_visit.adapter.PlaceAdapter
import kyr.company.kyr_place_visit.databinding.Fragment2Binding
import kyr.company.kyr_place_visit.databinding.Fragment3Binding
import kyr.company.kyr_place_visit.dto.PlaceVo
import kyr.company.r9_rev_b.common.AppContants
import kyr.company.r9_rev_b.common.MyApplication


class Fragment3 : Fragment() {

    private lateinit var binding : Fragment3Binding

    var placeAdapter : PlaceAdapter = PlaceAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment3, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {

        binding.noteRecyclerView.layoutManager= LinearLayoutManager(MyApplication.applicationContext())
        binding.noteRecyclerView.adapter=placeAdapter

        placeAdapter.addItem(PlaceVo("테스트1","안녕하세요","경기도 안산시 단원구1"))
        placeAdapter.addItem(PlaceVo("테스트2","안녕하세요","경기도 안산시 단원구2"))
        placeAdapter.addItem(PlaceVo("테스트3","안녕하세요","경기도 안산시 단원구3"))
        placeAdapter.addItem(PlaceVo("테스트4","안녕하세요","경기도 안산시 단원구4"))
        placeAdapter.addItem(PlaceVo("테스트5","안녕하세요","경기도 안산시 단원구5"))
        placeAdapter.addItem(PlaceVo("테스트6","안녕하세요","경기도 안산시 단원구6"))
        placeAdapter.addItem(PlaceVo("테스트7","안녕하세요","경기도 안산시 단원구7"))
        placeAdapter.addItem(PlaceVo("테스트8","안녕하세요","경기도 안산시 단원구8"))
        placeAdapter.addItem(PlaceVo("테스트9","안녕하세요","경기도 안산시 단원구9"))

        placeAdapter.notifyDataSetChanged()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppContants.println("fragment2 onAttach")
    }

}