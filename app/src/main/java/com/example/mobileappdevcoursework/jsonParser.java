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
        int homeTeamPosition = participants.get(0).getAsJsonObject().getAsJsonObject("meta").get("position").getAsInt();
        int awayTeamPosition = participants.get(1).getAsJsonObject().getAsJsonObject("meta").get("position").getAsInt();



        return new gameInstance(gameName, startTime, venue, homeTeamPosition, awayTeamPosition);
    };

}
