package com.example.mobileappdevcoursework.data;
import androidx.annotation.NonNull;
import androidx.room.*;

@Entity
public class user {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    String name; //users name, gets displayed on the upcoming games page

    @ColumnInfo(name = "league")
    int favouriteLeague; // league id

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
}
