package com.example.hospitalapp;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Service.class, ServiceRequest.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HospitalDao hospitalDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "hospital_database")
                            .allowMainThreadQueries() // For simplicity in this task
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}