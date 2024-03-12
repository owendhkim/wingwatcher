package com.example.wingwatcher;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String BASE_URL = "ws://coms-309-052.class.las.iastate.edu:8080/map/";

    private WebSocketClient webSocket;
    private ArrayList<BirdTrackingInfo> trackingInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trackingInfoList = new ArrayList<>();

        URI serverUri = URI.create(BASE_URL);
        webSocket = new WebSocketClient(serverUri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d("WebSocket", "Connected");
            }

            @Override
            public void onMessage(String message) {
                Log.d("WebSocket", "Received message: " + message);
                processRealTimeInsight(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("WebSocket", "Closed");
            }

            @Override
            public void onError(Exception ex) {
                Log.d("WebSocket", "Error");
            }
        };

        webSocket.connect();

        String username = "your_username";

        // Get initial tracking info from the backend
        trackingInfoList = getAllTrackingInfoFromBackend(username);
        updateTrackingInfoOnMap(trackingInfoList);
    }

    private ArrayList<BirdTrackingInfo> getAllTrackingInfoFromBackend(String username) {
        // Use the provided backend URL to fetch tracking info for the user
        // Implement the HTTP request to your backend to get the data
        // Parse the JSON response and return the tracking info as a list
        return new ArrayList<>(); // Return an empty list for the example
    }

   private void updateTrackingInfoOnMap(ArrayList<BirdTrackingInfo> trackingInfoList) {
//        if (myMap != null) {
//            for (BirdTrackingInfo trackingInfo : trackingInfoList) {
//                double latitude = trackingInfo.getLatitude();
//                double longitude = trackingInfo.getLongitude();
//
//                if (latitude != 0.0 && longitude != 0.0) {
//                    LatLng birdLocation = new LatLng(latitude, longitude);
//                    MarkerOptions markerOptions = new MarkerOptions()
//                            .position(birdLocation)
//                            .title("Bird Name"); // Replace with the actual bird name
//                    myMap.addMarker(markerOptions);
//                }
//            }
//        }
   }


    private void processRealTimeInsight(String insight) {
        try {
            JSONObject jsonInsight = new JSONObject(insight);

            // Extract the latitude, longitude, and other data from the JSON message
            double latitude = jsonInsight.getDouble("latitude");
            double longitude = jsonInsight.getDouble("longitude");

            BirdTrackingInfo trackingInfo = new BirdTrackingInfo(latitude, longitude, null, null, null);

            // Update the map with the real-time tracking information

        } catch (JSONException e) {
            Log.e("WebSocket", "Error parsing JSON: " + e.getMessage());
        }
    }
}
