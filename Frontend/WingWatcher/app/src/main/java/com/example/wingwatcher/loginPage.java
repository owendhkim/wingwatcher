package com.example.wingwatcher;
/*
  @author Siddhartha G
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class loginPage extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mLoginBtn;
    ProgressBar progressBar;
    TextView mSignUpPage;
    RequestQueue requestQueue;
    ImageButton mPassHider;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        mLoginBtn = findViewById(R.id.loginBtn);
        mSignUpPage = findViewById(R.id.signUpTxt);
        requestQueue = Volley.newRequestQueue(this);
        mPassHider = findViewById(R.id.passHider);


        // Set a click listener to toggle password visibility
        mPassHider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle password visibility for the password field
                togglePasswordVisibility(mPassword, isPasswordVisible);
                isPasswordVisible = !isPasswordVisible; // Toggle the flag
            }
        });

        mSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), signUpPage.class));
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Please enter a valid email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password must be greater than 6 characters");
                    return;
                }
                authenticateUser(email, password);
            }
        });
    }

    private void authenticateUser(final String enteredEmail, final String enteredPassword) {
        progressBar.setVisibility(View.VISIBLE);
        String url = "http://coms-309-052.class.las.iastate.edu:8080/users";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());
                        boolean isUserAuthenticated = false;
                        try {
                            // Going through the JSON array and getting user information
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String email = jsonObject.getString("email");
                                String storedHashedPassword = jsonObject.getString("password");

                                if (enteredEmail.equals(email) && verifyPassword(enteredPassword, storedHashedPassword)) {
                                    isUserAuthenticated = true;

                                    // Extract user information
                                    String username = jsonObject.getString("username");
                                    int privilege = jsonObject.getInt("privilege");
                                    JSONArray birdTrackingInfo = jsonObject.getJSONArray("birdTrackingInfo");
                                    JSONObject analytics = jsonObject.optJSONObject("analytics");

                                    // Storing the user information for future use
                                    userInfo.setUserDetails(getApplicationContext(), email, username, privilege, birdTrackingInfo, analytics);
                                    break;
                                }
                            }

                            if (isUserAuthenticated) {
                                startActivity(new Intent(getApplicationContext(), dashboard.class));
                            } else {
                                // If the user is not authenticated, shows an error
                                mEmail.setError("Invalid email or password");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility((View.GONE));
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private boolean verifyPassword(String enteredPassword, String storedHashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(enteredPassword.toCharArray(), storedHashedPassword);
        return result.verified;
    }

    private void togglePasswordVisibility(EditText passwordField, boolean isPasswordVisible) {
        if (isPasswordVisible) {
            passwordField.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        passwordField.setSelection(passwordField.getText().length());
    }
}
