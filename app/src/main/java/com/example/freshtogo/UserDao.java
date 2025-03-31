package com.example.freshtogo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    // Check if user exists by username (used in registration)
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    // Check if user exists with matching username and password (used in login)
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User getUserByCredentials(String username, String password);

    // Insert new user
    @Insert
    long insertUser(User user);
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    User getUserById(int userId);

}
