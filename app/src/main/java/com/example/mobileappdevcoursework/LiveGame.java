package com.example.mobileappdevcoursework;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LiveGame {

    String title;
    int id;
    String startTime;
    int venueID;
    String venue;
    String homeName;
    String awayName;
    int homePos;
    int awayPos;
    List<Event> events;
    String score;
    int leagueId;

    public LiveGame(String title, String date, int id, int venueID,
                            String homeName, String awayName, int homePos, int awayPos,
                            List<Event> events, String score, int leagueId) {
        this.title = title;
        this.startTime = date;
        this.id = id;
        this.venueID = venueID;
        this.venue = getVenue();
        this.homeName = homeName;
        this.awayName = awayName;
        this.homePos = homePos;
        this.awayPos = awayPos;
        this.events = events != null ? events : new ArrayList<>();
        sortEventsByMinute();
        this.score = score;
        this.leagueId = leagueId;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "LiveGame{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", startTime='" + startTime + '\'' +
                ", venueID=" + venueID +
                ", venue='" + venue + '\'' +
                ", homeName='" + homeName + '\'' +
                ", awayName='" + awayName + '\'' +
                ", homePos=" + homePos +
                ", awayPos=" + awayPos +
                ", events=" + events +
                ", score='" + score + '\'' +
                ", leagueId=" + leagueId +
                '}';
    }

    public String getTitle() {
        return title;
    }


    public String getStartTime() {
        return startTime;
    }

    public int getVenueID() {
        return venueID;
    }


    public String getHomeName() {
        return homeName;
    }

    public String getAwayName() {
        return awayName;
    }

    public int getHomePos() {
        return homePos;
    }

    public int getAwayPos() {
        return awayPos;
    }

    public List<Event> getEvents() {
        return events;
    }

    public String getScore() {
        return score;
    }

    public int eventCount() {
        return this.events.size();
    }
    public String getVenue() {
        final StringBuilder venue = new StringBuilder();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.sportmonks.com/v3/football/venues/" + venueID + "?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    connection.disconnect();

                    if (content != null) {
                        String jsonString = content.toString();
                        venue.append(jsonParser.getVenue(jsonString));
                    }
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

        return venue.toString();
    }



    public String getHomeEventList() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Event event : this.events) {
//            stringBuilder.append("Event ID: ").append(event.getId()).append("\n");
//            stringBuilder.append("Event Name: ").append(event.getName()).append("\n");
//            stringBuilder.append("Event Minute: ").append(event.getMinute()).append("\n");
            if(event.getTeam() == "home") {
                stringBuilder.append(event.getName()).append("\n");
                System.out.println("added home event");
            }else{
                stringBuilder.append("---------------\n");
            }


        }

        return stringBuilder.toString();
    }

    public String getAwayEventList() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Event event : this.events) {
//            stringBuilder.append("Event ID: ").append(event.getId()).append("\n");
//            stringBuilder.append("Event Name: ").append(event.getName()).append("\n");
//            stringBuilder.append("Event Minute: ").append(event.getMinute()).append("\n");
            if(event.getTeam() == "away") {
                stringBuilder.append(event.getName()).append("\n");
                System.out.println("added away event");
            }else{
                stringBuilder.append("---------------\n");
            }



        }

        return stringBuilder.toString();
    }
    public String getMins(){
        StringBuilder stringBuilder = new StringBuilder();
        for (Event event: this.events){
            stringBuilder.append(event.getMinute()).append("\n");

        }
        return stringBuilder.toString();
    }

    public Event getEventAtIndex(int index) {
        if (index >= 0 && index < events.size()) {
            return events.get(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }
    public void sortEventsByMinute() {
        // Use Collections.sort with a custom comparator to sort events based on the minute
        Collections.sort(events, Comparator.comparing(this::parseMinute));
    }

    private int parseMinute(Event event) {
        String minuteString = event.getMinute();

        // Split the string to separate main time and additional time
        String[] parts = minuteString.split("\\+");

        // Parse the main time
        int mainTime = Integer.parseInt(parts[0].trim());

        // Add additional time if available
        int additionalTime = (parts.length > 1) ? Integer.parseInt(parts[1].trim()) : 0;

        // Calculate the total minute value
        return mainTime * 100 + additionalTime;
    }
}


//package com.example.mobileappdevcoursework;
//
//import java.util.List;
//
//public class LiveGame {
//
//    String title;
//    String date;
//    int id;
//
//    List<Event> events;
//
//    public LiveGame(String title, String date, int id, List<Event> events) {
//        this.title = title;
//        this.date = date;
//        this.id = id;
//        this.events = events;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//    public int eventCount(){return this.events.size();}
//}
