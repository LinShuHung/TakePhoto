package com.suhun.takephoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private String tag = MainActivity.class.getSimpleName();
    private ImageView img1, img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isSendPermissionRequestAboutCamera()){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 123);

        }else{
            initTakePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 123){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED){
                initTakePhoto();
            }else{
                finish();
            }
        }
    }

    public void takePhotoNotSaveToDevice(View view){

    }

    public void takePhotoSaveToDevice(View view){

    }

    private boolean isSendPermissionRequestAboutCamera(){
        boolean result = false;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            result = true;
        }

        return result;
    }

    private void initTakePhoto(){
        img1 = findViewById(R.id.lid_img1);
        img2 = findViewById(R.id.lid_img2);
    }
}