package com.example.mobileappdevcoursework.data;

import androidx.room.*;

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