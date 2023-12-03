package com.example.mobileappdevcoursework;

/*
This class is used in the GameDetails Fragment to store information about the game a User has selected in HomeFragment
 */
public class GameInstance {

    private String gameName;
    private String startTime;
    private String venue;
    private int homeTeamPosition;
    private int awayTeamPosition;
    private String homeName;
    private String awayName;

    public GameInstance(String gameName, String startTime, String venue, int homeTeamPosition, int awayTeamPosition, String homeName, String awayName) {
        this.gameName = gameName;
        this.startTime = startTime;
        this.venue = venue;
        this.homeTeamPosition = homeTeamPosition;
        this.awayTeamPosition = awayTeamPosition;
        this.homeName = homeName;
        this.awayName = awayName;
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
