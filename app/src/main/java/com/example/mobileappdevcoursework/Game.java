package com.example.mobileappdevcoursework;

import androidx.room.*;
//import androidx.room.PrimaryKey;

@Entity
public class Game {

    @PrimaryKey
    @ColumnInfo(name = "gameID")
    int gameID;

    @ColumnInfo(name = "game_name")
    String gameName;

    @ColumnInfo(name = "start_time")
    String startTime;

    @ColumnInfo(name = "venue_name")
    String venue;

    @ColumnInfo(name = "home_pos")
    int homeTeamPosition;

    @ColumnInfo(name = "away_pos")
    int awayTeamPosition;

    @ColumnInfo(name = "home_name")
    String homeName;

    @ColumnInfo(name = "away_name")
    String awayName;

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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getHomeTeamPosition() {
        return homeTeamPosition;
    }

    public void setHomeTeamPosition(int homeTeamPosition) {
        this.homeTeamPosition = homeTeamPosition;
    }

    public int getAwayTeamPosition() {
        return awayTeamPosition;
    }

    public void setAwayTeamPosition(int awayTeamPosition) {
        this.awayTeamPosition = awayTeamPosition;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAwayName() {
        return awayName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }
}
