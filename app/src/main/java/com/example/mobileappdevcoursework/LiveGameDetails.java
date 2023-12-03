package com.example.mobileappdevcoursework;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobileappdevcoursework.data.DatabaseRepository;
import com.example.mobileappdevcoursework.data.FollowedGame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LiveGameDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveGameDetails extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int itemId;
    private LiveGame liveGame;
    private DatabaseRepository databaseRepository;

    public LiveGameDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LiveGameDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static LiveGameDetails newInstance(String param1, String param2) {
        LiveGameDetails fragment = new LiveGameDetails();
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
        databaseRepository = databaseRepository.getRepository(requireContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.fragment_live_game_details, container, false);
        TextView timeView = view.findViewById(R.id.kickOffView);
        TextView venueView = view.findViewById(R.id.venueLiveView);
        TextView scoreView = view.findViewById(R.id.scoreView);
        TextView titleTextView = view.findViewById(R.id.titleLiveTextView);
        TextView homeEventView = view.findViewById(R.id.homeEventView);
        TextView awayEventView = view.findViewById(R.id.awayEventView);
        TextView minuteView = view.findViewById(R.id.minuteView);
        Button followBtn = view.findViewById(R.id.followBtn);
        followBtn.setOnClickListener(this);
        System.out.println("created");
        if (bundle != null) {
            // Extract the item_id from the Bundle
            new Thread(new Runnable() {
                @Override
                public void run() {
                    itemId = bundle.getInt("ITEM_ID", -1);
                    int leagueid = bundle.getInt("LEAGUE_ID", -1);

                    try {
                        // if the user clicks on a notification, it should bring them to this fragment. since they could follow a game,
                        // then change their league selection, the league in the database might not be correct for the game.
                        // To prevent issues arising from this, the leagueId of the followed game is passed
                        if (leagueid == -1) {
                            leagueid = databaseRepository.getLeague();
                        }
                        System.out.println(leagueid);
                        //denmark 1
                        URL url = new URL("https://api.sportmonks.com/v3/football/livescores/inplay?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:" + leagueid);
                        //scotland 1
                        // URL url = new URL("https://api.sportmonks.com/v3/football/livescores/inplay?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:501");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder content = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        in.close();
                        connection.disconnect();
                        System.out.println("live details \n\n\n\n\n\n\n\n\n\n");
                        if (content != null) {
                            // System.out.println(content);
                            //liveGame = jsonParser.parseLiveJson(content.toString(), itemId);
                            List<LiveGame> liveGames = JsonParse.parseLiveJson(content.toString(), leagueid);
                            for (LiveGame game : liveGames) {
                                if (game.getId() == itemId) {
                                    System.out.println(game.toString());

                                    liveGame = game;
                                }
                            }

//                            URL url2 = new URL("https://api.sportmonks.com/v3/football/venues/" + liveGame.getVenueID() + "?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr");
//                            HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
//
//                            BufferedReader in2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
//                            String inputLine2;
//                            StringBuilder content2 = new StringBuilder();
//
//                            while ((inputLine2 = in2.readLine()) != null) {
//                                content2.append(inputLine2);
//                            }
//                            in2.close();
//                            connection2.disconnect();
//
//
//                            if (content2 != null) {
//                                String venue = jsonParser.getVenue(content2.toString());

                            // Update the UI on the main thread
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //System.out.println(venue);
                                    venueView.setText("Venue: " + liveGame.getVenue());


                                }
                            });
                        } else {
                            System.out.println("null venue");
                        }

                        // Update the UI on the main thread
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (liveGame != null) {

                                    System.out.println(liveGame.toString());
                                    System.out.println(liveGame.getStartTime());
                                    timeView.setText("Started at: " + liveGame.getStartTime());
                                    scoreView.setText("Score: " + liveGame.getScore());
                                    titleTextView.setText(liveGame.getTitle());
                                    homeEventView.setText(liveGame.getHomeEventList());
                                    awayEventView.setText(liveGame.getAwayEventList());
                                    minuteView.setText(liveGame.getMins());


                                }
                            }
                        });
                    //}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }).start();

        }
        return view;
    };

    @Override
    public void onClick(View v) {
        FollowedGame followedGame = new FollowedGame();
        followedGame.setGameID(itemId);
        System.out.println(" event count      " + liveGame.eventCount());
        followedGame.setEventCount(liveGame.eventCount());

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Perform database update in a separate thread
                databaseRepository.updateFollowedGames(followedGame);

                // If you need to update the UI based on the result, you can use a handler or other mechanisms
                // Example: handler.post(new Runnable() { ... });
            }
        }).start();

    }
}