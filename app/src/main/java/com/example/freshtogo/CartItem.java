package com.example.freshtogo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart")
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public String name;
    public String category;
    public double price;
    public int imageResId;


    public int quantity = 1;
}
