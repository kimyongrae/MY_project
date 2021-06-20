package kyr.company.kyr_place_visit_ver2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kyr.company.kyr_place_visit_ver2.common.AppContants
import kyr.company.kyr_place_visit_ver2.common.MyApplication
import kyr.company.kyr_place_visit_ver2.dao.BoardDao
import kyr.company.kyr_place_visit_ver2.db.AppDatabase
import kyr.company.kyr_place_visit_ver2.model.FileVo
import kyr.company.kyr_place_visit_ver2.model.PlaceVo
import kyr.company.kyr_place_visit_ver2.service.BoardRepository

class UploadViewModel : ViewModel() {

    //업로드 파일 리스트
    val UploadFileLiveData : MutableLiveData<MutableList<FileVo>> = MutableLiveData()
    //업로드 지도 위도 경도
    val uploadlatLng : MutableLiveData<LatLng> = MutableLiveData()
    //업로드 맵 상태 확인
    val uploadmapstate : MutableLiveData<Boolean> = MutableLiveData()
    //업로드 주소
    val uploadAddress : MutableLiveData<String> = MutableLiveData()
    //업로드 제목
    val uploadtitle : MutableLiveData<String> = MutableLiveData()
    //업로드 시작 날짜
    val uploadsDate: MutableLiveData<String> = MutableLiveData()
    //업로드 끝나는 날짜
    val uploadeDate: MutableLiveData<String> = MutableLiveData()
    //업로드 시간
    val uploadtime : MutableLiveData<String> = MutableLiveData()
    //업로드 내용
    val uploadcontent : MutableLiveData<String> = MutableLiveData()

    //현재 위치
    val currentlatLng: MutableLiveData<LatLng> = MutableLiveData()

    private val boardRepository : BoardRepository

    init {
        val boardDao : BoardDao =  AppDatabase.getDatabase(MyApplication.applicationContext()).boardDao()
        boardRepository  = BoardRepository(boardDao)
    }

    fun DataInsert(){

        var time = uploadtime.value!!.split("~")

        val placeVo:PlaceVo = PlaceVo()
        placeVo.title= uploadtitle.value.toString()
        placeVo.content = uploadcontent.value.toString()
        placeVo.startDate =uploadsDate.value.toString()
        placeVo.endDate = uploadeDate.value.toString()
        placeVo.startTime = time[0]
        placeVo.endTime = time[1]
        placeVo.latitude = uploadlatLng.value!!.latitude
        placeVo.longitude = uploadlatLng.value!!.longitude
        placeVo.address = uploadAddress.value.toString()

        AppContants.println("장소 확인:$placeVo")

        CoroutineScope(Dispatchers.Default).launch {
            boardRepository.DataInsert(placeVo,UploadFileLiveData.value!!)
        }

    }


}