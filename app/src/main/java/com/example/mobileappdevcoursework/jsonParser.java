package com.example.mobileappdevcoursework;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class jsonParser {

    //method that takes json produced by the endpoint /v3/football/fixtures/ and formats it into a List of type Game, this is used in HomeViewModel to fill the recyclerView in the HomeFragment
    public static List<Game> parseJson(String jsonString) {
        List<Game> games = new ArrayList<>();

        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement root = jsonParser.parse(jsonString);

            if (root.isJsonObject()) {
                JsonObject jsonObject = root.getAsJsonObject();
                JsonArray dataArray = jsonObject.getAsJsonArray("data");

                for (JsonElement itemElement : dataArray) {
                    JsonObject jsonItem = itemElement.getAsJsonObject();

                    int gameID = jsonItem.get("id").getAsInt();
                    String gameName = jsonItem.get("name").getAsString();
                    String startTime = jsonItem.get("starting_at").getAsString();

                    Game game = new Game();
                    game.setGameID(gameID);
                    game.setGameName(gameName);
                    game.setStartTime(startTime);


                    games.add(game);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return games;
    }

//    public static List<LiveGame> parseLiveJson(String jsonString) {
//        List<LiveGame> LiveGames = new ArrayList<>();
//
//        try {
//            JsonParser jsonParser = new JsonParser();
//            JsonElement root = jsonParser.parse(jsonString);
//
//            if (root.isJsonObject()) {
//                JsonObject jsonObject = root.getAsJsonObject();
//                JsonArray dataArray = jsonObject.getAsJsonArray("data");
//
//                for (JsonElement itemElement : dataArray) {
//                    JsonObject jsonItem = itemElement.getAsJsonObject();
//
//                    int gameID = jsonItem.get("id").getAsInt();
//                    String gameName = jsonItem.get("name").getAsString();
//                    String startTime = jsonItem.get("starting_at").getAsString();
//                    // ... other fields
//
//                    LiveGame game = new LiveGame(gameName, startTime, gameID);
//
//                    // ... set other fields
//
//                    LiveGames.add(game);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return LiveGames;
//    }


    //method that takes json produced by the endpoint /v3/football/livescores/ and formats it into a List of type Game, this is used in LiveViewModel to fill the recyclerView in the live scores fragment, as well as the live details page, and the mainactivity, as it is used to produce notifications
    public static List<LiveGame> parseLiveJson(String jsonString) {
        List<LiveGame> liveGames = new ArrayList<>();
        String lastResult = "0-0"; //last result is the current score. if an event is a goal it will contain the score, however if it isnt a goal result will be null, lastresult is used to track the score so that it can be displayed on all notifications, not just goals


        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement root = jsonParser.parse(jsonString);

            if (root.isJsonObject()) {
                JsonObject jsonObject = root.getAsJsonObject();
                // System.out.println(jsonObject);
                JsonArray dataArray = jsonObject.getAsJsonArray("data");
                if (dataArray != null) {
                    for (JsonElement dataElement : dataArray) {
                        JsonObject dataObject = dataElement.getAsJsonObject();
                        JsonArray eventsArray = dataObject.getAsJsonArray("events");
                        //System.out.println("          here          " + eventsArray);
                        List<Event> events = new ArrayList<>();
                        for (JsonElement eventElement : eventsArray) {
                            JsonObject eventObject = eventElement.getAsJsonObject();
                            //System.out.println(eventElement.toString());
                            int eventId = eventObject.getAsJsonPrimitive("id").getAsInt();
                            String eventName = "";
                            //JsonElement infoElement = eventObject.get("info");
                            //converting event ids to their equivalent values, which can be found by calling the endpoint /v3/core/types
                            if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 18) {
                                eventName = "Substitution";
                            } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 19) {
                                eventName = "Yellow Card " + eventObject.getAsJsonPrimitive("player_name").getAsString();
                            } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 14) {
                                eventName = "Goal " + eventObject.getAsJsonPrimitive("player_name").getAsString();
                            } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 16) {
                                eventName = "(P) Goal " + eventObject.getAsJsonPrimitive("player_name").getAsString(); // penalty goal
                            } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 17) {
                                eventName = "(P) Miss " + eventObject.getAsJsonPrimitive("player_name").getAsString(); //penalty miss
                            } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 15) {
                                eventName = "OG " + eventObject.getAsJsonPrimitive("player_name").getAsString(); //own goal. not sure how the api classes the team id for this, so it might appear on the wrong side
                            } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 20 || eventObject.getAsJsonPrimitive("type_id").getAsInt() == 21) {//20 is straight red, 21 is second yellow so both have same outcome
                                eventName = "red card " + eventObject.getAsJsonPrimitive("player_name").getAsString();
                            }
                            //18 sub
                            //19 yellow
                            //16 goal
//                    if (infoElement != null && !infoElement.isJsonNull() && infoElement.isJsonPrimitive()) {
//                        eventName = infoElement.getAsString();
//                    }

                            String eventMinute = eventObject.getAsJsonPrimitive("minute").getAsString();
                            JsonElement extraMinuteElement = eventObject.get("extra_minute");

                            if (extraMinuteElement != null && !extraMinuteElement.isJsonNull()) {
                                eventMinute += " + " + extraMinuteElement.getAsJsonPrimitive().getAsString(); // if extra minute isn't null the game is in injury time, and so the minute field gets combined with the extra_minute field to get the overall minute (i.e. 45+2)
                            }
                            String result = "";
                            JsonElement resultElement = eventObject.get("result");

                            if (resultElement != null && resultElement.isJsonPrimitive()) {
                                result = resultElement.getAsJsonPrimitive().getAsString();
                                if (result != null) {
                                    lastResult = result;  // Update lastResult when result is non-null
                                }
                            }

                            String addition = "";
                            JsonElement additionElement = eventObject.get("addition");

                            if (additionElement != null && additionElement.isJsonPrimitive()) {
                                addition = additionElement.getAsJsonPrimitive().getAsString();
                            }

                            int participantId = eventObject.getAsJsonPrimitive("participant_id").getAsInt();
                            String team = "";  // Initialize team to empty string

                            if (eventName != "") {
//                        if (participantId == homeTeamId) {
//                            team = "home";
//                            System.out.println("home");
//                        } else if (participantId == awayTeamId) {
//                            team = "away";
//                            System.out.println("away");
//                        }

                                events.add(new Event(eventId, eventName, eventMinute, result, addition, team));

                            }
                        }
                    }
                }else {
                    Log.e("jsonParser", "dataArray is null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return liveGames;
    }

    private static List<Event> parseEvents(JsonArray eventsArray) {
//        List<Event> events = new ArrayList<>();
//
//        for (JsonElement eventElement : eventsArray) {
//            JsonObject eventObject = eventElement.getAsJsonObject();
//            int eventId = eventObject.getAsJsonPrimitive("id").getAsInt();
//            String eventName = eventObject.getAsJsonPrimitive("type").getAsString();
//            String eventMinute = eventObject.getAsJsonPrimitive("minute").getAsString();
//            String addition = "";
//            JsonElement additionElement = eventObject.get("addition");
//
//            if (additionElement != null && additionElement.isJsonPrimitive()) {
//                addition = additionElement.getAsJsonPrimitive().getAsString();
//            }
//            String result = "";
//            JsonElement resultElement = eventObject.get("result");
//
//            if (resultElement != null && resultElement.isJsonPrimitive()) {
//                result = resultElement.getAsJsonPrimitive().getAsString();
//                if (result != null) {
//                    lastResult = result;  // Update lastResult when result is non-null
//                }
//            }
//            events.add(new Event(eventId, eventName, eventMinute, "", addition, ""));
//        }
//
//        return events;
        List<Event> events = new ArrayList<>();
        String lastResult = null;


        for (JsonElement eventElement : eventsArray) {
            JsonObject eventObject = eventElement.getAsJsonObject();
            System.out.println(eventElement.toString());
            int eventId = eventObject.getAsJsonPrimitive("id").getAsInt();
            String eventName = "";
            //JsonElement infoElement = eventObject.get("info");
            if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 18) {
                eventName = "Substitution";
            } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 19) {
                eventName = "Yellow Card " + eventObject.getAsJsonPrimitive("player_name").getAsString();
            } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 14) {
                eventName = "Goal " + eventObject.getAsJsonPrimitive("player_name").getAsString();
            } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 16) {
                eventName = "(P) Goal " + eventObject.getAsJsonPrimitive("player_name").getAsString();
            } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 17) {
                eventName = "(P) Miss " + eventObject.getAsJsonPrimitive("player_name").getAsString();
            } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 15) {
                eventName = "OG " + eventObject.getAsJsonPrimitive("player_name").getAsString(); //not sure how the api classes the team id for this, so it might appear on the wrong side
            } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 20 || eventObject.getAsJsonPrimitive("type_id").getAsInt() == 21) {//20 is straight red, 21 is second yellow so both have same outcome
                eventName = "red card " + eventObject.getAsJsonPrimitive("player_name").getAsString();
            }
                    //18 sub
                    //19 yellow
                    //16 goal
