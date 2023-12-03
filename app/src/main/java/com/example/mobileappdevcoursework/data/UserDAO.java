package com.example.mobileappdevcoursework.data;

import androidx.room.*;

@Dao
public interface UserDAO {



    @Query("Select name from User")
    String getName();

    @Query("Select league from User")
    int getLeague();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User userUpdate);



    @Query("Delete from User")
    void deleteUser();
}