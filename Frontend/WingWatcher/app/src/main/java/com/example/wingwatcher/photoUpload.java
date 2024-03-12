package com.example.wingwatcher;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class photoUpload extends AppCompatActivity {

    static final int PICK_IMAGE = 1;
    private ImageView imageView;
    private Button uploadButton;
    private WebSocketClient webSocketClient;
    protected int birdID;
    private Bitmap selectedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);

        imageView = findViewById(R.id.imageView);
        uploadButton = findViewById(R.id.uploadButton);
        birdID = 12;
        setupWebSocketClient();

//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(galleryIntent, PICK_IMAGE);

        if (getIntent().hasExtra("capturedBitmap")) {
            selectedBitmap = getIntent().getParcelableExtra("capturedBitmap");
            imageView.setImageBitmap(selectedBitmap);
        } else {
            // If no bitmap, allow user to pick image from gallery
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_IMAGE);
        }

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedBitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    if (webSocketClient != null && webSocketClient.isOpen()) { // Check if the WebSocket is open
                        ByteBuffer buffer = ByteBuffer.wrap(byteArray);

                        // Enhanced log statement with image size information
                        Log.d("WebSocket", "Attempting to send image data of size: " + byteArray.length + " bytes.");

                        webSocketClient.send(buffer); // Send the ByteBuffer if the connection is open
                    } else {
                        Toast.makeText(photoUpload.this, "WebSocket not connected", Toast.LENGTH_SHORT).show();
                        Log.d("WebSocket", "WebSocket not connected"); // Additional log for connection status
                    }
                } else {
                    Toast.makeText(photoUpload.this, "Bitmap is null", Toast.LENGTH_SHORT).show();
                    Log.d("WebSocket", "Bitmap is null"); // Additional log for bitmap status
                }
            }
        });

    }

    private void setupWebSocketClient() {
        URI uri;
        try {
            uri = new URI("ws://coms-309-052.class.las.iastate.edu:8080/classification/user321");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d("Connection", "Received");
            }

            @Override
            public void onMessage(String message) {
                Log.d("WebSocket", "Received from server: " + message);;
                Log.d("message", "BIRB ID");
                // Setting the ID from the received message
                try {
                    birdID = Integer.parseInt(message); // Parsing the integer
                } catch (NumberFormatException e) {
                    Log.d("FAIL", "Received from server: " + message);
                }

                webSocketClient.close();
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("WebSocket", "Connection closed with exit code " + code + " additional info: " + reason);
                Intent intent = new Intent(photoUpload.this, birdInfo.class);
                intent.putExtra("birdID", birdID);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };
        webSocketClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri selectedImage = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                selectedBitmap = BitmapFactory.decodeStream(imageStream); // Assign to member variable
                imageView.setImageBitmap(selectedBitmap);
                imageView.setVisibility(View.VISIBLE);
                uploadButton.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.close();
        }
    }
}
