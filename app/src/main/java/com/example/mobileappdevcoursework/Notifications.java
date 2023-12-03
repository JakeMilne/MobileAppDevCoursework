package com.example.mobileappdevcoursework;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mobileappdevcoursework.data.DatabaseRepository;
import com.example.mobileappdevcoursework.data.FollowedGame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

class Notifications implements Runnable {

    private Context context;
    private DatabaseRepository databaseRepository;

    public Notifications(Context context, DatabaseRepository databaseRepository) {
        this.context = context;
        this.databaseRepository = databaseRepository;
    }

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    @Override // https://chat.openai.com/share/44cc401f-7c49-4484-830b-f51375a05d0a
    public void run() {
        while (true) {
            long startTime = System.currentTimeMillis();
            Map<Integer, Integer> followedGames = notifCheck();
            List<LiveGame> liveGames = this.notificationSearch();
            boolean shouldSleep = false;
            if(followedGames == null){
                shouldSleep = true;
            } else{
                //logic to send notifications
                //for game in followed game
//                int oldCount = databaseRepository.getEventCount() // followed game id

                for (LiveGame game : liveGames){
                    if(followedGames.get(game.getId()) != null){
                        int newCount = game.getEvents().size();
                        int oldCount = followedGames.get(game.getId());
                        if(newCount > oldCount){
                            for(int i=newCount; i >oldCount; i++){
                                sendNotif(game.getEventAtIndex(i), game.getId(), game.leagueId);
                            }
                        }

                    }

                }

                for (Map.Entry<Integer, Integer> entry : followedGames.entrySet()){ //https://chat.openai.com/share/99c6ed7e-ed94-4ab5-a15a-3c708848e7bb
                    Integer gameId = entry.getKey();

                    // Check if the game with gameId is in liveGames
                    boolean isGameInLiveGames = liveGames.stream().anyMatch(game -> game.getId() == gameId);

                    if (!isGameInLiveGames) {
                        // Call your method here for games that are in followedGames but not in liveGames
                        databaseRepository.deleteFollowedGame(gameId);
                    }
                }
            }
            //boolean shouldSleep = notifCheck(); // change to Map<Integer, Integer> followedGames, and do if !followedGames == null shouldSleep = true
            long endTime = System.currentTimeMillis();
            //System.out.println("Execution time: " + (endTime - startTime) + " ms");

            if (shouldSleep) {
                try {
                    // Sleep for 1 minute if the condition is met
                    System.out.println("Sleep for 1 min");
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    // Sleep for the specified interval before the next execution
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //    private boolean notifCheck() {
    private Map<Integer, Integer> notifCheck() {

        long startTime = System.currentTimeMillis();
        // Your code to be executed every 10 seconds
        try {
            // Perform network-related operation in a background thread
            Thread.sleep(10000);
            List<LiveGame> liveGames = this.notificationSearch();
            if (liveGames == null) {
                System.out.println("Sleep for 1 min");
                Thread.sleep(60000);
                return null; // Return true if you want to sleep for 1 minute
            } else {
                System.out.println(liveGames);
                List<FollowedGame> followedGames = databaseRepository.getAllFollowed();
                Map<Integer, Integer> followedGamesMap = convertListToMap(followedGames);



                System.out.println("Executing myFunction every 10 seconds");
                return followedGamesMap; // Return false if you don't want to sleep for 1 minute
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println("notifCheck execution time: " + (endTime - startTime) + " ms");
        }
    }



    private static Map<Integer, Integer> convertListToMap(List<FollowedGame> followedGamesList) {
        Map<Integer, Integer> gameIdToEventCountMap = new HashMap<>();
        for (FollowedGame game : followedGamesList) {
            gameIdToEventCountMap.put(game.getGameID(), game.getEventCount());
        }
        return gameIdToEventCountMap;
    }

    public void sendNotif(Event event, int id, int leagueId) {
        // https://developer.android.com/develop/ui/views/notifications/build-notification#java
        // https://chat.openai.com/share/b2695ba0-3441-4722-9de4-665c551640c7
        final String CHANNEL_ID = "App_Title_id";
        List<String> notif = new ArrayList<>();
        notif = parseForNotif(event);
        String textTitle = notif.get(0);
        String textContent = notif.get(1);
        Context context = this.context;
        Intent intent = new Intent(context, LiveGameDetails.class);
        intent.putExtra("ITEM_ID", id);
        intent.putExtra("LEAGUE_ID", leagueId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        int notificationId = createID(textTitle);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        //this if statement auto generated by android studio
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the User grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }

    public static int createID(String input) {
        int sum = 0;

        // Check for null or empty string
        if (input != null && !input.isEmpty()) {
            // Iterate through each character in the string
            for (char c : input.toCharArray()) {
                // Add the ASCII value of the character to the sum
                sum += (int) c;
            }
        }

        return sum;
    }
    public static List<String> parseForNotif(Event event){
        List<String> notif = new ArrayList<>();

        //(new Event(eventId, eventName, eventMinute, result, addition, teamName))
        if (event.getAddition().isEmpty()){
            notif.add(event.getMinute() + "' "  + event.getName() + " " + event.getTeam() + " " + event.getResult());

        }else{
            notif.add(event.getMinute() + " + " + event.getAddition() + "' "  + event.getName() + " " + event.getTeam() + " " + event.getResult());
        }
        if (event.getAddition().isEmpty()){
            notif.add(event.getName() + " for  " + event.getTeam() + " at minute " + event.getMinute() + "' "  +  " the score is: " + event.getResult());


        }else{
            notif.add(event.getName() + " for  " + event.getTeam() + " at minute " + event.getMinute() + " + " + event.getAddition() + "' "  +  " the score is: " + event.getResult());

        }



        return notif;
    }

    public List<LiveGame> notificationSearch() {
        List<LiveGame> liveGames = new ArrayList<>();
        int leagueID = databaseRepository.getLeague();

        try {
            //System.out.println(leagueID);
            URL url = new URL("https://api.sportmonks.com/v3/football/livescores/inplay?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:" + leagueID);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            if (content != null) {
                String jsonString = content.toString();
                //System.out.println("here " + jsonString);
                boolean shouldDelay = false;
                if (jsonString.contains("\"message\":\"No result(s) found matching your request.")) {
                    return null;
                }


                liveGames = JsonParse.parseLiveJson(jsonString, leagueID);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return liveGames;
    }
}

