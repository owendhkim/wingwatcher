package com.example.wingwatcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.example.statsandmangement.databinding.ActivityUserManageBinding;

import java.io.UnsupportedEncodingException;

/**
 * This class is for user management (changing user passwords).
 * Communication is conducted with our server via the Volley API
 * Additionally there is a link back to our Analytics page.
 * @author Andrew de Gala
 */
public class userManage extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    String email;
    String newPassword;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);
        requestQueue = Volley.newRequestQueue(this);

        /*
        Transition to Main Activity
         */
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userManage.this,
                        birdAnalytics.class));
            }
        });
        /*
        Fetch Username/Password from server
         */
        Button displayChanges = (Button) findViewById(R.id.displayChanges);
        displayChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do later
            }
        });
        /*
        Changes Username/Password on server
         */
        Button submitChanges = (Button) findViewById(R.id.submitChanges);
        submitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText2 = (EditText) findViewById(R.id.editText2);
                newPassword = editText2.getText().toString().trim();
                changePassword(email);
            }
        });
        /*
        Locks in the current text box as the string
         */
        Button selectUser = (Button) findViewById(R.id.selectUser);
        selectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText1 = (EditText) findViewById(R.id.editText1);
                String userInput = editText1.getText().toString().trim();
                email = userInput;
            }
        });


    }
    public void changePassword(final String emailToSearch) {
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
