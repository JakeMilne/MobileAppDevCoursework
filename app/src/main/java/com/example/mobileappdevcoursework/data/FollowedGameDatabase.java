package com.example.mobileappdevcoursework.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {FollowedGame.class}, version = 1, exportSchema = false)
public abstract class FollowedGameDatabase extends RoomDatabase {

    public abstract FollowedGameDAO followedGameDAO();

    private static FollowedGameDatabase INSTANCE;

    public static FollowedGameDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (FollowedGameDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FollowedGameDatabase.class, "followed_game_database")
                            .fallbackToDestructiveMigration()
                            // the following is only for testing / initial dev purposes
                            //.allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
