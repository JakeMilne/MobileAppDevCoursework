package com.example.mobileappdevcoursework;

import androidx.room.*;

import java.util.List;

@Dao
public interface UserDAO {



    @Query("Select name from user")
    String getName();

    @Query("Select league from user")
    int getLeague();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(user userUpdate);



    @Query("Delete from user")
    void deleteUser();
}