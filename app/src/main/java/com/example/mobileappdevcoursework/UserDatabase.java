package com.example.mobileappdevcoursework;
import android.content.Context;

import androidx.room.*;

@Database(entities = {user.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase{
    public abstract UserDAO UserDAO();

    private static UserDatabase INSTANCE;

    public static UserDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (UserDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UserDatabase.class, "game_database")
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