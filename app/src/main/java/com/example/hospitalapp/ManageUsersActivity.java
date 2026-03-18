package com.example.hospitalapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {
    private ListView lvUsers;
    private AppDatabase db;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        db = AppDatabase.getDatabase(this);
        lvUsers = findViewById(R.id.lvUsers);

        loadUsers();

        lvUsers.setOnItemLongClickListener((parent, view, position, id) -> {
            User userToDelete = userList.get(position);
            if ("admin".equals(userToDelete.role)) {
                Toast.makeText(this, "Cannot delete admin account", Toast.LENGTH_SHORT).show();
                return true;
            }
            new Thread(() -> {
                db.hospitalDao().deleteUser(userToDelete);
                runOnUiThread(() -> {
                    Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
                    loadUsers();
                });
            }).start();
            return true;
        });
    }

    private void loadUsers() {
        new Thread(() -> {
            userList = db.hospitalDao().getAllUsers();
            List<String> displayList = new ArrayList<>();
            for (User u : userList) {
                displayList.add(u.username + " (" + u.role + ") - " + u.email);
            }
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
                lvUsers.setAdapter(adapter);
            });
        }).start();
    }
}