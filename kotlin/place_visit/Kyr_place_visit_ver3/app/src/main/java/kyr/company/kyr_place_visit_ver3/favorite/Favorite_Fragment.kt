package kyr.company.kyr_place_visit_ver3.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kyr.company.kyr_place_visit_ver3.R
import kyr.company.kyr_place_visit_ver3.databinding.FavoriteFragmentBinding

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