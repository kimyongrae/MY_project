package com.example.bluetooth_test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    Button buttonON,buttonOFF,show;
    BluetoothAdapter myBluetoothAdapter;

    Intent btnEnablingIntent;
    int requestCodeForEnable;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonON=(Button)findViewById(R.id.btnON);
        buttonOFF=(Button)findViewById(R.id.btnOFF);

        show=(Button)findViewById(R.id.show);
        listView=(ListView) findViewById(R.id.listview);

        myBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        btnEnablingIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable=1;

        bluetoothONMethod();
        bluetoothOFFMethod();
        exButton();

    }

    private void bluetoothONMethod(){
        buttonON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetoothAdapter==null){
                    Toast.makeText(getApplicationContext(),"지원하지 않는 기기",Toast.LENGTH_SHORT).show();
                }else{
                    if(!myBluetoothAdapter.isEnabled()){
                        startActivityForResult(btnEnablingIntent,requestCodeForEnable);
                    }
                }

            }
        });
    }

    private  void bluetoothOFFMethod(){
        buttonOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetoothAdapter.isEnabled()){
                    myBluetoothAdapter.disable();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==requestCodeForEnable){

            if(resultCode==RESULT_OK){
                Toast.makeText(getApplicationContext(),"Bluetooth is Enable",Toast.LENGTH_SHORT).show();
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),"Bluetooth is Enabling cancle",Toast.LENGTH_SHORT).show();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void exButton(){
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> bt=myBluetoothAdapter.getBondedDevices();
                String [] strings= new String[bt.size()];
                int index=0;

                if(bt.size()>0){

                    for (BluetoothDevice device : bt){
                        strings[index]=device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
                    listView.setAdapter(arrayAdapter);
                }

            }
        });
    }



}
