package com.example.hospitalapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "service_requests")
public class ServiceRequest {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String serviceName;
    public String notes;
    public String ward;
    public String bed;
    public String status; // e.g., "Pending", "Completed"
    public String username; // The user who made the request

    public ServiceRequest(String serviceName, String notes, String ward, String bed, String status, String username) {
        this.serviceName = serviceName;
        this.notes = notes;
        this.ward = ward;
        this.bed = bed;
        this.status = status;
        this.username = username;
    }
}