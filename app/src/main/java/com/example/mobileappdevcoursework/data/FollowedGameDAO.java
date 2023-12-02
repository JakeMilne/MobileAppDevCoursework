package com.example.mobileappdevcoursework.data;

import androidx.room.*;

import com.example.mobileappdevcoursework.data.FollowedGame;

import java.util.List;

@Dao
public interface FollowedGameDAO {

    @Query("SELECT * FROM FollowedGame")
    List<FollowedGame> getAllFollowed();

    @Query("SELECT event_count FROM FOLLOWEDGAME WHERE gameID = :id")
    int getEventCount(int id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateFollowedGames(FollowedGame game);

    @Query("Delete from FollowedGame Where gameID = :ID")
    void deleteFollowedGame(int ID);
}
