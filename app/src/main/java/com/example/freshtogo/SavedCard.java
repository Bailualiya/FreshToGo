package com.example.freshtogo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_cards")
public class SavedCard {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;               // Link card to a specific user
    public String bankName;         // e.g. "RBC Bank"
    public String last4Digits;      // e.g. "xxxx1234"
    public String cardLabel;
}
