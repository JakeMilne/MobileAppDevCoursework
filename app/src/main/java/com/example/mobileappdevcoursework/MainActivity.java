package com.example.mobileappdevcoursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mobileappdevcoursework.data.DatabaseRepository;
import com.example.mobileappdevcoursework.data.User;
import com.example.mobileappdevcoursework.databinding.ActivityMainBinding;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 123; // notification request code
    private DatabaseRepository databaseRepository;
    private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //code for the navigation bar comes from https://www.youtube.com/watch?v=jOFLmKMOcK0 , accessed 14/11/2023 at 10am the switch case at 7:54 was adapted to a series of if else statements to fix a constant expression required error
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        NavController navController = navHostFragment.getNavController();

        if (getIntent() != null) {
            String fragmentType = getIntent().getStringExtra("FRAGMENT_TYPE");

            if (fragmentType != null) {//if the user loads from a notification it sends them to LiveGameDetails, with the gameid and leagueid of the game the notification refers to
                switch (fragmentType) {
                    case "LiveGameDetails":
                        int itemId = getIntent().getIntExtra("ITEM_ID", -1);
                        int leagueId = getIntent().getIntExtra("LEAGUE_ID", -1);

                        Bundle bundle = new Bundle();
                        bundle.putInt("ITEM_ID", itemId);
                        bundle.putInt("LEAGUE_ID", leagueId);

                        navController.navigate(R.id.liveGameDetails, bundle);

                        break;
                }
            }
        }
        checkNotificationPermissions();



        new Thread(() -> {
            databaseRepository = DatabaseRepository.getRepository(this);
            if(databaseRepository.getLeague() == 0){ //if the user hasnt set up their profile this creates a default profile for them, without this the app crashes if a profile doesnt exist
                User userUpdate = new User("user", 501);

                databaseRepository.updateUser(userUpdate);
            }
            startNotifications();
        }).start();





        //navbar fragment switching
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

    //method to create the notification channel and manager
    private void startNotifications(){

        createNotificationChannel();
        LiveViewModel viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LiveViewModel.class);
        DatabaseRepository databaseRepository = DatabaseRepository.getRepository(this);
        Notifications notificationManager = new Notifications(this,  databaseRepository);
        notificationManager.run();

    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        final String CHANNEL_ID = "YouScore";

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



    private void checkNotificationPermissions() {
        final String permission = android.Manifest.permission.POST_NOTIFICATIONS;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // The permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            // The permission is already granted, proceed with your logic
            Log.d(TAG, "Notification permission is already granted");
        }
    }

}
