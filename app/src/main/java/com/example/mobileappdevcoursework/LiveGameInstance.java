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
    int homeScore;
    int awayScore;

    public LiveGameInstance(String title, String startTime, int venueID, String homeName, String awayName, int homePos, int awayPos, List<Event> events, int homeScore, int awayScore) {
        this.title = title;
        this.startTime = startTime;
        this.venueID = venueID;
        this.venue = getVenue();
        this.homeName = homeName;
        this.awayName = awayName;
        this.homePos = homePos;
        this.awayPos = awayPos;
        this.events = events;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    @Override
    public String toString() {
        return "LiveGameInstance{" +
                "title='" + title + '\'' +
                ", startTime='" + startTime + '\'' +
                ", venueID=" + venueID +
                ", venue='" + venue + '\'' +
                ", homeName='" + homeName + '\'' +
                ", awayName='" + awayName + '\'' +
                ", homePos=" + homePos +
                ", awayPos=" + awayPos +
                ", events=" + events +
                ", homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                '}';
    }

    public static String getEventList(List<Event> events) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Event event : events) {
//            stringBuilder.append("Event ID: ").append(event.getId()).append("\n");
//            stringBuilder.append("Event Name: ").append(event.getName()).append("\n");
//            stringBuilder.append("Event Minute: ").append(event.getMinute()).append("\n");
            stringBuilder.append(event.getName());
            stringBuilder.append(event.getMinute()).append("\n");
            stringBuilder.append("---------------\n");
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
