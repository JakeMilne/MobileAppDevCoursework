package com.example.mobileappdevcoursework.data;

import androidx.room.*;

//users can "follow" a live game, which enables notifications, this is used to store details about those games
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