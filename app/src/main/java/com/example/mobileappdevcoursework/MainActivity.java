package com.example.mobileappdevcoursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mobileappdevcoursework.databinding.ActivityMainBinding;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);


        //code for the navigation bar comes from https://www.youtube.com/watch?v=jOFLmKMOcK0 , accessed 14/11/2023 at 10am the switch case at 7:54 was adapted to a series of if else statements to fix a constant expression required error
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        //replaceFragment(new HomeFragment());
        NavController navController = navHostFragment.getNavController();


//        new Thread(() -> {
//            createNotificationChannel();
//            LiveViewModel viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LiveViewModel.class);
//            DatabaseRepository databaseRepository = DatabaseRepository.getRepository(this);
//            Notifications notificationManager = new Notifications(this, viewModel, databaseRepository);
//            notificationManager.run();
//        }).start();


        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.main) {
                navController.navigate(R.id.HomeFragment);
                return true;
            } else if (item.getItemId() == R.id.profile) {
                navController.navigate(R.id.userProfile);
                return true;
            } else if (item.getItemId() == R.id.live) {
                navController.navigate(R.id.liveScores);
                return true;
            }
            return false;
        });


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        final String CHANNEL_ID = "App_Title_id";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void replaceFragment(Fragment fragment) {
        Log.d("MainActivity", "Replacing fragment with " + fragment.getClass().getSimpleName());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();
        } else {
            Log.e("MainActivity", "Fragment is null");
        }

    }
}

class Notifications implements Runnable {

    private Context context;
    private LiveViewModel liveViewModel;
    private DatabaseRepository databaseRepository;

    public Notifications(Context context, LiveViewModel liveViewModel, DatabaseRepository databaseRepository) {
        this.context = context;
        this.liveViewModel = liveViewModel;
        this.databaseRepository = databaseRepository;
    }

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void run() {
        scheduler.scheduleAtFixedRate(() -> {
            long startTime = System.currentTimeMillis();
            notifCheck();
            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + " ms");
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void notifCheck() {
        // Your code to be executed every 10 seconds
        long startTime = System.currentTimeMillis();
        new Thread(() -> {
            try{
            // Perform network-related operation in a background thread
                Thread.sleep(10000);
            List<LiveGame> liveGames = liveViewModel.liveSearch();

            // Continue processing the data in the background thread
            List<FollowedGame> followedGames = databaseRepository.getAllFollowed();
            Map<Integer, Integer> followedGamesMap = convertListToMap(followedGames);
            for (FollowedGame followedGame : followedGames) {
                // Check if the FollowedGame is not in liveGames
                boolean isFollowedGameInLiveGames = false;

                for (LiveGame liveGame : liveGames) {
                    if (liveGame.getId() == followedGame.getGameID()) {
                        isFollowedGameInLiveGames = true;
                        break;
                    }
                }

                // If the FollowedGame is not in liveGames, delete it
                if (!isFollowedGameInLiveGames) {
                    databaseRepository.deleteFollowedGame(followedGame.getGameID());
                }
            }
            for (LiveGame game : liveGames) {
                if (followedGamesMap.get(game.getId()) != null) {
                    if (game.eventCount() > followedGamesMap.get(game.getId())) {
                        for (int i = followedGamesMap.get(game.getId()); i < game.eventCount(); i++) {
                            Event event = game.getEventAtIndex(i);
                            sendNotif(event);
                        }
                    }
                }
            }

            System.out.println("Executing myFunction every 10 seconds");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        long endTime = System.currentTimeMillis();
        System.out.println("notifCheck execution time: " + (endTime - startTime) + " ms");

    }

    // ... rest of the code ...


    private static Map<Integer, Integer> convertListToMap(List<FollowedGame> followedGamesList) {
        Map<Integer, Integer> gameIdToEventCountMap = new HashMap<>();
        for (FollowedGame game : followedGamesList) {
            gameIdToEventCountMap.put(game.getGameID(), game.getEventCount());
        }
        return gameIdToEventCountMap;
    }

    public void sendNotif(Event event) {
        final String CHANNEL_ID = "App_Title_id";
        List<String> notif = new ArrayList<>();
        notif = parseForNotif(event);
        String textTitle = notif.get(0);
        String textContent = notif.get(1);
        Context context = this.context;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(textTitle)
                .setContentText(textContent)
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
            // to handle the case where the user grants the permission. See the documentation
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
}

