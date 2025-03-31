package com.example.freshtogo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                User.class,
                CartItem.class,
                SavedCard.class,
                OrderItem.class
        },
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract CartDao cartDao();
    public abstract SavedCardDao savedCardDao();
    public abstract OrderDao orderDao();
}

