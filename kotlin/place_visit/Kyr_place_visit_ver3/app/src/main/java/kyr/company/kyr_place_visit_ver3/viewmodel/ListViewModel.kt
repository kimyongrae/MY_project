package kyr.company.kyr_place_visit_ver3.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kyr.company.kyr_place_visit_ver3.common.AppContants
import kyr.company.kyr_place_visit_ver3.common.MyApplication
import kyr.company.kyr_place_visit_ver3.dao.BoardDao
import kyr.company.kyr_place_visit_ver3.db.AppDatabase
import kyr.company.kyr_place_visit_ver3.model.FileVo
import kyr.company.kyr_place_visit_ver3.model.PlaceVo
import kyr.company.kyr_place_visit_ver3.service.BoardRepository

class ListViewModel : ViewModel() {

    //방문장소 리스트 데이터
    val placeListLiveData: MutableLiveData<MutableList<PlaceVo>> = MutableLiveData()
    //서비스단
    private val boardRepository : BoardRepository

    init {
        val boardDao : BoardDao =  AppDatabase.getDatabase(MyApplication.applicationContext()).boardDao()
        boardRepository  = BoardRepository(boardDao)
        placeList()
    }

    fun placeList() {
        CoroutineScope(Dispatchers.Default).launch {
            val result : MutableList<PlaceVo> = boardRepository.placelist()
            AppContants.println("방문 리스트 확인$result")
            placeListLiveData.postValue(result)
        }
    }


}