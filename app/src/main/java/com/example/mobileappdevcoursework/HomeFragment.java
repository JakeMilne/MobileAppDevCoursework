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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements MyAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private HomeViewModel viewModel;
    private databaseRepository databaseRepository;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("HomeFragment", "onCreateView: Fragment created");
        databaseRepository = databaseRepository.getRepository(requireContext());
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        adapter = new MyAdapter(getActivity().getApplicationContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        TextView welcomeView = view.findViewById(R.id.welcomeView);

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



        // Initialize the ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        System.out.println("here\n\n\n\n\n\n\n\n\n");
        // Observe the data changes in the ViewModel
        viewModel.getItems().observe(getViewLifecycleOwner(), new Observer<List<Game>>() {
            @Override
            public void onChanged(List<Game> games) {
                Log.d("HomeFragment", "onChanged: LiveData updated with " + games.size() + " games");

                // Update the RecyclerView with the new list of games
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

    @Override
    public void onItemClick(int itemId) {
        Toast.makeText(requireContext(), "Item Clicked: " + itemId, Toast.LENGTH_SHORT).show();

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        Bundle bundle = new Bundle();
        bundle.putInt("ITEM_ID", itemId);
        navController.navigate(R.id.action_homeFragment_to_gameDetails, bundle);
    }
}