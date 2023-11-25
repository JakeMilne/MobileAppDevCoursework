package com.example.mobileappdevcoursework;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class jsonParser {

    public static List<Item> parseJson(String jsonString) {
        List<Item> items = new ArrayList<>();

        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement root = jsonParser.parse(jsonString);

            if (root.isJsonObject()) {
                JsonObject jsonObject = root.getAsJsonObject();
                JsonArray dataArray = jsonObject.getAsJsonArray("data");

                for (JsonElement itemElement : dataArray) {
                    JsonObject jsonItem = itemElement.getAsJsonObject();

                    String name = jsonItem.get("name").getAsString();
                    String startTime = jsonItem.get("starting_at").getAsString();
                    int id = jsonItem.get("id").getAsInt();
                    Item item = new Item(name, startTime, id);
                    items.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return items;
    }

    public static gameInstance parseGame(String jsonString) {


        Gson gson = new Gson();
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonArray dataArray = jsonObject.getAsJsonArray("data");


        JsonObject gameObject = dataArray.get(0).getAsJsonObject();
        String gameName = gameObject.get("name").getAsString();
        String startTime = gameObject.get("starting_at").getAsString();
        String venue = gameObject.get("venue_id").getAsString();


        JsonArray participants = gameObject.getAsJsonArray("participants");

        String homeName = participants.get(0).getAsJsonObject().getAsJsonObject("meta").get("name").getAsString();
        String awayName = participants.get(1).getAsJsonObject().getAsJsonObject("meta").get("name").getAsString();
        int homeTeamPosition = participants.get(0).getAsJsonObject().getAsJsonObject("meta").get("position").getAsInt();
        int awayTeamPosition = participants.get(1).getAsJsonObject().getAsJsonObject("meta").get("position").getAsInt();


        return new gameInstance(gameName, startTime, venue, homeTeamPosition, awayTeamPosition, homeName, awayName);
    }

    ;

    public static LiveGameInstance parseLiveGame(String jsonData, int targetGameId) {
        Gson gson = new Gson();
        System.out.println(jsonData);
        JsonObject dataObject = gson.fromJson(jsonData, JsonObject.class);
        JsonArray dataArray = dataObject.getAsJsonArray("data");

        for (JsonElement element : dataArray) {
            JsonObject gameObject = element.getAsJsonObject();
            int gameId = gameObject.getAsJsonPrimitive("id").getAsInt();

            if (gameId == targetGameId) {
                String gameName = gameObject.getAsJsonPrimitive("name").getAsString();
                String startTime = gameObject.getAsJsonPrimitive("starting_at").getAsString();
                int venueId = gameObject.getAsJsonPrimitive("venue_id").getAsInt();

                JsonObject team1 = gameObject.getAsJsonArray("participants").get(0).getAsJsonObject();
                JsonObject team2 = gameObject.getAsJsonArray("participants").get(1).getAsJsonObject();

                String team1Name = team1.getAsJsonPrimitive("name").getAsString();
                String team2Name = team2.getAsJsonPrimitive("name").getAsString();

                JsonElement team1PositionElement = team1.getAsJsonObject("meta").get("position");
                JsonElement team2PositionElement = team2.getAsJsonObject("meta").get("position");

                int team1Position = team1PositionElement != null && team1PositionElement.isJsonPrimitive()
                        ? team1PositionElement.getAsJsonPrimitive().getAsInt()
                        : 0;

                int team2Position = team2PositionElement != null && team2PositionElement.isJsonPrimitive()
                        ? team2PositionElement.getAsJsonPrimitive().getAsInt()
                        : 0;
                JsonElement team1Meta = team1.getAsJsonObject("meta");
                JsonElement team2Meta = team2.getAsJsonObject("meta");

                System.out.println("Team 1 Meta: " + (team1Meta != null && !team1Meta.isJsonNull() ? team1Meta : "null"));
                System.out.println("Team 2 Meta: " + (team2Meta != null && !team2Meta.isJsonNull() ? team2Meta : "null"));

                int team1Score = 0;
                JsonElement scoreElement1 = team1.getAsJsonObject("meta").get("score");
                if (scoreElement1 != null && scoreElement1.isJsonPrimitive()) {
                    team1Score = scoreElement1.getAsInt();
                }

                int team2Score = 0;
                JsonElement scoreElement2 = team2.getAsJsonObject("meta").get("score");
                if (scoreElement2 != null && scoreElement2.isJsonPrimitive()) {
                    team2Score = scoreElement2.getAsInt();
                }

                // Get events array
                JsonArray eventsArray = gameObject.getAsJsonArray("events");

                // Create a list to store events
                List<Event> events = new ArrayList<>();

                // Parse each event and add to the list
                for (JsonElement eventElement : eventsArray) {
                    JsonObject eventObject = eventElement.getAsJsonObject();

                    int eventId = eventObject.getAsJsonPrimitive("id").getAsInt();
                    JsonElement infoElement = eventObject.get("info");
                    String eventName = (infoElement != null && !infoElement.isJsonNull()) ? infoElement.getAsString() : "";

                    int eventMinute = eventObject.getAsJsonPrimitive("minute").getAsInt();

                    // You may need to extract more details based on your Event class structure

                    // Create Event instance and add to the list
                    Event event = new Event(eventId, eventName, eventMinute);
                    events.add(event);
                }

                // Create and return LiveGameInstance with events
                return new LiveGameInstance(gameName, startTime, venueId, team1Name, team2Name, team1Position, team2Position, events, team1Score, team2Score);
            }
        }

        return null;
    }

}
