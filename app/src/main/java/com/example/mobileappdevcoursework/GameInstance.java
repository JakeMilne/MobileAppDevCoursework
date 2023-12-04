package com.example.mobileappdevcoursework;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
This class is used in the GameDetails Fragment to store information about the game a User has selected in HomeFragment
 */
public class GameInstance {

    private String gameName;
    private String startTime;
    private String venue;
    private String venueName;
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
        return this.venue;
    }
    public String getVenueName(){

            final StringBuilder venueString = new StringBuilder();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("https://api.sportmonks.com/v3/football/venues/" + venue + "?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder content = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        in.close();
                        connection.disconnect();


                        String jsonString = content.toString();
                        venueString.append(JsonParse.getVenue(jsonString));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

            try {
                // Wait for the thread to finish
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return venueString.toString();

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
