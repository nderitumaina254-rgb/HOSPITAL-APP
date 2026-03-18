package com.example.hospitalapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "services")
public class Service {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String serviceCode;
    public String serviceName;

    public Service(String serviceCode, String serviceName) {
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
    }
}