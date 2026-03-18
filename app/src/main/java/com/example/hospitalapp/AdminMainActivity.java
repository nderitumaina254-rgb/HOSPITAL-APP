package com.example.hospitalapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Button btnViewRequests = findViewById(R.id.btnViewRequests);
        Button btnManageServices = findViewById(R.id.btnManageServices);
        Button btnManageUsers = findViewById(R.id.btnManageUsers);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnViewRequests.setOnClickListener(v -> {
            startActivity(new Intent(AdminMainActivity.this, ViewRequestsActivity.class));
        });

        btnManageServices.setOnClickListener(v -> {
            startActivity(new Intent(AdminMainActivity.this, ManageServicesActivity.class));
        });

        btnManageUsers.setOnClickListener(v -> {
            startActivity(new Intent(AdminMainActivity.this, ManageUsersActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(AdminMainActivity.this, LoginActivity.class));
            finish();
        });
    }
}