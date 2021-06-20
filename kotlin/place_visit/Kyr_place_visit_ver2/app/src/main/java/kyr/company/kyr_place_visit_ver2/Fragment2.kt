package kyr.company.kyr_place_visit_ver2

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile
import kyr.company.kyr_place_visit_ver2.adapter.PictureAdapter
import kyr.company.kyr_place_visit_ver2.common.AppContants
import kyr.company.kyr_place_visit_ver2.common.MyApplication
import kyr.company.kyr_place_visit_ver2.databinding.Fragment2Binding
import kyr.company.kyr_place_visit_ver2.model.FileVo
import kyr.company.kyr_place_visit_ver2.viewmodel.UploadViewModel
import java.util.*


class Fragment2 : Fragment(), OnMapReadyCallback,DatePickerFragment.OnInputDateSelected {

    private lateinit var binding : Fragment2Binding

    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap

    //GPS 변수
    private var mFusedLocationClient : FusedLocationProviderClient? = null //위치 업데이트 객체
    private var locationRequest : LocationRequest? = null//위치요청 설정 객체
    private var location: Location?  = null

    //위도 경도를 담는 변수
    var currentPosition: LatLng? = null

    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    private val UPDATE_INTERVAL_MS : Long = 0 // 1초
    private val FASTEST_UPDATE_INTERVAL_MS : Long = 0 // 0.5초

    //카메라 줌 변수
    private val DEFAULT_ZOOM = 15f
    private var init_flag : Boolean = true

    private var Latitude:Double = 0.0
    private var Longitude:Double = 0.0
    private var Address =""

    var marker: Marker? = null

    //맵 선택 라이브 데이터
    private lateinit var mainviewmodel : UploadViewModel

    private lateinit var pictureAdapter: PictureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment2, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainviewmodel= ViewModelProvider(requireActivity()).get(UploadViewModel::class.java)

        mapView = view.findViewById(R.id.write_map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        initUI()
    }

    private fun initUI() {

          pictureAdapter = PictureAdapter()
          binding.picuterRecycler.layoutManager=LinearLayoutManager(MyApplication.applicationContext(),LinearLayoutManager.HORIZONTAL,false)
          binding.picuterRecycler.adapter=pictureAdapter
        
        //업로드 될 파일
        mainviewmodel.UploadFileLiveData.observe(requireActivity(), Observer{
            pictureAdapter.setItems(it)
            pictureAdapter.notifyDataSetChanged()
        })
        
        //제목
        mainviewmodel.uploadtitle.observe(requireActivity(), Observer {
            binding.title.text=it
        })

        //주소
        mainviewmodel.uploadAddress.observe(requireActivity(), Observer {
            binding.address.text=it
        })

        //시간
        mainviewmodel.uploadtime.observe(requireActivity(), Observer {
            binding.time.text = it.toEditable()
        })

        //시작 날짜
        mainviewmodel.uploadsDate.observe(requireActivity(), Observer {
            binding.sDateText.text=it.toEditable()
        })

        //끝나는 날짜
        mainviewmodel.uploadeDate.observe(requireActivity(), Observer {
            binding.eDateText.text=it.toEditable()
        })
        
       /* //내용
        mainviewmodel.uploadcontent.observe(requireActivity(), Observer {
            AppContants.println("내용데이터 호출:$it")
            binding.contents.text=it.toEditable()
        })*/
        
        //맵초기화
       mainviewmodel.uploadmapstate.observe(requireActivity(), Observer {
           if(it){
               if(mainviewmodel.uploadlatLng.value!=null){

                   binding.mapNavitext.text=""

                   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainviewmodel.uploadlatLng.value, DEFAULT_ZOOM))
                   //마커가 널이 아닐경우 지우기
                   if(marker!=null){
                       marker!!.remove()
                   }

                   marker = mMap.addMarker(
                       MarkerOptions()
                           .position(mainviewmodel.uploadlatLng.value!!)
                           .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
               }
           }
       })

