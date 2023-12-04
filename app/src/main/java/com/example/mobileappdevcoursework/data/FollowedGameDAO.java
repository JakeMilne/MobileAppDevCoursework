package com.example.mobileappdevcoursework.data;

import androidx.room.*;

import com.example.mobileappdevcoursework.data.FollowedGame;

import java.util.List;

//DAO for FOllowedGame table
@Dao
public interface FollowedGameDAO {

    //return all from FollowedGame
    @Query("SELECT * FROM FollowedGame")
    List<FollowedGame> getAllFollowed();

    //return the event count for a specified game
    @Query("SELECT event_count FROM FOLLOWEDGAME WHERE gameID = :id")
    int getEventCount(int id);


    //replace a specified game, mostly used for updating the event count
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateFollowedGames(FollowedGame game);

    //deletes a specified game, used when the game isn't in the list of live games returned by the API, therefore is finished and notifications are no longer needed
    @Query("Delete from FollowedGame Where gameID = :ID")
    void deleteFollowedGame(int ID);
}
