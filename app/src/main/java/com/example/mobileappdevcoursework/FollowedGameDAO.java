package com.example.mobileappdevcoursework;

import androidx.room.*;
import android.content.Context;

import java.util.List;

@Dao
public interface FollowedGameDAO {

    @Query("SELECT * FROM FollowedGame")
    List<FollowedGame> getAllFollowed();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateFollowedGames(FollowedGame game);

    @Query("Delete from FollowedGame Where gameID = :ID")
    void deleteFollowedGame(int ID);
}
