package com.example.mobileappdevcoursework;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link gameDetails#newInstance} factory method to
 * create an instance of this fragment.
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
        // Required empty public constructor
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
        Log.d("gameDetails", "onCreate: Fragment created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        View rootView = inflater.inflate(R.layout.fragment_game_details, container, false);

        if (bundle != null) {
            // Extract the item_id from the Bundle
            int itemId = bundle.getInt("ITEM_ID", -1);

            String baseURL = "https://api.sportmonks.com/v3/football/fixtures/" + itemId + "?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:501";
            try{


                URL url = new URL(baseURL);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();


                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine.toString());
                    content.append(inputLine);
                }
                in.close();
                connection.disconnect();

                String jsonString = content.toString();
                System.out.println(jsonString);
                gameInstance thisgame = jsonParser.parseGame(jsonString);


                TextView titleTextView = rootView.findViewById(R.id.titleTextView);
                TextView timeView = rootView.findViewById(R.id.timeView);
                TextView venueView = rootView.findViewById(R.id.venueView);
                TextView homeView = rootView.findViewById(R.id.homeView);
                TextView awayView = rootView.findViewById(R.id.awayView);
                TextView homPosView = rootView.findViewById(R.id.homePosView);
                TextView awayPosView = rootView.findViewById(R.id.awayPosView);

                titleTextView.setText(thisgame.gameName);
                timeView.setText(thisgame.startTime);
                venueView.setText(thisgame.venue);
                homeView.setText(thisgame.homeName);
                awayView.setText(thisgame.awayName);
                homPosView.setText(thisgame.homeTeamPosition);
                awayPosView.setText(thisgame.awayTeamPosition);






            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        return inflater.inflate(R.layout.fragment_game_details, container, false);
    }




}