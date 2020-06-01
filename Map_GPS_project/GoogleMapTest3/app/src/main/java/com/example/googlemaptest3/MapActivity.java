package com.example.googlemaptest3;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements  OnMapReadyCallback{


    private static final String TAG="MapActivity";
    private static final String FINE_LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LoCATION_PERMISSION_REQUEST_CODE=1234;
    private static final float DEFAULT_ZOOM=16f;

    //widget
    private EditText mSearchText;
    private ImageView mGps;

    private Boolean mLocationPermissionsGranted=false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mSearchText=(EditText)findViewById(R.id.input_search);

        mGps=(ImageView)findViewById(R.id.ic_gps);

        getLocationPermission();

        init();
    }

    private void init(){
        Log.d(TAG,"init: initializing");
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent KeyEvent) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || KeyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || KeyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    getLocate();
                }

                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: clicked gps Icon");
                getDeviceLocation();
            }
        });

        hideSoftKeyboard();
    }

    private void getLocate(){
        Log.d(TAG,"geoLocate:geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString,7);
        }catch (IOException e){
            Log.d(TAG,"geoLocate:IOException:"+e.getMessage());
        }

        if(list.size() >0){
            Address address=list.get(0);

            Log.d(TAG,"geoLocate:found a location:"+address.toString());

            moveCarmera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }

    }

    private void getDeviceLocation(){
        Log.d(TAG,"getDeviceLocation:getting the device current location");
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        try {
            if(mLocationPermissionsGranted){

                Task location=mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if(task.isSuccessful()){
                            Log.d(TAG,"oncomplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            moveCarmera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM,"My Location");
                        }
                        else{
                            Log.d(TAG,"oncomplete: current location is null");
                            Toast.makeText(getApplicationContext(),"unable to get current location",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        }catch (Exception e){
            Log.d(TAG,"getDeviceLocation:Exceptiong"+e.getMessage());
        }

    }
    private void moveCarmera(LatLng latLng,float zoom,String title){
        Log.d(TAG,"moveCamera: moving the camera to lat:"+latLng.latitude + ", lng"+latLng.longitude);

        Log.d("title",title);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if(!title.equals("My Location")){

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

        hideSoftKeyboard();

    }


    private void initMap() {
        Log.d(TAG,"initMap:initalizing map");
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission(){

        Log.d(TAG,"getLocationPermmsion:getting location permissions");

        String [] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if((ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)&&
            (ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED)) {
            mLocationPermissionsGranted = true;
            initMap();
        }
         else {
            ActivityCompat.requestPermissions(this, permissions, LoCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult: called.");
        mLocationPermissionsGranted=false;


        switch (requestCode){
            case LoCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length >0){
                    for (int i=0; i<grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted=false;
                            return;
                        }
                    }
                    Log.d(TAG,"onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;

                    initMap();
                }
            }



        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d("asdsada","Ready");
        Toast.makeText(getApplicationContext(),"map Ready",Toast.LENGTH_SHORT).show();
        mMap=googleMap;

        if(mLocationPermissionsGranted){
            getDeviceLocation();

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);


            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED &&
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {
                return;
            }

            //나의 위치정보 상태
            mMap.setMyLocationEnabled(true);


            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }

    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


}
