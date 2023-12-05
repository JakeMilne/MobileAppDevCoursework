package com.example.mobileappdevcoursework;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

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
    private static final String TAG = "Notifications";


    public Notifications(Context context, DatabaseRepository databaseRepository) {
        this.context = context;
        this.databaseRepository = databaseRepository;
    }

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    @Override // https://chat.openai.com/share/44cc401f-7c49-4484-830b-f51375a05d0a
    public void run() {
        while (true) {
            List<FollowedGame> followedGames = databaseRepository.getAllFollowed();
            Map<Integer, Integer> followedGamesMap = convertListToMap(followedGames); //storing the games in a map with gameID being the key, and eventcount being the value. This should make it faster to access the data
            List<LiveGame> liveGames = this.notificationSearch();
            boolean shouldSleep = false;
            if(followedGamesMap == null || liveGames == null){ //if the user doesnt have any games followed. or there arent any live games, shouldSleep is true
                shouldSleep = true;
            } else{
                //logic to check for + send notifications


                for (LiveGame game : liveGames) {
                    if (followedGamesMap.get(game.getId()) != null) {
                        //getting event count for the live game, and the event count from the last time the match was checked for events
                        int newCount = game.getEvents().size();
                        int oldCount = followedGamesMap.get(game.getId());

                        if (newCount > oldCount) { //if newCount is > oldCount an event has occurred, so a notification should be sent
                            for (int i = oldCount; i < newCount; i++) { //gets the new events using game.getEventAtIndex(i) and sends them
                                if (i < game.getEvents().size()) {
                                    Event event = game.getEventAtIndex(i);
                                    sendNotif(event, game.getId(), game.leagueId);
                                }
                            }
                            FollowedGame followedGame = new FollowedGame(game.getId(), newCount);
                            databaseRepository.updateFollowedGames(followedGame); //updates the database with the new count
                        }
                    }
                }

                for (Map.Entry<Integer, Integer> entry : followedGamesMap.entrySet()){ //https://chat.openai.com/share/99c6ed7e-ed94-4ab5-a15a-3c708848e7bb
                    Integer gameId = entry.getKey();

                    // Check if the game with gameId is in liveGames
                    boolean isGameInLiveGames = liveGames.stream().anyMatch(game -> game.getId() == gameId);

                    if (!isGameInLiveGames) {
                        // if the followed game isn't in liveGames, it gets deleted
                        databaseRepository.deleteFollowedGame(gameId);
                    }
                }
            }


            if (shouldSleep) {
                try {
                    // Sleep for an extra minute if the there aren't any games to track
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    // Sleep for the specified interval before the next execution, this could be lowered to receive notifications more often
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    //method to convert the list of games to a map.
    private static Map<Integer, Integer> convertListToMap(List<FollowedGame> followedGamesList) {
        Map<Integer, Integer> gameIdToEventCountMap = new HashMap<>();
        for (FollowedGame game : followedGamesList) {
            gameIdToEventCountMap.put(game.getGameID(), game.getEventCount());
        }
        return gameIdToEventCountMap;
    }

    public void sendNotif(Event event, int id, int leagueId) {
        //ANDROID DEVELOPERS, 2023. Create a notification. [online]. Palo Alto, California: Android. Available from: https://developer.android.com/develop/ui/views/notifications/build-notification#java [Accessed 1 December 2023].


        // https://chat.openai.com/share/b2695ba0-3441-4722-9de4-665c551640c7
        final String CHANNEL_ID = "YouScore";
        List<String> notif = new ArrayList<>();
        notif = parseForNotif(event);
        String textTitle = notif.get(0);
        String textContent = notif.get(1);
        Context context = this.context;
        // Use the hosting Activity class instead of LiveGameDetails
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("FRAGMENT_TYPE", "LiveGameDetails"); //directs the user to LiveGameDetails if they click the notification
        intent.putExtra("ITEM_ID", id); //game id of the game the notification refers to
        intent.putExtra("LEAGUE_ID", leagueId); //league id of the game the notification refers to

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        //same icon as LiveScores on the navbar
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_live_tv_24)
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
        Log.d(TAG, "Notification sent");
    }

    //psuedo random number for the notification id, so that if there's a duplicate notification it replaces the old one
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
    //formatting text for the notification
    public static List<String> parseForNotif(Event event){
        List<String> notif = new ArrayList<>();

        notif.add(event.getMinute() + "' "  + event.getName() + " " + event.getTeam() + " " + event.getResult());
        notif.add(event.getName() + " for " + event.getTeam() + " at minute " + event.getMinute() + "' "  +  " the score is: " + event.getResult());

        return notif;
    }

    //search live games to get a list of LiveGames
    public List<LiveGame> notificationSearch() {
        List<LiveGame> liveGames = new ArrayList<>();
        int leagueID = databaseRepository.getLeague();
        //I have realised on the day before the
        // deadline that this bit renders passing league id through notifications into LiveGameDetails Obsolete,
        //I don't have the opportunity to test this but it might be possible to either change the url to have all the leagues
        //(like having fixtureLeagues:501,271,513,1659 on the end of the url instead of fixtureLeagues:" + leagueID)) or change
        // the FollowedGame database to also store leagueids, change the part with the map
        // and then get all unique league ids from the database and search that way
        //I know you can search multiple leagues at once  on the fixtures endpoint, so I assume would work here on the livescores
        //endpoint, but I can't test it and I am scared of breaking it.

        try {
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

                if (jsonString.contains("\"message\":\"No result(s) found matching your request.")) { //if there aren't any live games this is the start of the message the API returns
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