        locationRequest = LocationRequest().
        setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())


        //시작 날짜
        binding.sDate.setOnClickListener {
            //스위치가 ON일때 달력을 띄움
            showDataPicker(AppContants.startDate)
        }

        //날짜 시간 초기화
        binding.sDateText.text = nowCalendar().toEditable()
        binding.eDateText.text = nowCalendar().toEditable()
        binding.time.text="00:00~23:59".toEditable()

        if(mainviewmodel.uploadsDate.value==null){
            mainviewmodel.uploadsDate.value = binding.sDateText.text.toString()
        }

        if(mainviewmodel.uploadeDate.value==null){
            mainviewmodel.uploadeDate.value = binding.eDateText.text.toString()
        }

        if(mainviewmodel.uploadtime.value==null){
            mainviewmodel.uploadtime.value = binding.time.text.toString()
        }

        //제목 입력을 클릭했을 경우
        binding.titleEdit.setOnClickListener {
            val dialog: Fragment2TitleDialog = Fragment2TitleDialog(context!!)
            dialog.startDialog()

            dialog.setItemListner(object: Fragment2TitleDialog.OntitleListner{
                override fun sendDate(title: String) {
                    //전달받은 타이틀
//                    binding.title.text=title

                    mainviewmodel.uploadtitle.value=title
                }
            })
        }


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

                mainviewmodel.uploadtime.value="${startTime}~${endTime}"

//                binding.time.text = "${startTime}~${endTime}".toEditable()

            }
        }

        //데이터 저장
        binding.save.setOnClickListener {
//            binding.contents.text
            //내용을 가져와서 세팅 해줌
            mainviewmodel.uploadcontent.value=binding.contents.text.toString()

            if(mainviewmodel.uploadtitle.value==null){
                AppContants.toast("제목을 확인 해주세요")
                return@setOnClickListener
            }

            if(mainviewmodel.uploadcontent.value==null || mainviewmodel.uploadcontent.value == ""){
                AppContants.toast("내용을 확인 해주세요")
                return@setOnClickListener
            }

            if(mainviewmodel.uploadlatLng.value==null){
                AppContants.toast("장소를 선택 해주세요")
                return@setOnClickListener
            }

            mainviewmodel.DataInsert()
        }



        //사진을 추가합니다.
        binding.pictureAdd.setOnClickListener {
/*            val intent: Intent = Intent(Intent.ACTION_PICK,MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type="image/*"
            startActivityForResult(Intent.createChooser(intent,"Select Photos"),AppContants.PICTURE_REQUEST_CODE)*/
 */
/*            val intent: Intent = Intent(MyApplication.applicationContext(), AlbumSelectActivity::class.java)
            intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 4)
            startActivityForResult(intent, AppContants.PICTURE_REQUEST_CODE)*/

            val intent = Intent(MyApplication.applicationContext(), FilePickerActivity::class.java)
            intent.putExtra(
                FilePickerActivity.CONFIGS, Configurations.Builder()
                    .setCheckPermission(true)
                    .setShowImages(true)
                    .enableImageCapture(true)
                    .setMaxSelection(4)
                    .setSkipZeroSizeFiles(true)
                    .build()
            )
            startActivityForResult(intent, AppContants.PICTURE_REQUEST_CODE)

        }

    }
    
    //사진 화면 요청
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode != RESULT_OK) {
            return
        }

        if (resultCode == RESULT_OK && data!=null && requestCode == AppContants.PICTURE_REQUEST_CODE) {
            val files: ArrayList<MediaFile> = data.getParcelableArrayListExtra<MediaFile>(FilePickerActivity.MEDIA_FILES) as ArrayList<MediaFile>
            val filedata : MutableList<FileVo> = mutableListOf()
            for (i in files.indices){
                val filepath=getPathFromUri(files[i].uri)
                filedata.add(FileVo(filepath))
            }

            mainviewmodel.UploadFileLiveData.value=filedata
        }

