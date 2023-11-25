package com.example.mobileappdevcoursework;

import android.util.Log;

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

    public static LiveGameInstance parseLiveGame(String jsonData, int targetGameId) {
        Gson gson = new Gson();
        System.out.println(jsonData);
        JsonObject dataObject = gson.fromJson(jsonData, JsonObject.class);
        JsonArray dataArray = dataObject.getAsJsonArray("data");

        for (JsonElement element : dataArray) {
            JsonObject gameObject = element.getAsJsonObject();
            int gameId = gameObject.getAsJsonPrimitive("id").getAsInt();

            if (gameId == targetGameId) {
                Log.d("JsonResponse", gameObject.toString());
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

                JsonArray eventsArray = gameObject.getAsJsonArray("events");
                List<Event> events = new ArrayList<>();

                for (JsonElement eventElement : eventsArray) {
                    JsonObject eventObject = eventElement.getAsJsonObject();

                    int eventId = eventObject.getAsJsonPrimitive("id").getAsInt();
                    String eventName = "";
                    JsonElement infoElement = eventObject.get("info");

                    if (infoElement != null && !infoElement.isJsonNull() && infoElement.isJsonPrimitive()) {
                        eventName = infoElement.getAsString();
                    }

                    int eventMinute = eventObject.getAsJsonPrimitive("minute").getAsInt();
                    String result = eventObject.getAsJsonPrimitive("result").getAsString();
                    String addition = eventObject.getAsJsonPrimitive("addition").getAsString();

                    Event event = new Event(eventId, eventName, eventMinute, result, addition);
                    events.add(event);
                }

                int team1Score = getScore(team1);
                int team2Score = getScore(team2);

                return new LiveGameInstance(gameName, startTime, venueId, team1Name, team2Name, team1Position, team2Position, events, team1Score, team2Score);
            }
        }

        return null;
    }

    // Helper method to extract the score from the team JSON object
    private static int getScore(JsonObject team) {
        JsonElement scoreElement = team.getAsJsonObject("meta").get("score");
        return (scoreElement != null && scoreElement.isJsonPrimitive()) ? scoreElement.getAsInt() : 0;
    }
}
