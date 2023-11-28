package com.example.mobileappdevcoursework;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link gameDetails#newInstance} factory method to
 * create an instance of this fragment.
 */

/*
Fragment to show details of upcoming game
accessed when user clicks on a game in HomeFragment
 */
public class gameDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public gameDetails() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment gameDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static gameDetails newInstance(String param1, String param2) {
        gameDetails fragment = new gameDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //game Id is passed from HomeFragment, bundle handles this
        Bundle bundle = getArguments();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_game_details, container, false);

        //getting textviews to fill out later
        TextView titleTextView = rootView.findViewById(R.id.titleTextView);
        TextView timeView = rootView.findViewById(R.id.timeView);
        TextView venueView = rootView.findViewById(R.id.venueView);
        TextView homeView = rootView.findViewById(R.id.homeView);
        TextView awayView = rootView.findViewById(R.id.awayView);
        TextView homePosView = rootView.findViewById(R.id.homePosView);
        TextView awayPosView = rootView.findViewById(R.id.awayPosView);



        if (bundle != null) {
            // Extract the item_id from the Bundle
            int itemId = bundle.getInt("ITEM_ID", -1);
            //calling the api for the specific game
            String baseURL = "https://api.sportmonks.com/v3/football/fixtures/" + itemId + "?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:501";
            try{
                
                URL url = new URL(baseURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();


                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine.toString()); // used for testing
                    content.append(inputLine);
                }
                in.close();
                connection.disconnect();

                String jsonString = content.toString();
                //System.out.println(jsonString); // used for testing if output matches the api result

                //parsing the result and storing it as an instance of gameInstance
                gameInstance thisGame = jsonParser.parseGame(jsonString);


                //filling textviews with the instance of gameInstance

                //name of match i.e. Celtic vs Rangers
                titleTextView.setText(thisGame.gameName);

                //date and time of kickoff
                timeView.setText(thisGame.startTime);

                //venue for the game, usually just the home teams stadium
                venueView.setText(thisGame.venue);

                //home team name
                homeView.setText(thisGame.homeName);

                //away team name
                awayView.setText(thisGame.awayName);

                //home teams position in their league
                homePosView.setText(thisGame.homeTeamPosition);
                //away teams position in their league
                awayPosView.setText(thisGame.awayTeamPosition);
                //league positions might break once the playoffs start, as I don't know if the json data for playoff rounds include league positions


            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        return inflater.inflate(R.layout.fragment_game_details, container, false);
    }




}