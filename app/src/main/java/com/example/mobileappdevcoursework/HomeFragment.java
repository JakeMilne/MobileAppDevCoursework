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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

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
        adapter = new MyAdapter(getActivity().getApplicationContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        List<Item> sampleData = new ArrayList<>();
        sampleData.add(new Item("Sample Title 1", "2023-11-22 15:30:00"));
        sampleData.add(new Item("Sample Title 2", "2023-11-23 10:00:00"));

        adapter.setItems(sampleData);
        // Initialize the ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        System.out.println("here\n\n\n\n\n\n\n\n\n");
        // Observe the data changes in the ViewModel
        viewModel.getItems().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                Log.d("HomeFragment", "onChanged: LiveData updated with " + items.size() + " items");

                adapter.setItems(items);
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
}
