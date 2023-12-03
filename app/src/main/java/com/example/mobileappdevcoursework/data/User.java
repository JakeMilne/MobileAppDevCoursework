package com.example.mobileappdevcoursework.data;
import androidx.annotation.NonNull;
import androidx.room.*;

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
