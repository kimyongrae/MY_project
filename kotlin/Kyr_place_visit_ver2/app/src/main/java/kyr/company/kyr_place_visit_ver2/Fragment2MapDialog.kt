package kyr.company.kyr_place_visit_ver2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kyr.company.kyr_place_visit_ver2.common.AppContants
import kyr.company.kyr_place_visit_ver2.databinding.Fragment2MapDialogBinding
import java.io.IOException
import java.util.*

class Fragment2MapDialog: DialogFragment(), OnMapReadyCallback {

    private lateinit var binding : Fragment2MapDialogBinding

    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap

    var marker: Marker? = null

    private var latitude:Double = 0.0
    private var longitude:Double = 0.0
    private var address =""

    private var listener : OnAddressDateSendListner? = null

    companion object{

        fun newInstance(): Fragment2MapDialog? {
            return Fragment2MapDialog()
        }

    }

    fun setItemListner(listner: OnAddressDateSendListner){
        this.listener = listner
    }

    interface OnAddressDateSendListner{
        fun sendDate(address:String,latitude:Double,longitude:Double)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,R.style.FullscreenDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment2_map_dialog, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.select_map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        init()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        latitude = arguments!!.getDouble("latitude")
        longitude = arguments!!.getDouble("longitude")
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude,longitude),15f))
        //map 버튼 없애기
        mMap.uiSettings.isMyLocationButtonEnabled=false
        mMap.uiSettings.isZoomControlsEnabled=false
        mMap.uiSettings.isMapToolbarEnabled=true

        //맵을 클릭했을 경우
        mMap.setOnMapClickListener {
            
            //마커가 널이 아닐경우 지우기
            if(marker!=null){
                marker!!.remove()
            }

            marker = mMap.addMarker(MarkerOptions()
                .position(it)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
        
            //주소 업데이트
            address=getCurrentAddress(it)

            binding.address.text=address

            latitude=it.latitude
            longitude=it.longitude

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it,16f))


        }

    }


    //위도 경도를 받아서 주소로 변환 하는 메소드
    fun getCurrentAddress(latlng: LatLng): String {

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


    private fun init(){

        //뒤로가기 클릭시 다이얼로그 프래그먼트 종료
        binding.back.setOnClickListener {
            dismiss()
        }

        //확인 선택시 프래그먼트 종료 후 address 데이터를 보냄
        binding.confirm.setOnClickListener {
            if(marker==null){
                AppContants.toast("장소를 선택 한 후에 확인을 선택 해주세요")
                return@setOnClickListener
            }
            
            listener!!.sendDate(address,latitude,longitude)
            dismiss()
        }


    }



}