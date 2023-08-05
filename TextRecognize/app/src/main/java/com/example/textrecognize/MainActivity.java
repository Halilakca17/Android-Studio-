package com.example.textrecognize;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.Manifest;
import android.widget.PopupMenu;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MaterialButton inputImagebutton;
    private ShapeableImageView imageIv;
    private EditText recognizedTextEt;

    private  static final String TAG = "MAIN_TAG";
    private Uri imageUri = null;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private String[] cameraPermission;
    private String[] storagePermission;

    private ProgressDialog progressDialog;

    private TextRecognizer textRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputImagebutton = findViewById(R.id.inputImagebutton);
        MaterialButton recognizedTextbutton = findViewById(R.id.recognizedTextbutton);
        imageIv = findViewById(R.id.imageIv);
        recognizedTextEt = findViewById(R.id.recognizedTextEt);


        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Lütfen Bekleyin");
        progressDialog.setCanceledOnTouchOutside(false);

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);


        inputImagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputImageDıalog();
            }
        });

        recognizedTextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    Toast.makeText(MainActivity.this,"Görsel seçiniz",Toast.LENGTH_SHORT).show();

                }
                else {
                    recognizeTextFromImage();
                }
            }
        });

    }

    private void recognizeTextFromImage() {
        Log.d(TAG, "recognizeTextFromImage: ");
        progressDialog.setMessage("Görsel Hazırlanıyor...");
        progressDialog.show();

        try {
            InputImage inputImage = InputImage.fromFilePath(this,imageUri);
            progressDialog.setMessage("Metin algılanıyor...");
            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            progressDialog.dismiss();
                            String recognizedText = text.getText();
                            recognizedTextEt.setText(recognizedText);
                            Log.d(TAG, "onSuccess: recognizedText:" + recognizedText);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Log.e(TAG, "onFailure: ",e);
                            Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            progressDialog.dismiss();
            Log.e(TAG, "recognizeTextFromImage: ",e);
            Toast.makeText(this,"Görsel hazırlanırken bir hata oluştu: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void showInputImageDıalog() {
        PopupMenu popupMenu = new PopupMenu(this,inputImagebutton);
        popupMenu.getMenu().add(Menu.NONE,1,1,"CAMERA");
        popupMenu.getMenu().add(Menu.NONE,2,2,"GALLERY");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == 1) {
                    Log.d(TAG, "onMenuItemClick: camera clicked...");
                    if (checkCameraPermissions()) {
                        pickImageCamera();
                    }else {
                        requestCameraPermission();
                    }
                }
                else if (id == 2) {
                    Log.d(TAG, "onMenuItemClick: gallery Clicked...");
                    if (checkStoragePermission()) {
                        pickImageData();
                    }
                    else {
                        requestStoragePermissions();
                    }
                }
                return true;
            }
        });
    }

    private void pickImageData(){
        Log.d(TAG, "pickImageData: pickImage");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }


    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        imageUri = data.getData();
                        imageIv.setImageURI(imageUri);
                        Log.d(TAG, "onActivityResult: imageUri: " + imageUri);
                    }else {
                        Log.d(TAG, "onActivityResult: ");
                        Toast.makeText(MainActivity.this,"iptal edildi...",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void pickImageCamera() {
        Log.d(TAG, "pickImageCamera: ");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        // Bu satırı eklemeyi unutmayın!
        cameraActivityResultLauncher.launch(intent);


    }

    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "onActivityResult: imageUri " + imageUri);
                        imageIv.setImageURI(imageUri);
                    }else {
                        Log.d(TAG, "onActivityResult: cancelled");
                        Toast.makeText(MainActivity.this,"iptal edildi.",Toast.LENGTH_SHORT).show();
                    }
                }
            }

    );

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }
    private void requestStoragePermissions() {
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);

    }

    private boolean checkCameraPermissions() {
        boolean cameraResult = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storageResult = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return cameraResult && storageResult;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted) {
                        pickImageCamera();
                    }
                    else{
                        Toast.makeText(this,"Kamera ve Depolama izinleri gerekli.",Toast.LENGTH_SHORT).show();

                    }


                }
                else {
                    Toast.makeText(this,"iptal edildi",Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length> 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted) {
                        pickImageData();
                    }
                    else {
                        Toast.makeText(this,"Depolama izni gerekli",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }
}