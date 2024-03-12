package com.example.wingwatcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * This Class is for the Analytics Page.
 * It pings our server for connectivity and gathers data to fill out a data table.
 * The data taken from the server is collected via the Volley API
 * and is parsed in our private methods.
 *
 * Additionally, there is a refresh button and a link to our user management page
 * @author Andrew de Gala
 */
public class birdAnalytics extends AppCompatActivity {
    RequestQueue requestQueue;
    int numberOfUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_analytics);
        requestQueue = Volley.newRequestQueue(this);
        fetchNumberOfObjects();
        //Data to fill in table fields
        TextView numUsers;


        //Basic Refresh Button
        Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            /**
             * Button for refreshing the page of Analytics
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        /*
        Transition to UserManage Activity
         */
        Button userManagement = (Button) findViewById(R.id.userManagement);
        userManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(birdAnalytics.this,
                        userManage.class));
            }
        });
    }

    private void fetchNumberOfObjects() {
        String url = "http://coms-309-052.class.las.iastate.edu:8080/users";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());

                        // Set to keep track of unique user ids
                        Set<Integer> uniqueUserIds = new HashSet<>();

                        // Traverse through each user object
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject user = response.getJSONObject(i);

                                // Collect the user id
                                int userId = user.getInt("id");
                                uniqueUserIds.add(userId);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Now, uniqueUserIds.size() contains the count of unique user ids
                        numberOfUsers(uniqueUserIds.size());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error appropriately
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void numberOfUsers(int numberOfUsers) {
        TextView numUsers = findViewById(R.id.numUsers);
        numUsers.setText(String.valueOf(numberOfUsers));
    }
}
