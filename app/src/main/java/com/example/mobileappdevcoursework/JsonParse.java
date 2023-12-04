package com.example.mobileappdevcoursework;

import android.util.Log;

import com.example.mobileappdevcoursework.data.Game;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class JsonParse {

    //method that takes json produced by the endpoint /v3/football/fixtures/ and formats it into a List of type Game, this is used in HomeViewModel to fill the recyclerView in the HomeFragment
    // and is also what is stored in GameDatabase
    //this method could be combined with parseGame, but I didn't want to store unnecessary data in room
    public static List<Game> parseJson(String jsonString) {
        List<Game> games = new ArrayList<>();

        try {
            JsonElement root = JsonParser.parseString(jsonString);

            if (root.isJsonObject()) {
                JsonObject jsonObject = root.getAsJsonObject();
                JsonArray dataArray = jsonObject.getAsJsonArray("data"); // games are in the "data" tag in the json

                for (JsonElement itemElement : dataArray) { // looping for each Game in the json
                    JsonObject jsonItem = itemElement.getAsJsonObject();

                    //gameID is the primary key
                    int gameID = jsonItem.get("id").getAsInt();

                    //gameName and startTime are showed in the recyclerView
                    String gameName = jsonItem.get("name").getAsString();
                    String startTime = jsonItem.get("starting_at").getAsString();

                    //create an instance of Game with the data from the API
                    Game game = new Game();
                    game.setGameID(gameID);
                    game.setGameName(gameName);
                    game.setStartTime(startTime);

                    //adding the game to the List
                    games.add(game);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return games;
    }



    //method that takes json produced by the endpoint /v3/football/livescores/ and formats it into a List of type Game, this is used in LiveViewModel to fill the recyclerView in the live scores fragment, as well as the live details page, and the mainactivity, as it is used to produce notifications
    public static List<LiveGame> parseLiveJson(String jsonString, int leagueid) {
        List<LiveGame> liveGames = new ArrayList<>();
//        String lastResult = "0-0";


        try {
            JsonElement root = JsonParser.parseString(jsonString);
            if (root.isJsonObject()) {
                JsonObject jsonObject = root.getAsJsonObject();
                JsonArray dataArray = jsonObject.getAsJsonArray("data");
                if (dataArray != null) {
                    for (JsonElement dataElement : dataArray) {
                        //lastResult was initially on line 61, but I think theres an error with the logic so it's been moved,
                        //however there aren't any live games on between now (morning of the 4th) and the deadline so I can't test it properly.
                        //with lastResult on line 61 I think the result from one game would end up being passed into a second game if the score in the second game is 0-0
                        String lastResult = "0-0"; //last result is the current score. if an event is a goal it will contain the score, however if it isnt a goal result will be null.
                        // lastresult is passed to the live game instance as a placeholder, however there the setScores function also sets the score to the last result as a backup.
                        // lastResult could probably be deleted without the app being effected

                        JsonObject dataObject = dataElement.getAsJsonObject();
                        JsonArray eventsArray = dataObject.getAsJsonArray("events"); //storing all "events" (goals, yellow cards etc) in a json array
                        List<Event> events = new ArrayList<>(); //creating a list of Event to be passed into the LiveGame

                        int homeTeamId, awayTeamId = 0; //team ids
                        String homeName, awayName = "";// team names
                        homeTeamId = dataObject.getAsJsonArray("participants").get(0).getAsJsonObject().getAsJsonPrimitive("id").getAsInt();
                        homeName = dataObject.getAsJsonArray("participants").get(0).getAsJsonObject().getAsJsonPrimitive("name").getAsString();

                        awayTeamId = dataObject.getAsJsonArray("participants").get(1).getAsJsonObject().getAsJsonPrimitive("id").getAsInt();
                        awayName = dataObject.getAsJsonArray("participants").get(1).getAsJsonObject().getAsJsonPrimitive("name").getAsString();


                        for (JsonElement eventElement : eventsArray) {
                            JsonObject eventObject = eventElement.getAsJsonObject();
                            //System.out.println(eventElement.toString());
                            int eventId = eventObject.getAsJsonPrimitive("id").getAsInt();
                            String eventName = "";
                            JsonElement playerNameElement = eventObject.get("player_name");
                            String name = "";
                            if (playerNameElement != null && playerNameElement.isJsonPrimitive()) { //is primitive checks if its a valid type https://www.javadoc.io/doc/com.google.code.gson/gson/2.6.2/com/google/gson/JsonPrimitive.html
                                name = playerNameElement.getAsJsonPrimitive().getAsString(); //player name
                            }
                            //JsonElement infoElement = eventObject.get("info");
                            //converting event ids to their equivalent values, which can be found by calling the endpoint /v3/core/types
                            int eventCode = eventObject.getAsJsonPrimitive("type_id").getAsInt(); //https://www.javadoc.io/doc/com.google.code.gson/gson/2.6.1/com/google/gson/JsonElement.html

                            if (eventCode == 18) { // originally I had eventObject.getAsJsonPrimitive("type_id").getAsInt() in each if instead of eventCode, This shouldn't be a problem but I can't test it, in case this part breaks using eventObject.getAsJsonPrimitive("type_id").getAsInt() was what was working when I tested it. but I think eventCode is better for speed
                                eventName = "Substitution\n";
                            } else if (eventCode == 19) {
                                eventName = "Yellow Card \n" + name;
                            } else if (eventCode == 14) {
                                eventName = "Goal \n" + name;
                            } else if (eventCode == 16) {
                                eventName = "(P) Goal \n" + name; // penalty goal
                            } else if (eventCode == 17) {
                                eventName = "(P) Miss \n" + name; //penalty miss
                            } else if (eventCode == 15) {
                                eventName = "OG \n" + name; //own goal. not sure how the api classes the team id for this, so it might appear on the wrong side
                            } else if (eventCode == 20 || eventCode == 21) {//20 is straight red, 21 is second yellow so both have same outcome
                                eventName = "red card \n" + name;
                            }


                            String eventMinute = eventObject.getAsJsonPrimitive("minute").getAsString();
                            JsonElement extraMinuteElement = eventObject.get("extra_minute");

                            if (extraMinuteElement != null && !extraMinuteElement.isJsonNull()) {
                                eventMinute += " + " + extraMinuteElement.getAsJsonPrimitive().getAsString(); // if extra minute isn't null the game is in injury time, and so the minute field gets combined with the extra_minute field to get the overall minute (i.e. 45+2)
                            }
                            String result = "";
                            JsonElement resultElement = eventObject.get("result");
                            if (resultElement != null && resultElement.isJsonPrimitive()) {
                                result = resultElement.getAsJsonPrimitive().getAsString();
                                if(result != null){
                                    lastResult = result;  // Update lastResult when result is non-null


                                }
                            }

                            String addition = "";
                            JsonElement additionElement = eventObject.get("addition"); //additional information about the event, an example would be "1st goal" or "2nd goal"

                            if (additionElement != null && additionElement.isJsonPrimitive()) {
                                addition = additionElement.getAsJsonPrimitive().getAsString();
                            }

                            int participantId = eventObject.getAsJsonPrimitive("participant_id").getAsInt();//team id that the event refers to
                            String team = "";  // team name of the team that the event refers to
                            boolean home = false;
                            if (eventName != "") {
                            if (participantId == homeTeamId) {
                                team = homeName;
                                home = true; //used to create the timeline in live game details, the home boolean is used to sort which side each event belongs to
                            } else if (participantId == awayTeamId) {
                                team = awayName;
                            }

                            events.add(new Event(eventId, eventName, eventMinute, result, addition, team, home)); //adding new event to list, which is then passed into the LiveGame

                            }

                        }

                        //filling out the rest of LiveGame
                        int id = dataObject.getAsJsonPrimitive("id").getAsInt();
                        String title = dataObject.getAsJsonPrimitive("name").getAsString();
                        String date = dataObject.getAsJsonPrimitive("starting_at").getAsString();
                        int venueId = dataObject.getAsJsonPrimitive("venue_id").getAsInt();

                        JsonArray participants = dataObject.getAsJsonArray("participants");
                        JsonObject homeTeam = participants.get(0).getAsJsonObject();
                        JsonObject awayTeam = participants.get(1).getAsJsonObject();

                        String homeTeamName = homeTeam.getAsJsonPrimitive("name").getAsString();
                        String awayTeamName = awayTeam.getAsJsonPrimitive("name").getAsString();

                        JsonElement homeTeamPositionElement = homeTeam.getAsJsonObject("meta").get("position");
                        JsonElement awayTeamPositionElement = awayTeam.getAsJsonObject("meta").get("position");

                        // Handle null values for team positions, positions refers to the league standings, ie 1 = first in the league
                        int homeTeamPosition = 0;
                        if(homeTeamPositionElement != null){
                            homeTeamPosition = homeTeamPositionElement.getAsInt();
                        }
                        int awayTeamPosition = 0;
                        if(awayTeamPositionElement != null){
                            awayTeamPosition = awayTeamPositionElement.getAsInt();
                        }

                        LiveGame liveGame = new LiveGame(title, date, id, venueId, homeTeamName, awayTeamName, homeTeamPosition, awayTeamPosition, events, lastResult, leagueid);
                        liveGames.add(liveGame);
                    }
                }else {
                    Log.e("jsonParse", "dataArray is null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return liveGames;
    }



    //used for game details since the game objects that get stored in the database don't have all the details needed.
    public static GameInstance parseGame(String jsonString) {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonObject dataObject = jsonObject.getAsJsonObject("data");

        String gameName = dataObject.get("name").getAsString(); //game name, in the format Home vs Away
        String startTime = dataObject.get("starting_at").getAsString();//date + time of kick-off
        String venue = dataObject.get("venue_id").getAsString(); //id of the venue the game is played at
        JsonArray participants = dataObject.getAsJsonArray("participants");

        String homeName = participants.get(0).getAsJsonObject().get("name").getAsString();
        String awayName = participants.get(1).getAsJsonObject().get("name").getAsString();
        int homeTeamPosition = participants.get(0).getAsJsonObject().getAsJsonObject("meta").get("position").getAsInt();// positions refers to the league standings, ie 1 = first in the league (same as in LiveGame)
        int awayTeamPosition = participants.get(1).getAsJsonObject().getAsJsonObject("meta").get("position").getAsInt();

        return new GameInstance(gameName, startTime, venue, homeTeamPosition, awayTeamPosition, homeName, awayName);


    }






    // takes json produced by /v3/football/venues/ and returns the name of the venue, this is necessary because some endpoints have venue ids, but not venue names
    public static String getVenue(String jsonData) {
        Gson gson = new Gson();
        String venueName = "";
        JsonObject dataObject = gson.fromJson(jsonData, JsonObject.class);
        JsonObject data = dataObject.getAsJsonObject("data");

        // Access the venue name from the "data" object
        venueName = data.get("name").getAsString();
        return venueName;

    }

}

