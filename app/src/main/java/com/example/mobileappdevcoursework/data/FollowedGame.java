package com.example.mobileappdevcoursework.data;

import androidx.room.*;
//import androidx.room.PrimaryKey;

@Entity
public class FollowedGame {

    @PrimaryKey
    @ColumnInfo(name = "gameID")
    int gameID;

    @ColumnInfo(name = "event_count")
    int eventCount;

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public int getGameID() {
        return gameID;
    }

    public int getEventCount() {
        return eventCount;
    }
}