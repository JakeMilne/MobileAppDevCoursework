package com.example.mobileappdevcoursework.data;
import android.content.Context;

import androidx.room.*;

@Database(entities = {User.class}, version = 3) //User got changed when I was making the app, meaning the version had to be incremented to prevent errors
public abstract class UserDatabase extends RoomDatabase{
    public abstract UserDAO UserDAO();

    private static UserDatabase INSTANCE;

    public static UserDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (UserDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UserDatabase.class, "user_database")
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