package kyr.company.kyr_place_visit_ver3.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kyr.company.kyr_place_visit_ver3.R
import kyr.company.kyr_place_visit_ver3.adapter.CalenderAdapter
import kyr.company.kyr_place_visit_ver3.adapter.ListAdapter
import kyr.company.kyr_place_visit_ver3.common.AppContants
import kyr.company.kyr_place_visit_ver3.common.MyApplication
import kyr.company.kyr_place_visit_ver3.databinding.ListFragmentBinding
import kyr.company.kyr_place_visit_ver3.viewmodel.ListViewModel

/**
 *4번째 데이터 출력 리스트 화면
 */
class List_Fragment : Fragment() {

    private lateinit var binding : ListFragmentBinding

    //리스트 뷰 모델
    private lateinit var listViewModel: ListViewModel

    //리스트 어답터
    private lateinit var listAdapter : ListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel = ViewModelProvider(requireActivity()).get(ListViewModel::class.java)
        //uploadViewModel = ViewModelProvider(requireActivity()).get(UploadViewModel::class.java)
        initUI()
    }

    private fun initUI() {

        listAdapter = ListAdapter()
        binding.placeRecycler.layoutManager= GridLayoutManager(MyApplication.applicationContext(), 2)
        binding.placeRecycler.adapter=listAdapter

        //리스트 라이브 데이터 조회
        listViewModel.placeListLiveData.observe(requireActivity(), Observer {
            if(it==null){
                return@Observer
            }
            listAdapter.setItems(it!!)
            listAdapter.notifyDataSetChanged()
        })


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppContants.println("fragment2 onAttach")
    }

}