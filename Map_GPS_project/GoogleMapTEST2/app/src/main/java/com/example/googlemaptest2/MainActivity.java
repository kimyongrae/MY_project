package com.example.googlemaptest2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    private static String TAG="MainActivity";

    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private static final int UPDATE_INTERVAL_MS = 0;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 0; // 0.5초

    private Marker currentMakrer;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;

    LatLng currentPosition;

    ArrayList<Marker> marker;
    Marker currentMarker;
    Location location;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RelativeLayout mapReleactive =findViewById(R.id.mapRelactive);
        final RelativeLayout searchRelative = findViewById(R.id.searchRelative);

        EditText search=findViewById(R.id.search);

        //화면 계속 켜놓기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        //셋팅이 설정되어있는지 확인 할때 사용함
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        button = findViewById(R.id.cilck);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "지도없애기", Toast.LENGTH_SHORT).show();

                mapReleactive.setVisibility(View.VISIBLE);
                searchRelative.setVisibility(View.GONE);

                if(marker.size()<=0){
                    return;
                }

                for (int i = 0; i < marker.size(); i++) {
                    marker.get(i).remove();
                }
            }



        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapReleactive.setVisibility(View.GONE);
                searchRelative.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MarkerOptions markerOptions;
        mMap = googleMap;

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {

                // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                        Toast.makeText(getApplicationContext(),"권한이 필요합니다",Toast.LENGTH_SHORT).show();
                }

                else {
                    // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                    // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                    ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
                }


        }

        marker = new ArrayList<>();

        LatLng SEOUL = new LatLng(37.56, 126.97);
        LatLng busan = new LatLng(37.58, 127.97);

        markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");

        marker.add(mMap.addMarker(new MarkerOptions().position(busan).title("부산").snippet("스니퍼")));
        marker.add(mMap.addMarker(markerOptions));


/*        LatLng donghae = new LatLng(37.59, 126.99);

        MarkerOptions markerOptions1;
        markerOptions1 = new MarkerOptions();

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.testimage);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);

        markerOptions1.position(donghae).title("gg").snippet("안녕하세요").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        currentmarker = mMap.addMarker(markerOptions1);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(donghae, 16));*/

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        setMapMyLocation();

        if(checkPermission()){
            startLocationUpdates();
        }

    }

    public void setMapMyLocation(){

        if(checkPermission()) {
            if (mMap != null)
                mMap.setMyLocationEnabled(true);
        }

    }


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);


            location=locationResult.getLastLocation();
            currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude()) + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet);

                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);
            }

    };

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {


        if (currentMarker != null)
            currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);


    }


    //퍼미션 체크여부
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            return true;
        }

        return false;

    }


    private void startLocationUpdates()
    {
            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

    }


    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    7);


        } catch (IOException ioException) {
            //네트워크 문제
            Log.d(TAG,"지오코더 서비스 사용불가");
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.d(TAG,"잘못된 GPS 좌표");
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Log.d(TAG,"주소 미발견");
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString()+"\n";
        }

    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {

        if (mFusedLocationClient != null) {
            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }

        super.onStop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==PERMISSIONS_REQUEST_CODE
                && grantResults[0]==PackageManager.PERMISSION_GRANTED
                && grantResults[1]==PackageManager.PERMISSION_GRANTED)
        {
            setMapMyLocation();
            startLocationUpdates();
        }

        else{


        }

    }//end onRequestPermissionsResult



}
