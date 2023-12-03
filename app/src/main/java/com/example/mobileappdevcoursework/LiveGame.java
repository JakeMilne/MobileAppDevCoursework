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
        if (events != null) {
            this.events = events;
            setScores();//since the api returns an unordered list of events, we need to wait until the list of events is ordered to assign accurate scores to events that have null scores

        } else {
            this.events = new ArrayList<>();
        }

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
    private void setScores(){
        if (!events.isEmpty()) {
            events.get(0).setResult("0-0");
            for (int i = events.size() - 1; i > 0; i--) {
                while (events.get(i).getResult() == null) {
                    events.get(i).setResult(events.get(i - 1).getResult());
                    i--;
                }
            }
            if(events.size()==0){
                score = "0-0";
            }else {
                score = events.get(events.size() - 1).getResult();
            }
        }
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


                    String jsonString = content.toString();
                    venue.append(JsonParse.getVenue(jsonString));

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



    // the next three methods are used to create a timeline of events in a game, where the events related to the home team are in a textView on the left, the minutes in the middle textView, and events related to the awayteam are on the right. if an event is not related to a team, a line of ---- will be shown instead
    public String getHomeEventList() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Event event : this.events) {
//            stringBuilder.append("Event ID: ").append(event.getId()).append("\n");
//            stringBuilder.append("Event Name: ").append(event.getName()).append("\n");
//            stringBuilder.append("Event Minute: ").append(event.getMinute()).append("\n");
            if(event.isHome()) {
                stringBuilder.append(event.getName()).append("\n");
            }else{
                stringBuilder.append("---------------\n\n");
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
            if(!event.isHome()) {
                stringBuilder.append(event.getName()).append("\n");
            }else{
                stringBuilder.append("---------------\n\n");
            }



        }

        return stringBuilder.toString();
    }
    public String getMins(){
        StringBuilder stringBuilder = new StringBuilder();
        for (Event event: this.events){
            stringBuilder.append(event.getMinute()).append("\n\n");

        }
        return stringBuilder.toString();
    }

    public Event getEventAtIndex(int index) {

        return events.get(index);

    }
    public void sortEventsByMinute() {
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {
                // Split on '+' if present to get the first part of the minute
                String minute1 = event1.getMinute().split("\\+")[0].trim();  // Trim the string
                String minute2 = event2.getMinute().split("\\+")[0].trim();  // Trim the string

                // Convert to integers for comparison
                int minuteValue1 = Integer.parseInt(minute1);
                int minuteValue2 = Integer.parseInt(minute2);

                // Compare the minutes
                return Integer.compare(minuteValue1, minuteValue2);
            }
        });
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
