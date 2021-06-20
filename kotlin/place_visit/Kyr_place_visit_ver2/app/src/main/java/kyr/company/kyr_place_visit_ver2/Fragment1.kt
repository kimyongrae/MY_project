package kyr.company.kyr_place_visit_ver2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng

import kyr.company.kyr_place_visit_ver2.common.AppContants
import kyr.company.kyr_place_visit_ver2.common.MyApplication
import kyr.company.kyr_place_visit_ver2.databinding.Fragment1Binding
import kyr.company.kyr_place_visit_ver2.viewmodel.UploadViewModel
import java.io.IOException
import java.lang.Exception
import java.util.*


class Fragment1 : Fragment(), OnMapReadyCallback {

    private lateinit var binding : Fragment1Binding

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

    var locationCallback: LocationCallback? =null

    //맵 선택 라이브 데이터
    private lateinit var mainviewmodel : UploadViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment1, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainviewmodel= ViewModelProvider(requireActivity()).get(UploadViewModel::class.java)
        mapView = view.findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        initUI()
    }

    override fun onResume() {
        super.onResume()

        locationcall()
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
        closeDrawer()
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

    private fun initUI() {

        binding?.navDrawer!!.drawerLayoutBack.setOnClickListener{
            closeDrawer()
        }

        binding?.navDrawer!!.drawerLayoutMyinfo.setOnClickListener {
            myinfo()
        }

        binding!!.navDrawer.drawerLayoutNote.setOnClickListener {
            AppNote()
        }

        locationRequest = LocationRequest().
            setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

        //현재 위치
        binding.currentGps.setOnClickListener {
            startLocationUpdates()
        }

        binding.search.setOnClickListener {

            val dialog :Fragment1SearchDialog = Fragment1SearchDialog.newInstance()!!
            dialog.show(fragmentManager!!,"SearchDialog")

        }

    }

    private fun locationcall(){

        locationCallback = object :LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                try {
                    //가장최근의 위치를 가지고옴
                    location = locationResult.lastLocation
                    //currentPosition 객체에 위도 경도 설정
                    currentPosition = LatLng(location!!.latitude, location!!.longitude)

                    mainviewmodel.currentlatLng.value = currentPosition
                }catch (e:Exception){
                    e.printStackTrace()
                }
                var cameraUpdate : CameraUpdate? =null

                //처음에만 defalut_zoom 으로 설정
                if(init_flag){
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(mainviewmodel.currentlatLng.value, DEFAULT_ZOOM)
                    init_flag=false

                }else{

                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(mainviewmodel.currentlatLng.value,16f)
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
    }

    /**
     * 변화된 위치를 콜백해주는 함수
     * requestLocationUpdates 요청시 onLocationResult가 자동적으로 호출됨
     */

    /*locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            //가장최근의 위치를 가지고옴
            location = locationResult.lastLocation

            //currentPosition 객체에 위도 경도 설정
            currentPosition = LatLng(location!!.latitude, location!!.longitude)

            var cameraUpdate : CameraUpdate? =null
            
            //처음에만 defalut_zoom 으로 설정
            if(init_flag){
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentPosition, DEFAULT_ZOOM)
                init_flag=false

            }else{

                cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentPosition,16f)
                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                //내위치 표시
                mMap.isMyLocationEnabled=true

            }

            mMap.moveCamera(cameraUpdate)
            
            //gps 로케이션 업데이트 중지
            mFusedLocationClient!!.removeLocationUpdates(this)

        }
    }*/

    //정보창 클릭 리스너
    private fun startLocationUpdates() {

        //해당 함수 호출시 콜백방식으로  onLocationResult 를 호출해줌

        //권한체크
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    //위도 경도를 받아서 주소로 변환 하는 메소드
    fun getCurrentAddress(latlng: LatLng): String? {

        //지오코더... GPS를 주소로 변환
        return if (activity != null) {
            val geocoder = Geocoder(activity, Locale.getDefault())
            val addresses: List<Address>
            addresses = try {
                geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    7
                )
            } catch (ioException: IOException) {
                //네트워크 문제
                AppContants.println("지오코더 서비스 사용불가")
                return "지오코더 서비스 사용불가"
            } catch (illegalArgumentException: IllegalArgumentException) {
                AppContants.println("잘못된 GPS 좌표")
                return "잘못된 GPS 좌표"
            }

            if (addresses == null || addresses.isEmpty()) {
                AppContants.println("주소 미발견")
                return "주소 미발견"
            }
            else {
                val address = addresses[0]
                """
             ${address.getAddressLine(0)}
     """.trimIndent()
            }
        }

        else {
            return "loading"
        }

    }



    private fun AppNote() {
        AppContants.println("AppNote 클릭")
        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun closeDrawer(): Unit{

        if(binding?.drawerLayout!!.isDrawerOpen(GravityCompat.START)){
            binding?.drawerLayout!!.closeDrawer(GravityCompat.START)
        }

    }

    private fun myinfo(): Unit{

        AppContants.println("myinfo 클릭")
        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        
        mMap = googleMap
        //map 버튼 없애기
        mMap.uiSettings.isMyLocationButtonEnabled=false
        mMap.uiSettings.isZoomControlsEnabled=false
        mMap.uiSettings.isMapToolbarEnabled=true

        //초기 위치 설정
        setDefaultLocation();

    }

    private fun setDefaultLocation() {

        if(mainviewmodel.currentlatLng.value==null){
            val seoul = LatLng(37.52487, 126.92723)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 10f))
        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainviewmodel.currentlatLng.value, 15f))
        }


        //권한이 없을 경우 fusedLocation  실행 안함
            if (ActivityCompat.checkSelfPermission(MyApplication.applicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MyApplication.applicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

    }

    fun String.toEditable(): Editable{
       return Editable.Factory.getInstance().newEditable(this)
    }



}