/*        if(resultCode == RESULT_OK){
            val uri =data!!.data
            val clipData = data!!.clipData

            if(clipData!=null){
                AppContants.println("아이템 갯수:${clipData.itemCount}")
                val itemsize=(clipData.itemCount)-1
                for (i in 0.. itemsize){
                    val uri =clipData.getItemAt(i).uri
                    AppContants.println("$uri")
                }
            }
        }*/

    }

    fun getPathFromUri(uri: Uri): String {
        val cursor: Cursor? = context!!.contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToNext()
        val path: String = cursor.getString(cursor.getColumnIndex("_data"))
        cursor.close()
        return path
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


    //날짜를 보여주는 다이얼로그 프래그 먼트
    private fun showDataPicker(value: String) {
        val args = Bundle()
        args.putString("key", value)
        val newFragment: DialogFragment = DatePickerFragment()
        newFragment.arguments = args
        newFragment.setTargetFragment(this, 1)
        newFragment.show(fragmentManager!!, "datePicker")
    }

    //장소 업데이트
    private fun startLocationUpdates() {

        //해당 함수 호출시 콜백방식으로  onLocationResult 를 호출해줌
        //권한체크
        if (ActivityCompat.checkSelfPermission(MyApplication.applicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApplication.applicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


    /**
     * 변화된 위치를 콜백해주는 함수
     * requestLocationUpdates 요청시 onLocationResult가 자동적으로 호출됨
     */
    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            //가장최근의 위치를 가지고옴
            location = locationResult.lastLocation

            //currentPosition 객체에 위도 경도 설정
            currentPosition = LatLng(location!!.latitude, location!!.longitude)

            var cameraUpdate : CameraUpdate? =null

            //처음에만 defalut_zoom 으로 설정
            if(init_flag){

                if(mainviewmodel.uploadlatLng.value!=null){
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(mainviewmodel.uploadlatLng.value, DEFAULT_ZOOM)
                    init_flag=false
                }else{
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentPosition, DEFAULT_ZOOM)
                    init_flag=false
                }

            }else{

                if(mainviewmodel.uploadlatLng.value!=null){
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(mainviewmodel.uploadlatLng.value, DEFAULT_ZOOM)
                }else{
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentPosition,15f)
                }

                if (ActivityCompat.checkSelfPermission(MyApplication.applicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MyApplication.applicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                //내위치 표시
                mMap.isMyLocationEnabled=true

            }

            mMap.moveCamera(cameraUpdate)

            //gps 로케이션 업데이트 중지
            mFusedLocationClient!!.removeLocationUpdates(this)

        }
    }

    fun String.toEditable(): Editable {
        return Editable.Factory.getInstance().newEditable(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppContants.println("fragment2 onAttach")
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        //map 버튼 없애기
        mMap.uiSettings.isMyLocationButtonEnabled=false
        mMap.uiSettings.isZoomControlsEnabled=false
        mMap.uiSettings.isMapToolbarEnabled=true

        //초기 위치 설정
//        startLocationUpdates()
        setDefaultLocation()

        if(mMap==googleMap){
            mainviewmodel.uploadmapstate.value=true
        }

        if(mainviewmodel.uploadmapstate.value==true){

        //맵을 클릭했을 경우
        mMap.setOnMapClickListener {

            val seoul = LatLng(37.52487, 126.92723)

            //map dialog를 띄움
            val args = Bundle()
            if(mainviewmodel.currentlatLng.value != null ){
                args.putDouble("latitude", mainviewmodel.currentlatLng.value!!.latitude)
                args.putDouble("longitude",mainviewmodel.currentlatLng.value!!.longitude)
            }else{
                args.putDouble("latitude", seoul.latitude)
                args.putDouble("longitude",seoul.longitude)
            }
            val dialog :Fragment2MapDialog = Fragment2MapDialog.newInstance()!!
            dialog.arguments=args
            dialog.setTargetFragment(this,1)
            dialog.show(fragmentManager!!,"MapDialog")

            dialog.setItemListner(object: Fragment2MapDialog.OnAddressDateSendListner{
                override fun sendDate(address: String, latitude: Double, longitude: Double) {
                    Address=address
                    Latitude=latitude
                    Longitude=longitude
                    binding.mapNavitext.text=""
//                    binding.address.text=Address

                    mainviewmodel.uploadAddress.value=Address
                    val latLng=LatLng(latitude,longitude)

                    mainviewmodel.uploadlatLng.value = latLng
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainviewmodel.uploadlatLng.value,15f))

                    if(marker!=null){
                        marker!!.remove()
                    }

                    marker = mMap.addMarker(
                        MarkerOptions()
                            .position(mainviewmodel.uploadlatLng.value!!)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))

                }
            })
        }

        }

    }

    private fun setDefaultLocation() {

        val seoul = LatLng(37.52487, 126.92723)
        if(mainviewmodel.currentlatLng.value!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainviewmodel.currentlatLng.value, 10f))
        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 10f))
        }

        //권한이 없을 경우 fusedLocation  실행 안함
        if (ActivityCompat.checkSelfPermission(MyApplication.applicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(MyApplication.applicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

    }


    override fun sendDate(date: String, dateType: String) {


        when(dateType){
            AppContants.startDate -> {
                val compare:Int =date.compareTo(binding.eDateText.text.toString())
//                AppContants.println("어떤게 더크냐:$compare")
                if(compare>0){
                    AppContants.toast("시작일자를 다시 설정해주세요")
                    return
                }

                mainviewmodel.uploadsDate.value=date
            }
            else->{

                val compare:Int =date.compareTo(binding.sDateText.text.toString())

                if(compare<0){
                    AppContants.toast("종료일자를 다시 설정해주세요")
                    return
                }
                mainviewmodel.uploadeDate.value=date
            }
        }

    }

}