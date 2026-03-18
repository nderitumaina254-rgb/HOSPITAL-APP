package com.example.hospitalapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private RadioGroup rgLoginRole;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = AppDatabase.getDatabase(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        rgLoginRole = findViewById(R.id.rgLoginRole);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        // Seed some admin data if not exists
        seedAdmin();

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedRoleId = rgLoginRole.getCheckedRadioButtonId();
            String selectedRole = (selectedRoleId == R.id.rbLoginAdmin) ? "admin" : "user";

            new Thread(() -> {
                User user = db.hospitalDao().login(username, password);
                runOnUiThread(() -> {
                    if (user != null) {
                        if (!user.role.equals(selectedRole)) {
                            Toast.makeText(this, "Incorrect role selected for this user", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent;
                        if ("admin".equals(user.role)) {
                            intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                        } else {
                            intent = new Intent(LoginActivity.this, UserMainActivity.class);
                        }
                        intent.putExtra("USERNAME", user.username);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void seedAdmin() {
        new Thread(() -> {
            if (db.hospitalDao().getUserByName("admin") == null) {
                db.hospitalDao().insertUser(new User("admin", "admin@hospital.com", "admin123", "admin"));
            }
            // Seed initial services
            if (db.hospitalDao().getAllServices().isEmpty()) {
                db.hospitalDao().insertService(new Service("CL001", "Cleaning"));
                db.hospitalDao().insertService(new Service("EP002", "Equipment assistance"));
                db.hospitalDao().insertService(new Service("LC001", "Linen change"));
                db.hospitalDao().insertService(new Service("PS001", "Porter services"));
            }
        }).start();
    }
}