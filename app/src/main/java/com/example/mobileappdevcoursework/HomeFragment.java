package com.example.mobileappdevcoursework;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements MyAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private HomeViewModel viewModel;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("HomeFragment", "onCreateView: Fragment created");

        //inflating view
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        //initialising the recycler view
        recyclerView = view.findViewById(R.id.recycler);
        adapter = new MyAdapter(getActivity().getApplicationContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        // Initialize the ViewModel, this is used to get the list of upcoming games from the API
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        //System.out.println("here\n\n\n\n\n\n\n\n\n"); // used in testing when the navigation wasn't working correctly

        //this watches the livedata in homeviewmodel for updates
        viewModel.getItems().observe(getViewLifecycleOwner(), new Observer<List<Game>>() {
            @Override
            public void onChanged(List<Game> games) {

                // Updates the RecyclerView with the new list of games once the API response has been handled
                adapter.setGames(games);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // calls the loadData function when the fragment starts. This is what deals with the game table and the API to fill the recyclerview
        viewModel.loadData();
    }


    //on click functions for the view details button
    @Override
    public void onItemClick(int gameId) {
        //Toast.makeText(requireContext(), "Item Clicked: " + gameId, Toast.LENGTH_SHORT).show(); //used for testing

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        //sending the gameId of the selected game to the game details fragment
        Bundle bundle = new Bundle();
        bundle.putInt("ITEM_ID", gameId);
        navController.navigate(R.id.action_homeFragment_to_gameDetails, bundle);
    }
}