package com.example.wingwatcher;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.location.Location;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.content.Intent;
import android.Manifest;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class mapPage extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private WebSocketClient webSocket;
    private Timer dataf;
    private TimerTask datafTask;

    // Backend URL
    private static final String BASE_URL = "http://coms-309-052.class.las.iastate.edu:8080";

    // JSON media type
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_page);
        dataf = new Timer();
        datafTask = new TimerTask() {
            @Override
            public void run() {
                fetchData();
            }
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        dataf.schedule(datafTask, 0, 1000);

        initWebSocketConnection();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (dataf != null) {
            dataf.cancel();
        }
    }

    private void initWebSocketConnection() {
        String serverUrl = "ws://coms-309-052.class.las.iastate.edu:8080/map/";
        URI serverUri = URI.create(serverUrl);

        webSocket = new WebSocketClient(serverUri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d("WebSocket", "Connected");
            }

            @Override
            public void onMessage(String message) {
                Log.d("WebSocket", "Received message: " + message);
                processWebSocketMessage(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("WebSocket", "Closed");
            }

            @Override
            public void onError(Exception ex) {
                Log.d("WebSocket", "Error");
                ex.printStackTrace();
            }
        };

        webSocket.connect();
    }

    private void processWebSocketMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            double latitude = jsonObject.optDouble("latitude");
            double longitude = jsonObject.optDouble("longitude");
            String birdName = jsonObject.optString("birdName");

            if (latitude != 0.0 && longitude != 0.0 && !birdName.isEmpty()) {
                updateMapWithMarker(birdName, latitude, longitude);
            }
        } catch (JSONException e) {
            Log.e("WebSocket", "Error parsing JSON: " + e.getMessage());
        }
    }

    private void fetchData() {
        new FetchDataTask().execute();
    }

    private class FetchDataTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(BASE_URL + "/birdtrackinginfos")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            if (jsonData != null) {
                try {
                    JSONArray birdDataArray = new JSONArray(jsonData);
                    for (int i = 0; i < birdDataArray.length(); i++) {
                        JSONObject birdData = birdDataArray.getJSONObject(i);
                        double latitude = birdData.optDouble("latitude");
                        double longitude = birdData.optDouble("longitude");
                        String birdName = birdData.optString("birdName");

                        if (latitude != 0.0 && longitude != 0.0) {
                            updateMapWithMarker(birdName, latitude, longitude);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateMapWithMarker(String birdName, double latitude, double longitude) {
        if (myMap != null) {
            LatLng birdLocation = new LatLng(latitude, longitude);
            Marker marker = myMap.addMarker(new MarkerOptions().position(birdLocation).title(birdName));

            myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker clickedMarker) {
                    if (clickedMarker.getId().equals(marker.getId())) {
                        showBirdInfo(marker);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
                mapFragment.getMapAsync(mapPage.this);
            }
        });
    }

    public void onBackButtonClick(View view) {
        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        myMap.setOnMapClickListener(this);

        if (currentLocation != null) {
            LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            myMap.addMarker(new MarkerOptions().position(current).title("My Location"));
            fetchData();
        }

        myMap.setOnMarkerClickListener(marker -> {
            showBirdInfo(marker);
            return true;
        });

        myMap.setOnInfoWindowClickListener(marker -> {
            Intent intent = new Intent(mapPage.this, birdList.class);
            startActivity(intent);
        });
    }

    private void showBirdInfo(Marker marker) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_bird_name, null);
        dialogBuilder.setView(dialogView);

        EditText birdNameInput = dialogView.findViewById(R.id.birdNameInput);

        if (marker != null) {
            birdNameInput.setText(marker.getTitle());
        }

        AlertDialog dialog = dialogBuilder.create();

        // Set the dialog's position based on the marker's position
        if (marker != null && myMap != null) {
            LatLng markerPosition = marker.getPosition();
            Point markerPoint = myMap.getProjection().toScreenLocation(markerPosition);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            layoutParams.x = markerPoint.x;
            layoutParams.y = markerPoint.y;
            dialog.getWindow().setAttributes(layoutParams);
        }

        dialog.show();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        showBirdNameInputDialog(latLng);
    }

    private void showBirdNameInputDialog(final LatLng latLng) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_bird_name, null);
        dialogBuilder.setView(dialogView);

        final EditText birdNameInput = dialogView.findViewById(R.id.birdNameInput);

        dialogBuilder.setPositiveButton("Submit", (dialog, which) -> {
            String birdName = birdNameInput.getText().toString();

            if (!birdName.isEmpty()) {
                updateMapMarker(birdName, latLng.latitude, latLng.longitude);
                sendBirdTrackingInfoToBackend(birdName, latLng.latitude, latLng.longitude);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter the bird name.", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void updateMapMarker(String birdName, double latitude, double longitude) {
        if (myMap != null) {
            LatLng birdLocation = new LatLng(latitude, longitude);
            Marker marker = myMap.addMarker(new MarkerOptions().position(birdLocation).title(birdName));

            myMap.setOnMarkerClickListener(clickedMarker -> {
                if (clickedMarker.getId().equals(marker.getId())) {
                    showBirdInfo(clickedMarker);
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendBirdTrackingInfoToBackend(String birdName, double latitude, double longitude) {

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = dateFormat.format(currentDate);
        String time = timeFormat.format(currentDate);

        // Prepare the JSON data to send to the backend, including the user's name
        try {
            JSONObject birdData = new JSONObject();
            birdData.put("latitude", latitude);
            birdData.put("longitude", longitude);
            birdData.put("date", date);
            birdData.put("time", time);
            birdData.put("birdName", birdName);

            new SendBirdTrackingInfoTask().execute(birdData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SendBirdTrackingInfoTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String json = params[0];

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(json, JSON);

            Request request = new Request.Builder()
                    .url(BASE_URL + "/birdtrackinginfos")
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    // The data was successfully sent to the backend
                    Log.d("SndBirdTrackingInfoTask", "Data sent to backend");
                } else {
                    // There was an error sending the data
                    Log.e("SndBirdTrackingInfoTask", "Error sending data to backend");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
