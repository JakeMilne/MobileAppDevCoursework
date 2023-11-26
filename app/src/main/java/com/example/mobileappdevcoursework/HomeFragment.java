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
        View rootView = inflater.inflate(R.layout.fragment_main_page, container, false);
        recyclerView = rootView.findViewById(R.id.recycler);
        adapter = new MyAdapter(getActivity().getApplicationContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

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

        return rootView;
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