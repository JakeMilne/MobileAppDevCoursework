package com.example.mobileappdevcoursework;

public class gameInstance {

    String gameName;
    String startTime;
    String venue;
    int homeTeamPosition;
    int awayTeamPosition;

    public gameInstance(String gameName, String startTime, String venue, int homeTeamPosition, int awayTeamPosition) {
        this.gameName = gameName;
        this.startTime = startTime;
        this.venue = venue;
        this.homeTeamPosition = homeTeamPosition;
        this.awayTeamPosition = awayTeamPosition;
    }
}
