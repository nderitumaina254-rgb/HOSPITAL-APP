package com.example.hospitalapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class UserMainActivity extends AppCompatActivity {
    private Spinner spinnerServices;
    private EditText etNotes, etWard, etBed;
    private AppDatabase db;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        db = AppDatabase.getDatabase(this);
        username = getIntent().getStringExtra("USERNAME");

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome, " + username);

        spinnerServices = findViewById(R.id.spinnerServices);
        etNotes = findViewById(R.id.etNotes);
        etWard = findViewById(R.id.etWard);
        etBed = findViewById(R.id.etBed);
        Button btnSubmitRequest = findViewById(R.id.btnSubmitRequest);
        Button btnLogout = findViewById(R.id.btnLogout);

        loadServices();

        btnSubmitRequest.setOnClickListener(v -> {
            String serviceName = spinnerServices.getSelectedItem() != null ? spinnerServices.getSelectedItem().toString() : "";
            String notes = etNotes.getText().toString().trim();
            String ward = etWard.getText().toString().trim();
            String bed = etBed.getText().toString().trim();

            if (serviceName.isEmpty() || ward.isEmpty() || bed.isEmpty()) {
                Toast.makeText(this, "Please select a service and fill ward/bed info", Toast.LENGTH_SHORT).show();
                return;
            }

            ServiceRequest request = new ServiceRequest(serviceName, notes, ward, bed, "Pending", username);
            new Thread(() -> {
                db.hospitalDao().insertRequest(request);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Request submitted successfully", Toast.LENGTH_SHORT).show();
                    etNotes.setText("");
                    etWard.setText("");
                    etBed.setText("");
                });
            }).start();
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(UserMainActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void loadServices() {
        new Thread(() -> {
            List<Service> services = db.hospitalDao().getAllServices();
            List<String> serviceNames = new ArrayList<>();
            for (Service s : services) {
                serviceNames.add(s.serviceName);
            }
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, serviceNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerServices.setAdapter(adapter);
            });
        }).start();
    }
}