//                    if (infoElement != null && !infoElement.isJsonNull() && infoElement.isJsonPrimitive()) {
//                        eventName = infoElement.getAsString();
//                    }

            String eventMinute = eventObject.getAsJsonPrimitive("minute").getAsString();
            JsonElement extraMinuteElement = eventObject.get("extra_minute");

            if (extraMinuteElement != null && !extraMinuteElement.isJsonNull()) {
                eventMinute += " + " + extraMinuteElement.getAsJsonPrimitive().getAsString();
            }

            int team = eventObject.getAsJsonPrimitive("participant_id").getAsInt();
            JsonElement teamElement = eventObject.get("participant_id");
            String teamName = "";




            try{

                //denmark 1
                URL url = new URL("https://api.sportmonks.com/v3/football/teams/" + team + "?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr");
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
                if(content != null){
                    String jsonString = content.toString();
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
                    teamName = jsonObject.getAsJsonObject("data").get("name").getAsString();
                }




            } catch (Exception e) {
                e.printStackTrace();
            }





            if (teamElement != null && !teamElement.isJsonNull()) {

            }


            String result = "";
            JsonElement resultElement = eventObject.get("result");

            if (resultElement != null && resultElement.isJsonPrimitive()) {
                result = resultElement.getAsJsonPrimitive().getAsString();
                if (result != null) {
                    lastResult = result;  // Update lastResult when result is non-null

                }else{
                    result = lastResult;
                }
            }

                    String addition = "";
                    JsonElement additionElement = eventObject.get("addition");

                    if (additionElement != null && additionElement.isJsonPrimitive()) {
                        addition = additionElement.getAsJsonPrimitive().getAsString();
                    }



                    events.add(new Event(eventId, eventName, eventMinute, result, addition, teamName));


        }
        return events;
    }


    //used for game details since the game objects that get stored in the database dont have all the details needed.
    public static gameInstance parseGame(String jsonString) {
        Gson gson = new Gson();
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonObject dataObject = jsonObject.getAsJsonObject("data");

        String gameName = dataObject.get("name").getAsString();
        String startTime = dataObject.get("starting_at").getAsString();
        String venue = dataObject.get("venue_id").getAsString();
        JsonArray participants = dataObject.getAsJsonArray("participants");

        String homeName = participants.get(0).getAsJsonObject().get("name").getAsString();
        String awayName = participants.get(1).getAsJsonObject().get("name").getAsString();
        int homeTeamPosition = participants.get(0).getAsJsonObject().getAsJsonObject("meta").get("position").getAsInt();
        int awayTeamPosition = participants.get(1).getAsJsonObject().getAsJsonObject("meta").get("position").getAsInt();

        return new gameInstance(gameName, startTime, venue, homeTeamPosition, awayTeamPosition, homeName, awayName);


    }





