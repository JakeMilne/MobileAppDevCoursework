package com.example.mobileappdevcoursework;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


//LiveScores fragment shows all live games in the users chosen league, LiveViewModel handles the API and MyLiveAdapter handles the recyclerView
public class LiveScores extends Fragment implements MyLiveAdapter.OnItemClickListener{



    private RecyclerView recyclerView;
    private MyLiveAdapter adapter;
    private LiveViewModel viewModel;
    private static final String TAG = "LiveScores Fragment";


    public LiveScores() {
        // Required empty public constructor
    }

    public static LiveScores newInstance(String param1, String param2) {
        LiveScores fragment = new LiveScores();
        Bundle args = new Bundle();

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
        View rootView = inflater.inflate(R.layout.fragment_live_scores, container, false);

        recyclerView = rootView.findViewById(R.id.liveRecycler);
        TextView noLiveGamesTextView = rootView.findViewById(R.id.noLiveGamesTextView);//if there aren't any live games, this textview shows saying so

        adapter = new MyLiveAdapter(getActivity().getApplicationContext(), new ArrayList<>(), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(LiveViewModel.class);

        viewModel.getGames().observe(getViewLifecycleOwner(), new Observer<List<LiveGame>>() {
            @Override
            public void onChanged(List<LiveGame> LiveGames) {
                Log.d(TAG, "onChanged: LiveData updated with " + LiveGames.size() + " live games");

                if (LiveGames != null && !LiveGames.isEmpty()) {
                    noLiveGamesTextView.setVisibility(View.GONE);
                    adapter.setLiveGames(LiveGames);
                } else {
                    noLiveGamesTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        // Trigger data loading when the fragment starts
        viewModel.loadData();
    }

    //same idea as OnItemClick in HomeFragment
    @Override
    public void onItemClick(int itemId) {
        Toast.makeText(requireContext(), "Item Clicked: " + itemId, Toast.LENGTH_SHORT).show();

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        Bundle bundle = new Bundle();
        bundle.putInt("ITEM_ID", itemId);
        navController.navigate(R.id.action_liveScores_to_liveGameDetails, bundle);
    }
}
