package com.example.freshtogo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CartDao {

    @Insert
    void insertItem(CartItem item);

    @Query("SELECT * FROM cart WHERE userId = :userId AND name = :productName LIMIT 1")
    CartItem getItemByName(int userId, String productName);

    @Query("UPDATE cart SET quantity = quantity + 1 WHERE id = :id")
    void incrementQuantity(int id);

    @Query("SELECT * FROM cart WHERE userId = :userId")
    List<CartItem> getCartItems(int userId);

    @Query("DELETE FROM cart WHERE userId = :userId")
    void clearCart(int userId);
    @Query("DELETE FROM cart WHERE id = :id")
    void deleteById(int id);

}
