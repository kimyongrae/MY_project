package com.example.textrecognizer_test;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import java.text.SimpleDateFormat;

public class AppContants {

    public static String DATABASE_NAME = "CalvusRevisionD.db";


    //db insert,modify 상태 처리
    public static final int MODE_INSERT = 1;
    public static final int MODE_MODIFY = 2;
    public static final int MODE_READ =3;

    //블루투스 권한 데이터
    public static final int REQUEST_ENABLE_BLUETOOTH=10;

    //모든 권한 읽기,쓰기,GPS 관련 권한들
    public static final int MY_REQUEST_PERMISSIONS=20;
    //위치 권한
    public static final int MY_REQUEST_LOCATION=21;
    //쓰기,읽기 권한
    public static final int MY_REQUEST_WRITE_READ=22;
    //카메라 찍는 권한
    public static final int MY_REQUEST_CAMERA=23;

    //화면 캡쳐 권한
    public static final int REQUEST_PHOTO_CAPTURE = 672;

    //사진찍은후 메뉴
    public static final int CONTENT_PHOTO_EX = 106;
    public static final int REQ_PHOTO_SELECTION=107;

    //데이터 날짜 포맷
    public static SimpleDateFormat dateFormat4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateFormat5 = new SimpleDateFormat("yyyy-MM-dd");

    public static SimpleDateFormat dataFormat1 = new SimpleDateFormat("HH시mm분ss초");

    //LOG 및 디버그 용
    private static final String TAG = "AppConstants";
    public static void println(final String data) {
        Log.d(TAG,data);
    }
    public static void toast(final String data, Context context){
        Toast.makeText(context,data,Toast.LENGTH_SHORT).show();
    }

    public static int WRITE_MAX=15;


}
