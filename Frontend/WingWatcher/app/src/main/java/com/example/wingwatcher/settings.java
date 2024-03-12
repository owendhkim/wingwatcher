package com.example.wingwatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class settings extends AppCompatActivity {

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        requestQueue = Volley.newRequestQueue(this);
        TextView helpOption = findViewById(R.id.helpOption);
        TextView changePasswordOption = findViewById(R.id.changePasswordOption);
        TextView logout = findViewById(R.id.logoutOption);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);

        helpOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelpDialog();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(settings.this, loginPage.class));
            }
        });
        changePasswordOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePasswordDialog();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation item clicks
                switch (item.getItemId()) {
                    case R.id.action_home:
                        // Navigate to the Dashboard activity
                        startActivity(new Intent(settings.this, dashboard.class));
                        finish(); // Finish the current activity
                        return true;
                    case R.id.action_profile:
                        // Navigate to the Profile activity
                        startActivity(new Intent(settings.this, profilePage.class));
                        finish();
                        return true;
                    case R.id.action_settings:
                        // Nothing to do
                        return true;
                }
                return false;
            }
        });
    }

    private void showHelpDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.activity_help_dialog, null);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText editText = dialogView.findViewById(R.id.editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help");
        builder.setMessage("Please provide details about your needs:");

        builder.setView(dialogView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Retrieve the text from the EditText
                String userText = editText.getText().toString();
                Log.d("UserInput", "User entered: " + userText);

                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showChangePasswordDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.activity_change_password_dialog, null);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText oldPasswordEditText = dialogView.findViewById(R.id.oldPasswordEditText);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText newPasswordEditText = dialogView.findViewById(R.id.newPasswordEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Password");
        builder.setMessage("Please enter your old and new passwords:");

        builder.setView(dialogView);

        builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Retrieve the text from the EditText fields
                String oldPassword = oldPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();

                changePassword(userInfo.getEmail(),newPassword);

                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void changePassword(final String emailToSearch,String newPassword) {
        String url = "http://coms-309-052.class.las.iastate.edu:8080/users/";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject userObject = getUserObjectByEmail(response, emailToSearch);
                        if (userObject != null) {
                            int userId = 0;
                            try {
                                userId = userObject.getInt("id");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String modifiedUrl = url + userId;
                            updatePassword(modifiedUrl, userObject, newPassword);
                        } else {
                            // Handle the case where no matching email was found
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
            }
        });

        requestQueue.add(jsonArrayRequest);
    }
    public JSONObject getUserObjectByEmail(JSONArray jsonArray, String emailToSearch) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("email").equals(emailToSearch)) {
                    return jsonObject;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Return null if no matching email found.
    }
    private void updatePassword(String url, JSONObject originalUserObject, String newPassword) {
        try {
            originalUserObject.put("password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = originalUserObject.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response, maybe show a success message to the user
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error, maybe notify the user about the error
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

}
