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
            Map<Integer, Integer> followedGamesMap = convertListToMap(followedGames);
            List<LiveGame> liveGames = this.notificationSearch();
            boolean shouldSleep = false;
            if(followedGamesMap == null){
                shouldSleep = true;
            } else{
                //logic to send notifications
                //for game in followed game
//                int oldCount = databaseRepository.getEventCount() // followed game id

                for (LiveGame game : liveGames) {
                    if (followedGamesMap.get(game.getId()) != null) {
                        int newCount = game.getEvents().size();
                        int oldCount = followedGamesMap.get(game.getId());
//                        Log.d(TAG, String.valueOf(newCount));
//                        Log.d(TAG, String.valueOf(oldCount));
                        if (newCount > oldCount) {
                            for (int i = oldCount; i < newCount; i++) {
                                if (i < game.getEvents().size()) {
                                    Event event = game.getEventAtIndex(i);
                                    sendNotif(event, game.getId(), game.leagueId);
                                }
                            }
                            FollowedGame followedGame = new FollowedGame(game.getId(), newCount);
                            databaseRepository.updateFollowedGames(followedGame);
                        }
                    }
                }

                for (Map.Entry<Integer, Integer> entry : followedGamesMap.entrySet()){ //https://chat.openai.com/share/99c6ed7e-ed94-4ab5-a15a-3c708848e7bb
                    Integer gameId = entry.getKey();

                    // Check if the game with gameId is in liveGames
                    boolean isGameInLiveGames = liveGames.stream().anyMatch(game -> game.getId() == gameId);

                    if (!isGameInLiveGames) {
                        // Call your method here for games that are in followedGames but not in liveGames
                        databaseRepository.deleteFollowedGame(gameId);
                    }
                }
            }


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
        final String CHANNEL_ID = "YouScore";
        List<String> notif = new ArrayList<>();
        notif = parseForNotif(event);
        String textTitle = notif.get(0);
        String textContent = notif.get(1);
        Context context = this.context;
        // Use the hosting Activity class instead of LiveGameDetails
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("FRAGMENT_TYPE", "LiveGameDetails");
        intent.putExtra("ITEM_ID", id);
        intent.putExtra("LEAGUE_ID", leagueId);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

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


        notif.add(event.getMinute() + "' "  + event.getName() + " " + event.getTeam() + " " + event.getResult());
        notif.add(event.getName() + " for " + event.getTeam() + " at minute " + event.getMinute() + "' "  +  " the score is: " + event.getResult());





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

