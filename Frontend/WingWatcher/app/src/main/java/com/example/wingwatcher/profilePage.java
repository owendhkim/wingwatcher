package com.example.wingwatcher;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class profilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        String username = userInfo.getUsername();
        String email = userInfo.getEmail();
        int privilege = userInfo.getPrivilege();

        TextView userNameTextView = findViewById(R.id.userNameTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView userPrivilegesTextView = findViewById(R.id.userPrivilegeTextView);
        userNameTextView.setText(username);
        emailTextView.setText(email);
        switch (privilege) {
            case 0:
                userPrivilegesTextView.setText("You are a valued Viewer.");
                break;
            case 1:
                userPrivilegesTextView.setText("You are an esteemed Researcher.");
                break;
            case 2:
                userPrivilegesTextView.setText("You are a distinguished Admin.");
                break;
            default:
                userPrivilegesTextView.setText("You have a special privilege.");
                break;
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        // Navigate to the dashboard
                        startActivity(new Intent(profilePage.this, dashboard.class));
                        return true;
                    case R.id.action_profile:
                        // Stay on the current profile page
                        return true;
                    case R.id.action_settings:
                        // Navigate to the main activity
                        startActivity(new Intent(profilePage.this, settings.class));
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}
