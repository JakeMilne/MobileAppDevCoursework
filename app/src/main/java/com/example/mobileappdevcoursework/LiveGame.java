package com.example.mobileappdevcoursework;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


//used to store details about ongoing games, retrieved from the fixtures/inplay endpoint
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
    List<Event> events; //events refer to notable things that happened in the game, such as goals or yellow cards
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


    //setScores is used to assign each event a result, since an event will only have a result if the event was a goal


    private void setScores(){
        if (!events.isEmpty()) {
            if(events.get(0) == null){
                events.get(0).setResult("0-0");
                //if the first event wasnt a goal, it sets the result to 0-0, its important
                //that the first event has a result because the method loops backwards to
                //find the most recent result before an event took place if the event wasn't a goal.
                //Because of this, if the first events result was null and events that took place before
                //the first goal would have a null result and wouldn't have the scoreline in the notification
            }
            for (int i = events.size() - 1; i > 0; i--) {
                while (events.get(i).getResult() == null) {
                    events.get(i).setResult(events.get(i - 1).getResult());
                    i--;
                }
            }
            //after setting a result for each event, get the result
            //at the latest event and set the score to that result
            score = events.get(events.size() - 1).getResult();

        } else{
            score = "0-0";
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

    //this method addresses the same venue name problem that GameInstance faces, where the API returns an ID for the venue, but not the name
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



    //the next three methods are used to create a timeline of events in a game,
    //where the events related to the home team are in a textView on the left, the minutes
    //in the middle textView, and events related to the awayteam are on the right.
    //if an event is not related to a team, a line of ---- will be shown instead
    public String getHomeEventList() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Event event : this.events) {
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

    public Event getEventAtIndex(int index) { //this is used for notifications
        return events.get(index);
    }

    //the events returned by the API aren't ordered, so they need to be ordered for the notifications and the timeline to function properly
    public void sortEventsByMinute() { //generated by ChatGPT 3.5
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

