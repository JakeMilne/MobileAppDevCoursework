package com.example.mobileappdevcoursework;
import androidx.annotation.NonNull;
import androidx.room.*;

@Entity
public class user {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "league")
    int favouriteLeague;

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
