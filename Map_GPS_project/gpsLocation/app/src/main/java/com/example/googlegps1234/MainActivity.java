package com.example.googlegps1234;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Button button;

    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);

        CallPermissions();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=0;
                requestLocationUpdates();
            }
        });

    }

    public void requestLocationUpdates(){


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        fusedLocationProviderClient=new FusedLocationProviderClient(this);
        locationRequest=new LocationRequest();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(0);
        locationRequest.setInterval(0);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(flag==0){
                    Log.d("TAG","lat"+locationResult.getLastLocation().getLatitude()
                            +"long"+locationResult.getLastLocation().getLongitude());
                    flag=1;
                }
            }
        },getMainLooper());

        }else {
            CallPermissions();
        }

    }

    public void CallPermissions(){
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
        String rationale = "Please provide location permission so that you can ...";
        Permissions.Options options = new Permissions.Options()
                .setSettingsDialogTitle("Warning")
                .setRationaleDialogTitle("LocationPermission");

        Permissions.check(this, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
//                requestLocationUpdates();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                CallPermissions();
            }
        });
    }

}
