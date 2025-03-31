package com.example.freshtogo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insertOrder(OrderItem item);

    @Query("SELECT * FROM order_items WHERE userId = :userId")
    List<OrderItem> getOrdersForUser(int userId);
}
