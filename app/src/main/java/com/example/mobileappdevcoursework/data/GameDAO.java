package com.example.mobileappdevcoursework.data;

//import androidx.room.Dao;

import java.util.List;
import androidx.room.*;

@Dao
public interface GameDAO {

    @Query("SELECT * FROM Game")
    List<Game> getAll();

    @Query("SELECT * FROM Game WHERE gameID IN (:gameIds)") //returns a list of Games from a list of Ids, wasn't used and so could be deleted
    List<Game> loadAllByIds(int[] gameIds);

    @Query("Select * from Game where gameID like :id")
    Game findGameById(int id);

    //add list of games to the table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGames(List<Game> games);


    //deletes all games, used when the api is called and the new batch of games is ready to be added
    @Query("Delete from Game")
    void deleteAll();
}