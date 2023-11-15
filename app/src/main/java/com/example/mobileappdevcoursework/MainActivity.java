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

