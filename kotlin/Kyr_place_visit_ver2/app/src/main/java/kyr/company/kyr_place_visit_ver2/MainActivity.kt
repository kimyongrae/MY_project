package kyr.company.kyr_place_visit_ver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kyr.company.kyr_place_visit_ver2.common.AppContants
import kyr.company.kyr_place_visit_ver2.common.MyApplication
import kyr.company.kyr_place_visit_ver2.databinding.ActivityMainBinding

import java.util.ArrayList

class MainActivity : AppCompatActivity(),Fragment3.ScreenLisener {

    var binding: ActivityMainBinding? = null

    private var fragment1: Fragment1? = null
    private var fragment2: Fragment2? = null
    private var fragment3: Fragment3? = null
    private var fragment4: Fragment4? = null

    private var fragmentTransaction: FragmentTransaction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //binding 객체 사용
        init()

    }

    override fun onPause() {
        super.onPause()
    }

    private fun init() {

        requstPermission()

        if(fragment1 == null){
            fragment1 = Fragment1()
//            fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction!!.add(R.id.container1, fragment1!!).commit()
        }

        if(fragment2 == null){
            fragment2 = Fragment2()
//            fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction!!.add(R.id.container1, fragment2!!).commit()
        }

        if(fragment3 == null){
            fragment3 = Fragment3()
//            fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction!!.add(R.id.container1, fragment3!!).commit()
        }
        if(fragment4 == null){
            fragment4 = Fragment4()
//            fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction!!.add(R.id.container1, fragment4!!).commit()
        }

        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction!!.replace(R.id.container1, fragment1!!)
/*        // 해당 transaction 을 Back Stack 에 저장
        fragmentTransaction!!.addToBackStack(null);*/
        fragmentTransaction!!.commit()

//        fragmentControl(1)

        //네비게이션 맵 클릭시
        binding!!.navMap.setOnClickListener {
            fragmentControl(1)
        }

        //네비게이션 글쓰기 클릭시
        binding!!.navWrite.setOnClickListener {
            fragmentControl(2)
        }

        //네비게이션 달력 클릭시
        binding!!.navDate.setOnClickListener {
            fragmentControl(3)
        }

        //네비게이션 리스트 클릭시
        binding!!.navList.setOnClickListener {
            fragmentControl(4)
        }

    }

    private fun requstPermission() {

        var PerMissionlistener : PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {

            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                AppContants.toast("앱의 기능을 사용하기 위해서는 권한이 필요합니다.")
                finish()
            }

        }

        TedPermission.with(MyApplication.applicationContext())
                .setPermissionListener(PerMissionlistener)
                .setRationaleMessage("앱의 기능을 사용하기 위해서는 권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION)
                .check()
    }



    fun fragmentControl(i: Int): Boolean {

     /*   AppContants.println("호출:$i")
        if (fragment1 != null) supportFragmentManager!!.beginTransaction().hide(fragment1!!).commit()
        if (fragment2 != null) supportFragmentManager!!.beginTransaction().hide(fragment2!!).commit()
        if (fragment3 != null) supportFragmentManager!!.beginTransaction().hide(fragment3!!).commit()
        if (fragment4 != null) supportFragmentManager!!.beginTransaction().hide(fragment4!!).commit()*/

        return when(i){
            1->{
                binding!!.navMap.setImageResource(R.drawable.navigation_map_24_on)
                binding!!.navWrite.setImageResource(R.drawable.navigation_create_24)
                binding!!.navDate.setImageResource(R.drawable.navigation_date_24)
                binding!!.navList.setImageResource(R.drawable.navigation_list_24)

                fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction!!.replace(R.id.container1, fragment1!!)
/*        // 해당 transaction 을 Back Stack 에 저장
        fragmentTransaction!!.addToBackStack(null);*/
                fragmentTransaction!!.commit()
//                if (fragment1 != null) supportFragmentManager!!.beginTransaction().show(fragment1!!).commit()
                return true
            }
            2->{
                binding!!.navMap.setImageResource(R.drawable.navigation_map_24)
                binding!!.navWrite.setImageResource(R.drawable.navigation_create_24_on)
                binding!!.navDate.setImageResource(R.drawable.navigation_date_24)
                binding!!.navList.setImageResource(R.drawable.navigation_list_24)
                fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction!!.replace(R.id.container1, fragment2!!)
                fragmentTransaction!!.commit()
                return true
            }
            3->{
                binding!!.navMap.setImageResource(R.drawable.navigation_map_24)
                binding!!.navWrite.setImageResource(R.drawable.navigation_create_24)
                binding!!.navDate.setImageResource(R.drawable.navigation_date_24_on)
                binding!!.navList.setImageResource(R.drawable.navigation_list_24)
                fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction!!.replace(R.id.container1, fragment3!!)
                fragmentTransaction!!.commit()
                return true
            }
            4->{
                binding!!.navMap.setImageResource(R.drawable.navigation_map_24)
                binding!!.navWrite.setImageResource(R.drawable.navigation_create_24)
                binding!!.navDate.setImageResource(R.drawable.navigation_date_24)
                binding!!.navList.setImageResource(R.drawable.navigation_list_24_on)
                fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction!!.replace(R.id.container1, fragment4!!)
                fragmentTransaction!!.commit()
                return true
            }
            else -> return false
        }

    }

    //화면 전환 리스너
    override fun trans(data: Int) {
        fragmentControl(data)
    }


}