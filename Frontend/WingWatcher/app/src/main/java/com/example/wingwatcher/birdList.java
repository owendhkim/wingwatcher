package com.example.wingwatcher;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class birdList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BirdAdapter adapter;
    private List<Bird> birdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        birdList = new ArrayList<>();
        adapter = new BirdAdapter(birdList,this);
        recyclerView.setAdapter(adapter);

        fetchDataFromUrl();

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(birdList.this, dashboard.class);
                startActivity(intent);
            }
        });

        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase().trim();
                filterBirdList(query);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void fetchDataFromUrl() {
        String apiUrl = "http://coms-309-052.class.las.iastate.edu:8080/birdInfo";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        boolean isDataFetchedCorrectly = false;
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject birdObject = response.getJSONObject(i);
                                int id = birdObject.getInt("id");
                                String name = birdObject.getString("name");
                                String imageUrl = birdObject.getString("image");

                                Bird bird = new Bird(id, name, imageUrl);
                                birdList.add(bird);
                            }
                            isDataFetchedCorrectly = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (isDataFetchedCorrectly) {
                            adapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void filterBirdList(String query) {
        List<Bird> filteredList = new ArrayList<>();
        for (Bird bird : birdList) {
            if (bird.getName().toLowerCase().contains(query)) {
                filteredList.add(bird);
            }
        }
        adapter.filterList(filteredList);
    }
}

