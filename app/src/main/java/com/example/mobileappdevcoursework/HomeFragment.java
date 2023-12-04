package com.example.mobileappdevcoursework;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappdevcoursework.data.DatabaseRepository;
import com.example.mobileappdevcoursework.data.Game;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements MyAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private HomeViewModel viewModel;
    private DatabaseRepository databaseRepository;
    private static final String TAG = "HomeFragment";

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, " onCreateView");
        databaseRepository = databaseRepository.getRepository(requireContext());
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        //creating recycler view
        recyclerView = view.findViewById(R.id.recycler);
        adapter = new MyAdapter(getActivity().getApplicationContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        //creating the textview that says welcome + the users name
        TextView welcomeView = view.findViewById(R.id.welcomeView);

        //gets name from user table and sets the welcome textview to include the name
        new Thread(new Runnable() {
            @Override
            public void run() {

                String name = databaseRepository.getName();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(name != null && !name.isEmpty()){
                                welcomeView.setText("welcome " + name);

                            }
                        }
                    });


            }
        }).start();



        // Initialize the ViewModel, which is used to call the API
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // Observe the data changes in the ViewModel
        viewModel.getItems().observe(getViewLifecycleOwner(), new Observer<List<Game>>() {
            @Override
            public void onChanged(List<Game> games) {
                Log.d(TAG, "onChanged: LiveData updated with " + games.size() + " games");

                // Update the RecyclerView with the new set of games
                adapter.setGames(games);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Trigger data loading when the fragment starts
        viewModel.loadData();
    }

    // on click method for when the user presses View Details
    @Override
    public void onItemClick(int itemId) {
        Toast.makeText(requireContext(), "Item Clicked: " + itemId, Toast.LENGTH_SHORT).show();

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        Bundle bundle = new Bundle();
        bundle.putInt("ITEM_ID", itemId); //itemId is used to get the chosen game from the API in GameDetails
        navController.navigate(R.id.action_homeFragment_to_gameDetails, bundle);
    }
}