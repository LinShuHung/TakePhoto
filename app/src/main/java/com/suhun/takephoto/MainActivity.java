package com.suhun.takephoto;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private String tag = MainActivity.class.getSimpleName();
    private ImageView img1, img2;
    private File saveDir;
    private ActivityResultLauncher<Intent> takePhotoNotSaveResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if(result.getResultCode() == RESULT_OK){
                                Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                                img1.setImageBitmap(bitmap);
                            }
                        }
                    });

    private ActivityResultLauncher<Intent> takePhotoSaveResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            String filepath = new File(saveDir + "/test.jpg").getAbsolutePath();
                            Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                            img2.setImageBitmap(bitmap);
                        }
                    });

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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoNotSaveResultLauncher.launch(intent);
    }

    public void takePhotoSaveToDevice(View view){
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", new File(saveDir + "/test.jpg"));
        Log.d(tag, "----uri " + uri +"----");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        takePhotoSaveResultLauncher.launch(intent);

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
        saveDir = Environment.getExternalStoragePublicDirectory("HiskioPhoto");
    }
}