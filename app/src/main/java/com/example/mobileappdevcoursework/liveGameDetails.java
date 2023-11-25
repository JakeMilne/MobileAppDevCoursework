package com.example.mobileappdevcoursework;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link liveGameDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class liveGameDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public liveGameDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment liveGameDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static liveGameDetails newInstance(String param1, String param2) {
        liveGameDetails fragment = new liveGameDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        View rootView = inflater.inflate(R.layout.fragment_live_game_details, container, false);
        System.out.println("created");
        if (bundle != null) {
            // Extract the item_id from the Bundle
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int itemId = bundle.getInt("ITEM_ID", -1);
                    try {
                        //denmark 1
                        URL url = new URL("https://api.sportmonks.com/v3/football/livescores/inplay?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:271");
                        //scotland 1
                        //URL url = new URL("https://api.sportmonks.com/v3/football/livescores/inplay?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:501");
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
                            final LiveGameInstance liveGame = jsonParser.parseLiveGame(content.toString(), itemId);

                            // Update the UI on the main thread
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (liveGame != null) {
                                        TextView timeView = rootView.findViewById(R.id.kickOffView);
                                        TextView venueView = rootView.findViewById(R.id.venueLiveView);
                                        TextView scoreView = rootView.findViewById(R.id.scoreView);
                                        TextView titleTextView = rootView.findViewById(R.id.titleLiveTextView);
                                        TextView eventView = rootView.findViewById(R.id.eventView);

                                        timeView.setText("started at: " + liveGame.startTime);
                                        scoreView.setText(liveGame.score);
                                        titleTextView.setText(liveGame.title);
                                        eventView.setText(liveGame.getEventList(liveGame.homeEvents));
                                        TextView awayEventView = rootView.findViewById(R.id.awayEventView);
                                        awayEventView.setText(liveGame.getEventList(liveGame.awayEvents));

                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        return rootView;
    };
}