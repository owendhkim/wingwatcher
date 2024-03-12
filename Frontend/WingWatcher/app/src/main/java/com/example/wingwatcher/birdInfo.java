package com.example.wingwatcher;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class birdInfo extends AppCompatActivity {

    private ImageView imageView;
    private ImageView rangeMap;
    private TextView birdName;
    private TextView birdInfo;
    private Button researcher;
    private EditText editableBirdInfo;
    private Button backButton;
    private Button birdCall;

    private Button modBird;
    protected int birdID;
    private String soundURL;
    private JSONObject birdData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_info);

        imageView = findViewById(R.id.imageView);
        rangeMap = findViewById(R.id.rangeMap);
        birdName = findViewById(R.id.birdName);
        birdInfo = findViewById(R.id.birdInfo);
        researcher = findViewById(R.id.researcher);
        editableBirdInfo = findViewById(R.id.editableBirdInfo);
        birdCall = findViewById(R.id.birdCall);
        modBird = findViewById(R.id.modBird);
        backButton = findViewById(R.id.backButton);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://coms-309-052.class.las.iastate.edu:8080/birdInfo/";
        Intent intent = getIntent();
        birdInfo.this.birdID = intent.getIntExtra("birdID", 30); // default birdID = 30 (Bald Eagle)
        url += birdID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VolleyResponse", "Received");
                        try {
                            birdInfo.this.birdData = response;
                            String imageUrl = response.getString("image");
                            String name = response.getString("name");
                            String info = response.getString("shortDesc");
                            // Ensure the string is not shorter than 100 characters to avoid StringIndexOutOfBoundsException
                            if (info.length() > 100) {
                                info = info.substring(0, 150);
                            }
                            birdInfo.setText(info);
                            String rangeURL = response.getString("rangeMap");
                            birdInfo.this.soundURL = response.getString("callSound");
                            birdName.setText(name);
                            birdInfo.setText(info);

                            ImageRequest imageRequest = new ImageRequest(imageUrl,
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap response) {
                                            imageView.setImageBitmap(response);
                                        }
                                    }, 0, 0, ImageView.ScaleType.CENTER, null, null);

                            requestQueue.add(imageRequest);

                            ImageRequest rangeMapRequest = new ImageRequest(rangeURL,
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap response) {
                                            rangeMap.setImageBitmap(response);
                                        }
                                    }, 0, 0, ImageView.ScaleType.CENTER, null, null);

                            requestQueue.add(rangeMapRequest);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);

        backButton.setOnClickListener(v -> {
            Intent dashboardIntent = new Intent(this, dashboard.class);
            startActivity(dashboardIntent);
            finish();
        });


        modBird.setOnClickListener(v -> {
            String updatedInfo = editableBirdInfo.getText().toString();

            try {
                // Update the short description in the stored JSON object
                birdData.put("shortDesc", updatedInfo);

                // Here, birdData now contains the entire object with the updated short description
                String putUrl = "http://coms-309-052.class.las.iastate.edu:8080/birdInfo/" + birdID;

                // Send the entire updated JSON object
                requestQueue.add(new JsonObjectRequest(Request.Method.PUT, putUrl, birdData,
                                         response -> Log.d("PutResponse", "Response is: " + response),
                                         error -> Log.e("PutError", "That didn't work!"))
                                 {
                                     // Optionally, override getHeaders() if required
                                 }
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        birdCall.setOnClickListener(v -> {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e("MediaPlayer Error", "What: " + what + " Extra: " + extra);
                    mp.release(); // Release the MediaPlayer resources
                    return true; // Indicates that the error was handled
                }
            });

            try {
                Log.d("URL", soundURL);
                mediaPlayer.setDataSource(soundURL);
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(mp -> {
                    mp.start();

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        if (mp.isPlaying()) {
                            mp.stop();
                        }
                        mp.release();
                    }, 5000);
                });
            } catch (IOException e) {
                e.printStackTrace();
                mediaPlayer.release();
            }
        });

        researcher.setOnClickListener(v -> {
            if(userInfo.getPrivilege() == 0){
                String temp = birdInfo.getText().toString();
                birdInfo.setText("Incorrect Permission - This will reset in 3 seconds");

                // Delay for 3 seconds and then reset the text
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        birdInfo.setText(temp); // Reset the text to its original value
                    }
                }, 3000); // 3000 milliseconds = 3 seconds
            }else{
                if(birdInfo.getVisibility() == View.VISIBLE) {
                    birdInfo.setVisibility(View.GONE);
                    editableBirdInfo.setText(birdInfo.getText().toString());
                    editableBirdInfo.setVisibility(View.VISIBLE);
                } else {
                    birdInfo.setVisibility(View.VISIBLE);
                    editableBirdInfo.setVisibility(View.GONE);
                }
            }
        });
    }
}