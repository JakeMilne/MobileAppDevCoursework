package com.example.mobileappdevcoursework.data;

import androidx.room.*;

//The Game class is used to store the items that are shown in the recyclerView in HomeFragment
@Entity
public class Game {

    @PrimaryKey
    @ColumnInfo(name = "gameID") //id of the game = used to search for the game when GameDetails is opened
    int gameID;

    @ColumnInfo(name = "game_name") //name in the format Home vs Away
    String gameName;

    @ColumnInfo(name = "start_time") //date + time of kick off, the recyclerView is sorted by this
    String startTime;


    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
