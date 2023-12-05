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
import com.example.mobileappdevcoursework.data.FollowedGame;
import com.google.android.material.tabs.TabLayout;

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

    private int itemId; //id of the chosen game
    private LiveGame liveGame; //instance of LiveGame which is used to store data about the chosen game
    private DatabaseRepository databaseRepository;
    private static final String TAG = "LiveGameDetails";


    public LiveGameDetails() {
        // Required empty public constructor
    }


    public static LiveGameDetails newInstance(String param1, String param2) {
        LiveGameDetails fragment = new LiveGameDetails();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseRepository = databaseRepository.getRepository(requireContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.fragment_live_game_details, container, false);

        //getting textviews and the follow button
        TextView timeView = view.findViewById(R.id.kickOffView);
        TextView venueView = view.findViewById(R.id.venueLiveView);
        TextView scoreView = view.findViewById(R.id.scoreView);
        TextView titleTextView = view.findViewById(R.id.titleLiveTextView);
        TextView homeEventView = view.findViewById(R.id.homeEventView);
        TextView awayEventView = view.findViewById(R.id.awayEventView);
        TextView minuteView = view.findViewById(R.id.minuteView);
        Button followBtn = view.findViewById(R.id.followBtn);//this is so the user can "follow" a game (enable notifications).
        followBtn.setOnClickListener(this);
        if (bundle != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Extract the item_id and league_id from the Bundle

                    itemId = bundle.getInt("ITEM_ID", -1);
                    int leagueid = bundle.getInt("LEAGUE_ID", -1);

                    try {
                        // if the user clicks on a notification, it should bring them to this fragment. since they could follow a game,
                        // then change their league selection, the league in the database might not be correct for the game.
                        // To prevent issues arising from this, the leagueId of the followed game is passed
                        if (leagueid == -1) {
                            leagueid = databaseRepository.getLeague();
                        }

                        //inplay doesnt have an option for searching by game id, so you need to get all livegames and search for it yourself.
                        URL url = new URL("https://api.sportmonks.com/v3/football/livescores/inplay?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:" + leagueid);

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

                            List<LiveGame> liveGames = JsonParse.parseLiveJson(content.toString(), leagueid); //leagueid is needed for notifications, so it is passed into parseLiveJson to be add as a field in each LiveGame
                            for (LiveGame game : liveGames) {
                                if (game.getId() == itemId) {

                                    liveGame = game;
                                }
                            }



                            // Update the UI on the ui thread
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //Log.d(TAG, liveGame.getVenue()); // used to test if the code that coverts venue id to name is working
                                    if (liveGame != null) {
                                        //setting textviews using liveGame
                                        timeView.setText("Started at: " + liveGame.getStartTime());
                                        scoreView.setText("Score: " + liveGame.getScore());
                                        titleTextView.setText(liveGame.getTitle());
                                        homeEventView.setText(liveGame.getHomeEventList());
                                        awayEventView.setText(liveGame.getAwayEventList());
                                        minuteView.setText(liveGame.getMins());
                                        venueView.setText("Venue: " + liveGame.getVenue());


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
        return view;
    };

    //onclick for follow button
    @Override
    public void onClick(View v) {
        FollowedGame followedGame = new FollowedGame(itemId, liveGame.eventCount());
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Perform database update in a separate thread
                databaseRepository.updateFollowedGames(followedGame);

            }
        }).start();

    }
}