package com.example.mobileappdevcoursework.data;
import android.content.Context;

import androidx.room.*;

@Database(entities = {Game.class}, version = 1, exportSchema = false)
public abstract class GameDatabase extends RoomDatabase{
    public abstract GameDAO GameDAO();

    private static GameDatabase INSTANCE;

    public static GameDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (GameDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    GameDatabase.class, "game_database")
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
