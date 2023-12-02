package com.example.mobileappdevcoursework.data;

//import androidx.room.Dao;

import java.util.List;
import androidx.room.*;

@Dao
public interface GameDAO {

    @Query("SELECT * FROM Game")
    List<Game> getAll();

    @Query("SELECT * FROM Game WHERE gameID IN (:gameIds)")
    List<Game> loadAllByIds(int[] gameIds);

    @Query("Select * from Game where gameID like :id")
    Game findGameById(int id);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGames(List<Game> games);



    @Query("Delete from Game")
    void deleteAll();
}