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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class signUpPage extends AppCompatActivity {

    EditText mFullName, mEmail, mPassword, mConfirmPassword;
    Button mSignUpBtn;
    ProgressBar progressBar;
    RequestQueue requestQueue;
    ImageButton mPassHider1, mPassHider2;
    boolean isPasswordVisible1 = false;
    boolean isPasswordVisible2 = false;
    int privilege;

    private static final String REGISTER_URL = "http://coms-309-052.class.las.iastate.edu:8080/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        Spinner userTypeSpinner = findViewById(R.id.dropDown);
        String[] userTypes = {"Privileges","Viewer", "Researcher", "Admin"};
        ArrayAdapter<String> userTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userTypes);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(userTypeAdapter);

        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedUserType = userTypes[position];
                if ("Viewer".equals(selectedUserType)) {
                    privilege = 0;
                } else if ("Researcher".equals(selectedUserType)) {
                   privilege = 1;
                } else if ("Admin".equals(selectedUserType)) {
                    privilege = 2;
                }else{
                    privilege =0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirmPassword);
        mSignUpBtn = findViewById(R.id.signUpBtn);
        progressBar = findViewById(R.id.progressBar);
        requestQueue = Volley.newRequestQueue(this);
        mPassHider1 = findViewById(R.id.passHider1);
        mPassHider2 = findViewById(R.id.passHider2);

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullName = mFullName.getText().toString().trim();
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirmPassword = mConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Please enter a valid email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password must be at least 6 characters long.");
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    mConfirmPassword.setError("Passwords do not match");
                    return;
                }
                registerUser(fullName, email, password);
            }
        });

        mPassHider1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle password visibility for the first password field
                togglePasswordVisibility(mPassword, isPasswordVisible1);
                isPasswordVisible1 = !isPasswordVisible1; // Toggle the flag
            }
        });

        mPassHider2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle password visibility for the second password field
                togglePasswordVisibility(mConfirmPassword, isPasswordVisible2);
                isPasswordVisible2 = !isPasswordVisible2; // Toggle the flag
            }
        });
    }

    private void registerUser(final String fullName, final String email, final String password) {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", fullName);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("privilege",privilege);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                REGISTER_URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // On success registration
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(signUpPage.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), loginPage.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // On failure registration
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(signUpPage.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), loginPage.class));
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
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

