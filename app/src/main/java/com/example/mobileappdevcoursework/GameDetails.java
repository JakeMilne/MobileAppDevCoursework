package com.example.mobileappdevcoursework;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobileappdevcoursework.data.DatabaseRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
//shows more in depth information about a chosen game, accessed from HomeFragment
public class GameDetails extends Fragment{

    private DatabaseRepository databaseRepository; //instance of roomDB database
    private static final String TAG = "GameDetails";

    public GameDetails() {
        // Required empty public constructor
    }

    public static GameDetails newInstance() {
        GameDetails fragment = new GameDetails();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Fragment created");
        databaseRepository = databaseRepository.getRepository(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.fragment_game_details, container, false);

        //initialising textviews and calendar button
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView timeView = view.findViewById(R.id.timeView);
        TextView venueView = view.findViewById(R.id.venueView);
        TextView homeView = view.findViewById(R.id.homeView);
        TextView awayView = view.findViewById(R.id.awayView);
        TextView homePosView = view.findViewById(R.id.homePosView);
        TextView awayPosView = view.findViewById(R.id.awayPosView);
        Button calendarBtn = view.findViewById(R.id.calendarBtn);



        if (bundle != null) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                    // Extract the item_id from the Bundle
                    int itemId = bundle.getInt("ITEM_ID", -1); //get the id of the game chosen from HomeFragment
                    int leagueID = databaseRepository.getLeague();  //get the users chosen league
                    String baseURL = "https://api.sportmonks.com/v3/football/fixtures/" + itemId + "?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:" + leagueID;
                    try{


                        URL url = new URL(baseURL);

                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder content = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        in.close();
                        connection.disconnect();

                        String jsonString = content.toString();
                        GameInstance thisGame = JsonParse.parseGame(jsonString); //creates a GameInstance object using the json

//
//                        URL url2 = new URL("https://api.sportmonks.com/v3/football/venues/" + thisGame.getVenue() + "?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr");
//                        HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
//
//                        BufferedReader in2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
//                        String inputLine2;
//                        StringBuilder content2 = new StringBuilder();
//
//                        while ((inputLine2 = in2.readLine()) != null) {
//                            content2.append(inputLine2);
//                        }
//                        in2.close();
//                        connection2.disconnect();
//
//
//                        if (content2 != null) {
//                            String venue = JsonParse.getVenue(content2.toString());
//                            String venue = thisGame.getVenueName();
//                            thisGame.setVenue(venue);
                            // Update the UI on the main thread
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    venueView.setText("Venue: " + thisGame.getVenueName());


                                }
                            });
//                        } else {
//                            System.out.println("null venue");
//                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                titleTextView.setText(thisGame.getGameName());
                                timeView.setText(thisGame.getStartTime());
                                homeView.setText(thisGame.getHomeName());
                                awayView.setText(thisGame.getAwayName());
                                homePosView.setText(String.valueOf(thisGame.getHomeTeamPosition()));
                                awayPosView.setText(String.valueOf(thisGame.getAwayTeamPosition()));

                                // Set the click listener for calendarBtn here
                                calendarBtn.setOnClickListener(new OnCalendarClickListener(thisGame));



                            }
                        });





                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }).start();

        };




        return view;
    }



}