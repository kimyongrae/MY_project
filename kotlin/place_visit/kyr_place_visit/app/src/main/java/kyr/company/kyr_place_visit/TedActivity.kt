package kyr.company.kyr_place_visit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kyr.company.r9_rev_b.common.AppContants
import kyr.company.r9_rev_b.common.MyApplication
import java.util.ArrayList

class TedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ted)


        var PerMissionlistener : PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                MainActiviyStart()
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

    private fun MainActiviyStart() {
        Handler().postDelayed(
            {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            ,0)

    }


}