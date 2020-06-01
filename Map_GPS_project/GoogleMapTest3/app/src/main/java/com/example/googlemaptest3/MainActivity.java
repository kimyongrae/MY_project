package com.example.googlemaptest3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isServicesOK()){
            init();
        }

    }

    private void init(){
        Button BtnMap=(Button)findViewById(R.id.btnMap);
        BtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MapActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: chekcing google service version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG,"isServicesOK: Google play service is working");
            return  true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG,"isServicesOK: an error occured but we can fix it");
            Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(getApplicationContext(),"you can't map requests",Toast.LENGTH_SHORT).show();
        }
        return false;
    }



}
