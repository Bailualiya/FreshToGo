package com.example.freshtogo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_items")
public class OrderItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public String name;
    public String category;
    public double price;
    public int imageResId;
    public int quantity;
}
