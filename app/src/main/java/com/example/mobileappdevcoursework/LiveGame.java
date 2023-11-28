package com.example.mobileappdevcoursework;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class LiveGame {

    String title;
    String date;
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

    public LiveGame(String title, String date, int id, int venueID,
                            String homeName, String awayName, int homePos, int awayPos,
                            List<Event> events, String score) {
        this.title = title;
        this.date = date;
        this.id = id;
        this.venueID = venueID;
        this.venue = getVenue();
        this.homeName = homeName;
        this.awayName = awayName;
        this.homePos = homePos;
        this.awayPos = awayPos;
        this.events = events;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
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
    public String getVenue(){
        String venue = "";
        try {


            URL url = new URL("https://api.sportmonks.com/v3/football/venues/id=" + this.venueID + "?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:271");
            //scotland 1
            //URL url = new URL("https://api.sportmonks.com/v3/football/livescores/inplay?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:501");


            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                //System.out.println(inputLine.toString());
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            if (content != null) {
                String jsonString = content.toString();
                System.out.println(jsonString);
                venue = jsonParser.getVenue(jsonString);
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        return venue;
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
