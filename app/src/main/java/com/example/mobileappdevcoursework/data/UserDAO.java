package com.example.mobileappdevcoursework.data;

import androidx.room.*;

@Dao
public interface UserDAO {



    @Query("Select name from User")
    String getName();

    @Query("Select league from User")
    int getLeague();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User userUpdate);//updating the user, used when they click the save button on UserProfile



    @Query("Delete from User")
    void deleteUser();//used when they click the save button on UserProfile, to ensure that only 1 user is in the table
}