//    public static LiveGameInstance parseLiveGame(String jsonData, int targetGameId) {
//        Gson gson = new Gson();
//        JsonObject dataObject = gson.fromJson(jsonData, JsonObject.class);
//        JsonArray dataArray = dataObject.getAsJsonArray("data");
//
//        for (JsonElement element : dataArray) {
//            JsonObject gameObject = element.getAsJsonObject();
//            JsonElement idElement = gameObject.get("id");
//            int gameId = (idElement != null && idElement.isJsonPrimitive()) ? idElement.getAsJsonPrimitive().getAsInt() : 0;
//
//            if (gameId == targetGameId) {
//                int homeTeamId, awayTeamId = 0;
//                if (gameObject.getAsJsonArray("participants").get(0).getAsJsonObject().getAsJsonObject("meta").getAsJsonPrimitive("location").getAsString() == "home") {
//                    homeTeamId = gameObject.getAsJsonArray("participants").get(0).getAsJsonObject().getAsJsonPrimitive("id").getAsInt();
//                    awayTeamId = gameObject.getAsJsonArray("participants").get(1).getAsJsonObject().getAsJsonPrimitive("id").getAsInt();
//
//                } else {
//                    homeTeamId = gameObject.getAsJsonArray("participants").get(1).getAsJsonObject().getAsJsonPrimitive("id").getAsInt();
//                    awayTeamId = gameObject.getAsJsonArray("participants").get(0).getAsJsonObject().getAsJsonPrimitive("id").getAsInt();
//
//                }
//                System.out.println("particiapnt " + gameObject.getAsJsonArray("participants"));
//                String gameName = gameObject.getAsJsonPrimitive("name").getAsString();
//                String startTime = gameObject.getAsJsonPrimitive("starting_at").getAsString();
//                int venueId = gameObject.getAsJsonPrimitive("venue_id").getAsInt();
//
//                JsonObject team1 = gameObject.getAsJsonArray("participants").get(0).getAsJsonObject();
//                JsonObject team2 = gameObject.getAsJsonArray("participants").get(1).getAsJsonObject();
//
//                String team1Name = team1.getAsJsonPrimitive("name").getAsString();
//                String team2Name = team2.getAsJsonPrimitive("name").getAsString();
//
//                JsonElement team1PositionElement = team1.getAsJsonObject("meta").get("position");
//                JsonElement team2PositionElement = team2.getAsJsonObject("meta").get("position");
//
//                int team1Position = team1PositionElement != null && team1PositionElement.isJsonPrimitive()
//                        ? team1PositionElement.getAsJsonPrimitive().getAsInt()
//                        : 0;
//
//                int team2Position = team2PositionElement != null && team2PositionElement.isJsonPrimitive()
//                        ? team2PositionElement.getAsJsonPrimitive().getAsInt()
//                        : 0;
//
//                JsonArray eventsArray = gameObject.getAsJsonArray("events");
//                List<Event> events = new ArrayList<>();
//                String lastResult = null;  // Initialize lastResult to null
//
//                for (JsonElement eventElement : eventsArray) {
//                    JsonObject eventObject = eventElement.getAsJsonObject();
//                    System.out.println(eventElement.toString());
//                    int eventId = eventObject.getAsJsonPrimitive("id").getAsInt();
//                    String eventName = "";
//                    //JsonElement infoElement = eventObject.get("info");
//                    if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 18) {
//                        eventName = "Substitution";
//                    } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 19) {
//                        eventName = "Yellow Card " + eventObject.getAsJsonPrimitive("player_name").getAsString();
//                    } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 14) {
//                        eventName = "Goal " + eventObject.getAsJsonPrimitive("player_name").getAsString();
//                    } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 16) {
//                        eventName = "(P) Goal " + eventObject.getAsJsonPrimitive("player_name").getAsString();
//                    } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 17) {
//                        eventName = "(P) Miss " + eventObject.getAsJsonPrimitive("player_name").getAsString();
//                    } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 15) {
//                        eventName = "OG " + eventObject.getAsJsonPrimitive("player_name").getAsString(); //not sure how the api classes the team id for this, so it might appear on the wrong side
//                    } else if (eventObject.getAsJsonPrimitive("type_id").getAsInt() == 20 || eventObject.getAsJsonPrimitive("type_id").getAsInt() == 21) {//20 is straight red, 21 is second yellow so both have same outcome
//                        eventName = "red card " + eventObject.getAsJsonPrimitive("player_name").getAsString();
//                    }
//                    //18 sub
//                    //19 yellow
//                    //16 goal
////                    if (infoElement != null && !infoElement.isJsonNull() && infoElement.isJsonPrimitive()) {
////                        eventName = infoElement.getAsString();
////                    }
//
//                    String eventMinute = eventObject.getAsJsonPrimitive("minute").getAsString();
//                    JsonElement extraMinuteElement = eventObject.get("extra_minute");
//
//                    if (extraMinuteElement != null && !extraMinuteElement.isJsonNull()) {
//                        eventMinute += " + " + extraMinuteElement.getAsJsonPrimitive().getAsString();
//                    }
//                    String result = "";
//                    JsonElement resultElement = eventObject.get("result");
//
//                    if (resultElement != null && resultElement.isJsonPrimitive()) {
//                        result = resultElement.getAsJsonPrimitive().getAsString();
//                        if (result != null) {
//                            lastResult = result;  // Update lastResult when result is non-null
//                        }
//                    }
//
//                    String addition = "";
//                    JsonElement additionElement = eventObject.get("addition");
//
//                    if (additionElement != null && additionElement.isJsonPrimitive()) {
//                        addition = additionElement.getAsJsonPrimitive().getAsString();
//                    }
//
//                    int participantId = eventObject.getAsJsonPrimitive("participant_id").getAsInt();
//                    String team = "";  // Initialize team to empty string
//
//                    if (eventName != "") {
//                        if (participantId == homeTeamId) {
//                            team = "home";
//                            System.out.println("home");
//                        } else if (participantId == awayTeamId) {
//                            team = "away";
//                            System.out.println("away");
//                        }
//
//                        events.add(new Event(eventId, eventName, eventMinute, result, addition, team));
//
//                    }
//                }
//
//                return new LiveGameInstance(gameName, startTime, venueId, team1Name, team2Name, team1Position, team2Position, events, lastResult);
//            }
//        }
//
//        return null;
//    }

    // takes json produced by /v3/football/venues/ and returns the name of the venue, this is necessary because some endpoints have venue ids, but not venue names
    public static String getVenue(String jsonData) {
        System.out.println("getvenue ");
        Gson gson = new Gson();
        String venueName = "";
        JsonObject dataObject = gson.fromJson(jsonData, JsonObject.class);
        JsonObject data = dataObject.getAsJsonObject("data");

        // Access the venue name from the "data" object
        venueName = data.get("name").getAsString();
        System.out.println(venueName);
        return venueName;

    }

    public static int countEvents(){ // dont call this??? delete sometime soon
        return 2;
    }
}
//     {"data":[{"id":18862014,"sport_id":1,"league_id":501,"season_id":21787,"stage_id":77464483,"group_id":null,"aggregate_id":null,"round_id":309106,"state_id":5,"venue_id":8879,"name":"St. Mirren vs Livingston","starting_at":"2023-11-25 15:00:00","result_info":"St. Mirren won after full-time.","leg":"1\/1","details":null,"length":90,"placeholder":false,"has_odds":true,"starting_at_timestamp":1700924400,"events":[{"id":92920109,"fixture_id":18862014,"period_id":5129112,"participant_id":496,"type_id":15,"section":"event","player_id":9597,"related_player_id":null,"player_name":"Sean Kelly","related_player_name":null,"result":"1-0","info":null,"addition":"1st Goal","minute":37,"extra_minute":null,"injured":null,"on_bench":false,"coach_id":null,"sub_type_id":null},{"id":92927217,"fixture_id":18862014,"period_id":5129466,"participant_id":496,"type_id":18,"section":"event","player_id":37547459,"related_player_id":430377,"player_name":"Alex Greive","related_player_name":"Jonah Ayunga","result":null,"info":null,"addition":null,"minute":60,"extra_minute":null,"injured":false,"on_bench":false,"coach_id":null,"sub_type_id":1523},{"id":92928180,"fixture_id":18862014,"period_id":5129466,"participant_id":258,"type_id":18,"section":"event","player_id":5105,"related_player_id":9597,"player_name":"Tom Parkes","related_player_name":"Sean Kelly","result":null,"info":null,"addition":null,"minute":64,"extra_minute":null,"injured":false,"on_bench":false,"coach_id":null,"sub_type_id":1523},{"id":92928181,"fixture_id":18862014,"period_id":5129466,"participant_id":258,"type_id":18,"section":"event","player_id":3861331,"related_player_id":2827,"player_name":"Mo Sangare","related_player_name":"Andrew Shinnie","result":null,"info":null,"addition":null,"minute":64,"extra_minute":null,"injured":true,"on_bench":false,"coach_id":null,"sub_type_id":1523},{"id":92930025,"fixture_id":18862014,"period_id":5129466,"participant_id":496,"type_id":18,"section":"event","player_id":3283,"related_player_id":37285031,"player_name":"Charles Dunne","related_player_name":"Richard Taylor","result":null,"info":null,"addition":null,"minute":72,"extra_minute":null,"injured":true,"on_bench":false,"coach_id":null,"sub_type_id":1524},{"id":92934779,"fixture_id":18862014,"period_id":5129466,"participant_id":496,"type_id":18,"section":"event","player_id":14603,"related_player_id":6972189,"player_name":"James Bolton","related_player_name":"Caolan Boyd-Munce","result":null,"info":null,"addition":null,"minute":87,"extra_minute":null,"injured":false,"on_bench":false,"coach_id":null,"sub_type_id":1523},{"id":92934778,"fixture_id":18862014,"period_id":5129466,"participant_id":496,"type_id":10,"section":"event","player_id":5105,"related_player_id":null,"player_name":"Tom Parkes","related_player_name":null,"result":null,"info":"Offside","addition":"Goal Disallowed","minute":84,"extra_minute":null,"injured":null,"on_bench":false,"coach_id":null,"sub_type_id":null},{"id":92932980,"fixture_id":18862014,"period_id":5129466,"participant_id":258,"type_id":18,"section":"event","player_id":378771,"related_player_id":174211,"player_name":"Stephen Kelly","related_player_name":"Scott Pitman","result":null,"info":null,"addition":null,"minute":82,"extra_minute":null,"injured":false,"on_bench":false,"coach_id":null,"sub_type_id":1523},{"id":92935552,"fixture_id":18862014,"period_id":5129466,"participant_id":496,"type_id":19,"section":"event","player_id":9781,"related_player_id":null,"player_name":"Alex Gogic","related_player_name":null,"result":null,"info":"Foul","addition":null,"minute":90,"extra_minute":1,"injured":null,"on_bench":false,"coach_id":null,"sub_type_id":1496},{"id":92935230,"fixture_id":18862014,"period_id":5129466,"participant_id":496,"type_id":18,"section":"event","player_id":24445765,"related_player_id":4417,"player_name":"Stav Nachmani","related_player_name":"Mikael Mandron","result":null,"info":null,"addition":null,"minute":87,"extra_minute":null,"injured":false,"on_bench":false,"coach_id":null,"sub_type_id":1523},{"id":92937812,"fixture

