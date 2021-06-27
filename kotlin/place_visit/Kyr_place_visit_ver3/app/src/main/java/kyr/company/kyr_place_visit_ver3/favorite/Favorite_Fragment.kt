package kyr.company.kyr_place_visit_ver3.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kyr.company.kyr_place_visit_ver3.R
import kyr.company.kyr_place_visit_ver3.adapter.PlaceAdapter
import kyr.company.kyr_place_visit_ver3.common.AppContants
import kyr.company.kyr_place_visit_ver3.common.MyApplication
import kyr.company.kyr_place_visit_ver3.databinding.CalendarFragmentBinding
import kyr.company.kyr_place_visit_ver3.databinding.FavoriteFragmentBinding
import kyr.company.kyr_place_visit_ver3.model.PlaceVo

/**
 * 3번째 화면 좋아요를 클릭한 화면
 */
class Favorite_Fragment : Fragment() {

    private lateinit var binding : FavoriteFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.favorite_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

}