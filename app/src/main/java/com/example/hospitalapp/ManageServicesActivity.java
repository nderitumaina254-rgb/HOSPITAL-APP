package com.example.hospitalapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ManageServicesActivity extends AppCompatActivity {
    private EditText etCode, etName;
    private ListView lvServices;
    private AppDatabase db;
    private List<Service> serviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_services);

        db = AppDatabase.getDatabase(this);
        etCode = findViewById(R.id.etServiceCode);
        etName = findViewById(R.id.etServiceName);
        Button btnAdd = findViewById(R.id.btnAddService);
        lvServices = findViewById(R.id.lvServices);

        loadServices();

        btnAdd.setOnClickListener(v -> {
            String code = etCode.getText().toString().trim();
            String name = etName.getText().toString().trim();

            if (code.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                db.hospitalDao().insertService(new Service(code, name));
                runOnUiThread(() -> {
                    Toast.makeText(this, "Service added", Toast.LENGTH_SHORT).show();
                    etCode.setText("");
                    etName.setText("");
                    loadServices();
                });
            }).start();
        });

        lvServices.setOnItemLongClickListener((parent, view, position, id) -> {
            Service serviceToDelete = serviceList.get(position);
            new Thread(() -> {
                db.hospitalDao().deleteService(serviceToDelete);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Service deleted", Toast.LENGTH_SHORT).show();
                    loadServices();
                });
            }).start();
            return true;
        });
    }

    private void loadServices() {
        new Thread(() -> {
            serviceList = db.hospitalDao().getAllServices();
            List<String> displayList = new ArrayList<>();
            for (Service s : serviceList) {
                displayList.add(s.serviceCode + " - " + s.serviceName);
            }
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
                lvServices.setAdapter(adapter);
            });
        }).start();
    }
}