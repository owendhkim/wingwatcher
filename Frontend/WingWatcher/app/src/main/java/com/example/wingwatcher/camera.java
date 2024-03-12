package com.example.wingwatcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.Manifest;

public class camera extends AppCompatActivity {

    ImageView imageview;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageview = findViewById(R.id.imageview);
        button = findViewById(R.id.button);

        // Request for camera runtime permission
        if (ContextCompat.checkSelfPermission(camera.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(camera.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bitmap capturedBitmap = (Bitmap) data.getExtras().get("data");

            // Pass the captured image to the photoUpload activity
            Intent photoUploadIntent = new Intent(this, photoUpload.class);
            photoUploadIntent.putExtra("capturedBitmap", capturedBitmap);
            startActivity(photoUploadIntent);
        }
    }
}
