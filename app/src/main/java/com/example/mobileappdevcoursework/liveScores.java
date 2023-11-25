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

import com.example.mobileappdevcoursework.databinding.FragmentLiveScoresBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link liveScores#newInstance} factory method to
 * create an instance of this fragment.
 */
public class liveScores extends Fragment implements MyAdapter.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private LiveViewModel viewModel;

    public liveScores() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment liveScores.
     */
    // TODO: Rename and change types and number of parameters
    public static liveScores newInstance(String param1, String param2) {
        liveScores fragment = new liveScores();
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
        View rootView = inflater.inflate(R.layout.fragment_live_scores, container, false);

        recyclerView = rootView.findViewById(R.id.liveRecycler);
        TextView noLiveGamesTextView = rootView.findViewById(R.id.noLiveGamesTextView);

//        adapter = new MyAdapter(getActivity().getApplicationContext(), new ArrayList<>());

        adapter = new MyAdapter(getActivity().getApplicationContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



        recyclerView.setAdapter(adapter);

//        List<Item> sampleData = new ArrayList<>();
//        sampleData.add(new Item("Sample Title 1", "2023-11-22 15:30:00"));
//        sampleData.add(new Item("Sample Title 2", "2023-11-23 10:00:00"));
//        adapter.setItems(sampleData);

        viewModel = new ViewModelProvider(this).get(LiveViewModel.class);

        viewModel.getItems().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                Log.d("HomeFragment", "onChanged: LiveData updated with " + items.size() + " items");

                if (items != null && !items.isEmpty()) {
                    noLiveGamesTextView.setVisibility(View.GONE);
                    adapter.setItems(items);
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

    @Override
    public void onItemClick(int itemId) {
        Toast.makeText(requireContext(), "Item Clicked: " + itemId, Toast.LENGTH_SHORT).show();

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        Bundle bundle = new Bundle();
        bundle.putInt("ITEM_ID", itemId);
        navController.navigate(R.id.action_liveScores_to_liveGameDetails, bundle);
    }
}
