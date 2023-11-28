package com.example.mobileappdevcoursework;

/*
This class is used in the GameDetails Fragment to store information about the game a user has selected in HomeFragment
 */
public class gameInstance {

    String gameName;
    String startTime;
    String venue;
    int homeTeamPosition;
    int awayTeamPosition;
    String homeName;
    String awayName;

    public gameInstance(String gameName, String startTime, String venue, int homeTeamPosition, int awayTeamPosition,String homeName, String awayName) {
        this.gameName = gameName;
        this.startTime = startTime;
        this.venue = venue;
        this.homeTeamPosition = homeTeamPosition;
        this.awayTeamPosition = awayTeamPosition;
        this.homeName = homeName;
        this.awayName = awayName;
    }
}
