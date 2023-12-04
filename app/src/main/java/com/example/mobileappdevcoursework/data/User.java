package com.example.mobileappdevcoursework.data;
import androidx.annotation.NonNull;
import androidx.room.*;

//Used to store information about the user, there should only ever be 1 user in the table, making it like a configuration file
@Entity
public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    String name; //users name, gets displayed on the upcoming games page

    @ColumnInfo(name = "league")
    int favouriteLeague; // id of league selected using spinner

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFavouriteLeague() {
        return favouriteLeague;
    }

    public void setFavouriteLeague(int favouriteLeague) {
        this.favouriteLeague = favouriteLeague;
    }

    public User(@NonNull String name, int favouriteLeague) {
        this.name = name;
        this.favouriteLeague = favouriteLeague;
    }

}
