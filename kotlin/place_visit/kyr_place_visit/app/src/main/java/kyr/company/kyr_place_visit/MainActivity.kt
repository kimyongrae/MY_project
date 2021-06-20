package kyr.company.kyr_place_visit

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kyr.company.kyr_place_visit.databinding.ActivityMainBinding
import kyr.company.r9_rev_b.common.AppContants
import kyr.company.r9_rev_b.common.MyApplication
import java.util.*


class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null

    private var fragment1: Fragment1 ? = null
    private var fragment2: Fragment2 ? = null
    private var fragment3: Fragment3 ? = null
    private var fragment4: Fragment4 ? = null

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
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction!!.add(R.id.container1, fragment1!!).commit()
        }

        if(fragment2 == null){
            fragment2 = Fragment2()
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction!!.add(R.id.container1, fragment2!!).commit()
        }

        if(fragment3 == null){
            fragment3 = Fragment3()
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction!!.add(R.id.container1, fragment3!!).commit()
        }
        if(fragment4 == null){
            fragment4 = Fragment4()
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction!!.add(R.id.container1, fragment4!!).commit()
        }

        supportFragmentManager.beginTransaction().show(fragment1!!).commit()
        supportFragmentManager.beginTransaction().hide(fragment2!!).commit()
        supportFragmentManager.beginTransaction().hide(fragment3!!).commit()
        supportFragmentManager.beginTransaction().hide(fragment4!!).commit()

        //바텀 네비게이션 메뉴 고정 시키기
        BottomNavigationHelper.disableShiftMode(binding!!.bottomNavigation)

        binding!!.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.tab1 -> fragmentControl(1)
                R.id.tab2 -> fragmentControl(2)
                R.id.tab3 -> fragmentControl(3)
                R.id.tab4 -> fragmentControl(4)
            }
            true
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




    private fun fragmentControl(i: Int): Boolean {

        AppContants.println("호출:$i")
        if (fragment1 != null) supportFragmentManager!!.beginTransaction().hide(fragment1!!).commit()
        if (fragment2 != null) supportFragmentManager!!.beginTransaction().hide(fragment2!!).commit()
        if (fragment3 != null) supportFragmentManager!!.beginTransaction().hide(fragment3!!).commit()
        if (fragment4 != null) supportFragmentManager!!.beginTransaction().hide(fragment4!!).commit()

        return when(i){
            1->{
                if (fragment1 != null) supportFragmentManager!!.beginTransaction().show(fragment1!!).commit()
                return true
            }
            2->{
                if (fragment2 != null) supportFragmentManager!!.beginTransaction().show(fragment2!!).commit()
                return true
            }
            3->{
                if (fragment3 != null) supportFragmentManager!!.beginTransaction().show(fragment3!!).commit()
                return true
            }
            4->{
                if (fragment4 != null) supportFragmentManager!!.beginTransaction().show(fragment4!!).commit()
                return true
            }
            else -> return false
        }

    }


}