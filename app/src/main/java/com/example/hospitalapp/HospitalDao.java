package com.example.hospitalapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HospitalDao {
    // User operations
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByName(String username);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Delete
    void deleteUser(User user);

    // Service operations
    @Insert
    void insertService(Service service);

    @Query("SELECT * FROM services")
    List<Service> getAllServices();

    @Delete
    void deleteService(Service service);

    // Service Request operations
    @Insert
    void insertRequest(ServiceRequest request);

    @Query("SELECT * FROM service_requests")
    List<ServiceRequest> getAllRequests();
    
    @Update
    void updateRequest(ServiceRequest request);
}