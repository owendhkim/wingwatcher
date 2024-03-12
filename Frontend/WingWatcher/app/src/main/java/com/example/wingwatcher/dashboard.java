package com.example.wingwatcher;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class dashboard extends AppCompatActivity {

    private CardView userAnalyticsCard;
    private CardView photoUploadCard;
    private CardView cameraCard;
    private CardView birdListCard;
    private CardView mapPageCard;
    private CardView quoteCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        String username = userInfo.getUsername();
        TextView usernameTextView = findViewById(R.id.username);
        usernameTextView.setText(username);

        ImageView profileImageView = findViewById(R.id.profileImage);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProfileActivity();
            }
        });

        userAnalyticsCard = findViewById(R.id.userAnalyticsIcon);
        photoUploadCard = findViewById(R.id.photoUploadIcon);
        cameraCard = findViewById(R.id.cameraIcon);
        birdListCard = findViewById(R.id.birdListIcon);
        mapPageCard = findViewById(R.id.mapPageIcon);
        quoteCard = findViewById(R.id.quoteIcon);

        userAnalyticsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userInfo.getPrivilege()==0){
                    displayAccessErrorDialog();
                }else{
                startActivity(new Intent(dashboard.this, birdAnalytics.class));
                }
            }
        });

        photoUploadCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(dashboard.this, photoUpload.class));
            }
        });

        cameraCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(dashboard.this, camera.class));
            }
        });

        birdListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(dashboard.this, birdList.class));
            }
        });

        mapPageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(dashboard.this, mapPage.class));
            }
        });

        quoteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(dashboard.this, quoteGenerator.class));
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        //stay on the same page
                        break;
                    case R.id.action_profile:
                        //navigate to profile page
                        startActivity(new Intent(dashboard.this, profilePage.class));
                        break;
                    case R.id.action_settings:
                        //navigate to settings page
                        startActivity(new Intent(dashboard.this, settings.class));
                        break;
                }
                return true;
            }
        });
    }
    private void navigateToProfileActivity() {
        Intent intent = new Intent(this, profilePage.class);
        startActivity(intent);
    }
    private void displayAccessErrorDialog() {
        String errorMessage = "We are sorry, this page is only accessible by admins and researchers. If needed, please request access in the help portal. We appreciate your patience.";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning!")
                .setMessage(errorMessage)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
