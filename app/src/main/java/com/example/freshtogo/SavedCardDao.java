package com.example.freshtogo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SavedCardDao {

    @Insert


    void insertCard(SavedCard card);



    @Query("SELECT * FROM saved_cards WHERE userId = :userId")
    List<SavedCard> getCardsByUser(int userId);
}
