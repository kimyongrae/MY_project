package com.example.textrecognizer_test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public String imageFilePath;

    public Uri photoUri;

    public ImageView imageView;
    public EditText recognizerEdit;

    public int selectedPhotoMenu;

    public Context context;

    //사진을 찍을때
    public boolean isPhotoCaptured;
    //사진을 가져올때
    public  boolean isPhotoFileSaved;

    public int imageViewid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=(ImageView) findViewById(R.id.imageView);
        recognizerEdit=(EditText) findViewById(R.id.recognizer);

        imageView.setOnClickListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PackageManager.PERMISSION_GRANTED: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "승인이 허가되어 있습니다.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "카메라 승인을 받지 않았습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private boolean CamerahasPermission() {
        boolean cameraflag=true;
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            cameraflag=false;
        }
        return  cameraflag;

    }

    private void carmerPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, AppContants.MY_REQUEST_CAMERA);
    }

    private void requestWriteReadPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE}, AppContants.MY_REQUEST_WRITE_READ);
    }

    private boolean WriteReadPermission() {

        boolean caputerflag=true;
        if(ActivityCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            caputerflag=false;
        }
        return  caputerflag;

    }



    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return bitmap1;
    }

    private void WriteLimit(EditText fileExplain){
        fileExplain.setText(fileExplain.getText().toString().substring(0,AppContants.WRITE_MAX));
        fileExplain.setSelection(fileExplain.length());
        AppContants.toast(AppContants.WRITE_MAX+"글자 이상을 넘을수 없습니다.",context);
    }


    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {

/*                String fileName=photoFile.getName();
                AppContants.println("파일이름:"+fileName+"\n");*/
//              photoUri = FileProvider.getUriForFile(this, "com.example.samplecapture.fileprovider", photoFile);

                photoUri = FileProvider.getUriForFile(this, this.getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                AppContants.println(photoFile.getAbsolutePath() + "");

                startActivityForResult(takePictureIntent, AppContants.REQUEST_PHOTO_CAPTURE);

            }

        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss").format(new Date());
        String imageFileName = "Capture" + timeStamp;

        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,       //prefix
                ".jpg",          //suffix
                storageDir           //directory
        );

        if(imageViewid==1) {
            imageFilePath = image.getAbsolutePath();
        }

        return image;
    }


    private void detect(){

        //perform text detection here

        //TODO 1. define TextRecognizer
        TextRecognizer recognizer=new TextRecognizer.Builder(MainActivity.this).build();

        //TODO 2. Get bitmap from imageview
        Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();

        //TODO 3. get frame from bitmap
        Frame frame=new Frame.Builder().setBitmap(bitmap).build();

        //TODO 4. get data from frame
        SparseArray<TextBlock> sparseArray =recognizer.detect(frame);

        //TODO 5. set data on textview
        StringBuilder stringBuilder = new StringBuilder();

        for (int i=0; i< sparseArray.size(); i++){
            TextBlock tx=sparseArray.get(i);
            String str=tx.getValue();

            stringBuilder.append(str);

        }
        recognizerEdit.setText(stringBuilder);

    }

    public boolean PhotoSetImage() {
        Bitmap bitmap;
        /*Bitmap bitmap = decodeSampledBitmapFromResource(new File(imageFilePath), imageView.getWidth(), imageView.getHeight());*/
        ExifInterface exif = null;
        File res;

            bitmap=decodeSampledBitmapFromResource(new File(imageFilePath), imageView.getWidth(), imageView.getHeight());


        if (bitmap == null) {
            if(imageViewid==1){
                imageFilePath="";
            }
            return false;
        }

        try {

            if(imageViewid==1){
                exif=new ExifInterface(imageFilePath);
            }

//            exif = new ExifInterface(imageFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        int exifOrientation;
        int exifDegree;

        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }
        Log.d("setImageBitmap", "setImageBitmap");


        if (imageViewid == 1) {
            imageView.setImageBitmap(rotate(bitmap, exifDegree));
            detect();
        } else if (imageViewid == 2) {
//            imageView2.setImageBitmap(rotate(bitmap, exifDegree));
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == AppContants.REQUEST_PHOTO_CAPTURE) {
            boolean result=PhotoSetImage();
            isPhotoCaptured = true;
        }

        if (requestCode == AppContants.REQ_PHOTO_SELECTION) {
            if (data != null) {
                AppContants.println("onActivityResult() for REQ_PHOTO_SELECTION.");

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                //resultPhotoBitmap = decodeSampledBitmapFromResource(new File(filePath), imageView.getWidth(), imageView.getHeight());
                if(imageViewid==1){
                    imageFilePath=filePath;
                }
                PhotoSetImage();

                //imageView.setImageBitmap(resultPhotoBitmap);

                isPhotoFileSaved = true;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    public void showPhotoSelectionActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, AppContants.REQ_PHOTO_SELECTION);
    }


    public static Bitmap decodeSampledBitmapFromResource(File res, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(res.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(res.getAbsolutePath(), options);
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height;
            final int halfWidth = width;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView:
                imageViewid = 1;
                break;
        }
        showDialog1(AppContants.CONTENT_PHOTO_EX);
    }


    public void showDialog1(int id) {
        AlertDialog.Builder builder = null;

        switch (id) {

            case AppContants.CONTENT_PHOTO_EX:
                selectedPhotoMenu = 0;
                builder = new AlertDialog.Builder(this);
                builder.setTitle("사진 메뉴 선택");
                builder.setSingleChoiceItems(R.array.array_photo_ex, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        selectedPhotoMenu = whichButton;
                    }
                });
                builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (selectedPhotoMenu == 0) {
                            if(CamerahasPermission()){
                                sendTakePhotoIntent();
                            }else{
                                AppContants.toast("카메라 권한이 필요합니다",getApplicationContext());
                                carmerPermission();
                            }
                        } else if (selectedPhotoMenu == 1) {
                            if(WriteReadPermission()) {
                                showPhotoSelectionActivity();
                            }else{
                                AppContants.toast("쓰기 읽기 권한이 필요합니다",getApplicationContext());
                                requestWriteReadPermissions();
                            }
                        } else if (selectedPhotoMenu == 2) {
//                          isPhotoCanceled = true;
                            isPhotoCaptured = false;
                            isPhotoFileSaved = false;

                            if(imageViewid==1){
                                imageView.setImageResource(R.drawable.no_image);
                            }
                            if(imageViewid==2){
//                                imageView2.setImageResource(R.drawable.no_image);
                            }


                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                break;

            default:
                break;
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
