package com.example.mobileappdevcoursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mobileappdevcoursework.databinding.ActivityMainBinding;
import android.os.Bundle;
import android.util.Log;

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


        binding.bottomNavigationView.setOnItemReselectedListener(item -> {
            if (item.getItemId() == R.id.main) {
                navController.navigate(R.id.HomeFragment);
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.profile) {
                navController.navigate(R.id.userProfile);
                replaceFragment(new userProfile());
            } else if (item.getItemId() == R.id.live) {
                navController.navigate(R.id.liveScores);
                replaceFragment(new liveScores());
            }
        });


    }

    private void replaceFragment(Fragment fragment){
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

