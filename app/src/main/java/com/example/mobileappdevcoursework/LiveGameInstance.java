package com.example.mobileappdevcoursework;

import java.util.List;

public class LiveGameInstance {

    String title;
    String startTime;
    int venueID;
    String venue;
    String homeName;
    String awayName;
    int homePos;
    int awayPos;
    List<Event> events;



    String score;

    public LiveGameInstance(String title, String startTime, int venueID, String homeName, String awayName, int homePos, int awayPos, List<Event> events, String score) {
        this.title = title;
        this.startTime = startTime;
        this.venueID = venueID;
        this.venue = getVenue();
        this.homeName = homeName;
        this.awayName = awayName;
        this.homePos = homePos;
        this.awayPos = awayPos;
        this.events = events;

        this.score = score;
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

    public String getVenue(){
//        require "uri"
//        require "net/http"
//
//        url = URI("https://api.sportmonks.com/v3/football/venues/{ID}?api_token=YOUR_TOKEN")
//
//        https = Net::HTTP.new(url.host, url.port)
//        https.use_ssl = true
//
//        request = Net::HTTP::Get.new(url)
//
//                response = https.request(request)
//        puts response.read_body

        return("hi");
    }
}
