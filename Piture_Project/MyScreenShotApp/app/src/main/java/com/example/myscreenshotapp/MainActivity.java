package com.example.myscreenshotapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    SimpleDateFormat day = new SimpleDateFormat("yyyy년MM월dd일 HH:mm:ss");
    Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout=findViewById(R.id.Linear1);

        ActivityCompat.requestPermissions(this,new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    public void ScreenShotButton(View view){
        View view1= linearLayout;
        view1.setDrawingCacheEnabled(true);

        Bitmap bitmap=Bitmap.createBitmap(view1.getDrawingCache());
        view1.setDrawingCacheEnabled(false);

        String dirPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/MyFiles";
        File dir=new File(dirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        String filePath= Environment.getExternalStorageDirectory()+"/Download/"+ Calendar.getInstance().getTime().toString()+".jpg";
        File fileScreenshot=new File(filePath);

        FileOutputStream fileOutputStream=null;

        try {
            fileOutputStream = new FileOutputStream(fileScreenshot);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        Intent intent=new Intent(Intent.ACTION_VIEW);
        Uri uri=Uri.fromFile(fileScreenshot);
        intent.setDataAndType(uri,"image/jpeg");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);

    }

}
