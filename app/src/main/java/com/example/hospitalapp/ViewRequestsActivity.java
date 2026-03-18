package com.example.hospitalapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ViewRequestsActivity extends AppCompatActivity {
    private ListView lvRequests;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        db = AppDatabase.getDatabase(this);
        lvRequests = findViewById(R.id.lvRequests);

        loadRequests();
    }

    private void loadRequests() {
        new Thread(() -> {
            List<ServiceRequest> requests = db.hospitalDao().getAllRequests();
            List<String> requestStrings = new ArrayList<>();
            for (ServiceRequest r : requests) {
                requestStrings.add("User: " + r.username + "\nService: " + r.serviceName + 
                        "\nWard/Bed: " + r.ward + "/" + r.bed + "\nNotes: " + r.notes + "\nStatus: " + r.status);
            }
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestStrings);
                lvRequests.setAdapter(adapter);
            });
        }).start();
    }
}