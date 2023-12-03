package com.example.mobileappdevcoursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mobileappdevcoursework.data.DatabaseRepository;
import com.example.mobileappdevcoursework.databinding.ActivityMainBinding;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 123; // You can use any integer value



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

        if (getIntent() != null) {
            String fragmentType = getIntent().getStringExtra("FRAGMENT_TYPE");

            if (fragmentType != null) {
                switch (fragmentType) {
                    case "LiveGameDetails":
                        int itemId = getIntent().getIntExtra("ITEM_ID", -1);
                        int leagueId = getIntent().getIntExtra("LEAGUE_ID", -1);

                        Bundle bundle = new Bundle();
                        bundle.putInt("ITEM_ID", itemId);
                        bundle.putInt("LEAGUE_ID", leagueId);

                        // Show the LiveGameDetails fragment with the provided data
                        navController.navigate(R.id.liveGameDetails, bundle);

                        break;
                    // Add more cases for other fragment types if needed
                }
            }
        }
        checkNotificationPermissions();







        new Thread(() -> {
            createNotificationChannel();
            LiveViewModel viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LiveViewModel.class);
            DatabaseRepository databaseRepository = DatabaseRepository.getRepository(this);
            Notifications notificationManager = new Notifications(this,  databaseRepository);
            notificationManager.run();
        }).start();


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

    private void checkNotificationPermissions() {
        final String permission = android.Manifest.permission.POST_NOTIFICATIONS;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // The permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            // The permission is already granted, proceed with your logic
            Log.d("MainActivity", "Notification permission is already granted");
        }
    }

}
