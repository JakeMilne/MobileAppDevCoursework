package com.example.mobileappdevcoursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.*;
import com.example.mobileappdevcoursework.databinding.ActivityMainBinding;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //code for the navigation bar comes from https://www.youtube.com/watch?v=jOFLmKMOcK0 , accessed 14/11/2023 at 10am the switch case at 7:54 was adapted to a series of if else statements to fix a constant expression required error
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new home());



        binding.bottomNavigationView.setOnItemReselectedListener(item -> {
            if (item.getItemId() == R.id.main) {
                replaceFragment(new home());
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new userProfile());
            } else if (item.getItemId() == R.id.live) {
                replaceFragment(new liveScores());
            }


        });


    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

    }
}

/*
import java.net.*;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        String apiKey = "vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr";
        //mainSearch(apiKey);
        test(apiKey, 0);

    }
    public String getMatch(String apiKey, int matchID){
        String dataObject = "hi";

        String baseurl = "https://api.sportmonks.com/v3/football/fixtures/";
        String addons = matchID + "&include=statistics;lineups.details?api_token=" + apiKey;

        //call to get lineups
        //for each player on each team get formation_position and player_name, put them into an array with name at position formation_position
        //sort array
        //print output as table for now. 
        return dataObject;
    }
    public static String test(String apiKey, int matchID){
        String dataObject = "hi";
        
        String stringy = "{\"data\":[{\"id\":18862856,\"sport_id\":1,\"league_id\":501,\"season_id\":21787,\"stage_id\":77464483,\"group_id\":null,\"aggregate_id\":null,\"round_id\":309103,\"state_id\":2,\"venue_id\":8946,\"name\":\"Hibernian vs Ross County\",\"starting_at\":\"2023-10-31 19:45:00\",\"result_info\":null,\"leg\":\"1\\/1\",\"details\":null,\"length\":90,\"placeholder\":false,\"has_odds\":true,\"starting_at_timestamp\":1698781500,\"events\":[],\"participants\":[{\"id\":246,\"sport_id\":1,\"country_id\":1161,\"venue_id\":8908,\"gender\":\"male\",\"name\":\"Ross County\",\"short_code\":\"RSC\",\"image_path\":\"https://cdn.sportmonks.com/images/soccer/teams/22/246.png\",\"founded\":1929,\"type\":\"domestic\",\"placeholder\":false,\"last_played_at\":\"2023-10-28 14:00:00\",\"meta\":{\"location\":\"away\",\"winner\":null,\"position\":11}},{\"id\":66,\"sport_id\":1,\"country_id\":1161,\"venue_id\":8946,\"gender\":\"male\",\"name\":\"Hibernian\",\"short_code\":\"HIB\",\"image_path\":\"https://cdn.sportmonks.com/images/soccer/teams/2/66.png\",\"founded\":1875,\"type\":\"domestic\",\"placeholder\":false,\"last_played_at\":\"2023-10-28 14:00:00\",\"meta\":{\"location\":\"home\",\"winner\":null,\"position\":8}}]}],\"subscription\":[{\"meta\":[],\"plans\":[{\"plan\":\"Football Free Plan\",\"sport\":\"Football\",\"category\":\"Standard\"},{\"plan\":\"Cricket Free Plan\",\"sport\":\"Cricket\",\"category\":\"Standard\"}],\"add_ons\":[],\"widgets\":[]}],\"rate_limit\":{\"resets_in_seconds\":3513,\"remaining\":2997,\"requested_entity\":\"Fixture\"},\"timezone\":\"UTC\"}";

        StringBuilder content = new StringBuilder();
        content.append(stringy);

        String jsonString = content.toString();
        //jsonString = jsonString.replaceAll("//[^\\n]*", "");

        //System.out.println(jsonString);
        JsonArray jsonArray = JsonParser.parseString(stringy).getAsJsonObject().getAsJsonArray("data");

        
        JsonArray participantsArray = jsonArray.get(0).getAsJsonObject().getAsJsonArray("participants");
        // 
        //System.out.println(jsonArray.get(0).getAsJsonObject().get("name").getAsString()  + " vs " + jsonArray.get(1).getAsJsonObject().get("name").getAsString());
        System.out.println(participantsArray.get(0).getAsJsonObject().get("name").getAsString() +" vs " +  participantsArray.get(1).getAsJsonObject().get("name").getAsString());
        System.out.println("the score is ");
        return dataObject;
    }
    public static String mainSearch(String apiKey){
        try{
            
            //URL url = new URL("https://api.sportmonks.com/v3/football/fixtures/18535517?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events"); // Replace endpoint with the specific endpoint you want to access
            
            
            //URL url = new URL("https://api.sportmonks.com/v3/core/types?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr");
            //URL url = new URL("https://api.sportmonks.com/v3/football/fixtures/18535517?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events&filters=eventTypes:11");
            //URL url = new URL("https://api.sportmonks.com/v3/football/fixtures/18535517?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;statistics.type&filters=eventTypes:18,14;fixtureStatisticTypes:45");
            
            //URL url = new URL("https://api.sportmonks.com/v3/football/fixtures/18535517?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=statistics.type&filters=fixtureStatisticTypes:45");
            URL url =  new URL("https://api.sportmonks.com/v3/football/livescores/inplay?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:501");
            
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestMethod("GET");
            //connection.setRequestProperty("Api-Token", apiKey);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                //System.out.println(inputLine.toString());
                content.append(inputLine);
            }
            
            String jsonString = content.toString();

            

            in.close();
            connection.disconnect();

            jsonString = jsonString.replaceAll("//[^\\n]*", "");
            //JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            //System.out.println(jsonObject.getAsJsonObject("data").getAsJsonArray("statistics").get(0));
            //System.out.println(jsonObject.getAsJsonObject("data").getAsJsonArray("statistics").get(1));
            //JsonArray eventsArray = jsonObject.getAsJsonObject("data").getAsJsonArray("events");

            //JsonElement dataElement = jsonObject.get("data");
           // JsonObject dataObject = dataElement.getAsJsonObject();

            //for (String key : dataObject.keySet()) {
             //   JsonElement value = dataObject.get(key);
                //System.out.println(key + ": " + value);
            //}
            //System.out.println(dataObject.get("name"));
            //System.out.println(content.toString());
            System.out.println(jsonString);
            //{"data":[{"id":18862856,"sport_id":1,"league_id":501,"season_id":21787,"stage_id":77464483,"group_id":null,"aggregate_id":null,"round_id":309103,"state_id":2,"venue_id":8946,"name":"Hibernian vs Ross County","starting_at":"2023-10-31 19:45:00","result_info":null,"leg":"1\/1","details":null,"length":90,"placeholder":false,"has_odds":true,"starting_at_timestamp":1698781500,"events":[],"participants":[{"id":246,"sport_id":1,"country_id":1161,"venue_id":8908,"gender":"male","name":"Ross County","short_code":"RSC","image_path":"https:\/\/cdn.sportmonks.com\/images\/soccer\/teams\/22\/246.png","founded":1929,"type":"domestic","placeholder":false,"last_played_at":"2023-10-28 14:00:00","meta":{"location":"away","winner":null,"position":11}},{"id":66,"sport_id":1,"country_id":1161,"venue_id":8946,"gender":"male","name":"Hibernian","short_code":"HIB","image_path":"https:\/\/cdn.sportmonks.com\/images\/soccer\/teams\/2\/66.png","founded":1875,"type":"domestic","placeholder":false,"last_played_at":"2023-10-28 14:00:00","meta":{"location":"home","winner":null,"position":8}}]}],"subscription":[{"meta":[],"plans":[{"plan":"Football Free Plan","sport":"Football","category":"Standard"},{"plan":"Cricket Free Plan","sport":"Cricket","category":"Standard"}],"add_ons":[],"widgets":[]}],"rate_limit":{"resets_in_seconds":3513,"remaining":2997,"requested_entity":"Fixture"},"timezone":"UTC"}
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "hi";
    }
    public static String playerSearch(){

        try {
            URL url =  new URL("https://api.sportmonks.com/v3/football/players/search/James Forrest?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr");
            //https://api.sportmonks.com/v3/football/livescores/
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestMethod("GET");
            //connection.setRequestProperty("Api-Token", apiKey);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                //System.out.println(inputLine.toString());
                content.append(inputLine);
            }
            
            String jsonString = content.toString();
            System.out.println(jsonString);
            

            in.close();
            connection.disconnect();

            jsonString = jsonString.replaceAll("//[^\\n]*", "");
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

            JsonElement dataElement = jsonObject.get("data");
            JsonObject dataObject = dataElement.getAsJsonObject();

            for (String key : dataObject.keySet()) {
                JsonElement value = dataObject.get(key);
                //System.out.println(key + ": " + value);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            
        
        return "hi";
    }
}
*/
//https://docs.sportmonks.com/football/endpoints-and-entities/endpoints/players/get-players-by-search-by-name
//https://docs.sportmonks.com/football/tutorials-and-guides/guides/how-to-build-a-match-page
//https://docs.sportmonks.com/football/api/request-options/filtering
//https://docs.sportmonks.com/football/welcome/what-can-you-do-with-sportmonks-data
//https://docs.sportmonks.com/football/api/request-options
//
//
//
//
//
//
//{"data":[{"id":18862856,"sport_id":1,"league_id":501,"season_id":21787,"stage_id":77464483,"group_id":null,"aggregate_id":null,"round_id":309103,"state_id":2,"venue_id":8946,"name":"Hibernian vs Ross County","starting_at":"2023-10-31 19:45:00","result_info":null,"leg":"1\/1","details":null,"length":90,"placeholder":false,"has_odds":true,"starting_at_timestamp":1698781500,"events":[],"participants":[{"id":246,"sport_id":1,"country_id":1161,"venue_id":8908,"gender":"male","name":"Ross County","short_code":"RSC","image_path":"https:\/\/cdn.sportmonks.com\/images\/soccer\/teams\/22\/246.png","founded":1929,"type":"domestic","placeholder":false,"last_played_at":"2023-10-28 14:00:00","meta":{"location":"away","winner":null,"position":11}},{"id":66,"sport_id":1,"country_id":1161,"venue_id":8946,"gender":"male","name":"Hibernian","short_code":"HIB","image_path":"https:\/\/cdn.sportmonks.com\/images\/soccer\/teams\/2\/66.png","founded":1875,"type":"domestic","placeholder":false,"last_played_at":"2023-10-28 14:00:00","meta":{"location":"home","winner":null,"position":8}}]}],"subscription":[{"meta":[],"plans":[{"plan":"Football Free Plan","sport":"Football","category":"Standard"},{"plan":"Cricket Free Plan","sport":"Cricket","category":"Standard"}],"add_ons":[],"widgets":[]}],"rate_limit":{"resets_in_seconds":3513,"remaining":2997,"requested_entity":"Fixture"},"timezone":"UTC"}
            
