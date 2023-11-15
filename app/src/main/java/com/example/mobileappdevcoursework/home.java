package com.example.mobileappdevcoursework;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment otherGames.
     */
    // TODO: Rename and change types and number of parameters
    public static home newInstance(String param1, String param2) {
        Log.d("home", " newInstance: 1");
        home fragment = new home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        Log.d("home", " newInstance: 2");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Create a background thread to perform network operations
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Item> items = mainSearch();

                // Update the UI on the main thread
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleResult(items);
                    }
                });
            }
        }).start();
    }

    private void handleResult(List<Item> items) {
        if (getView() != null) {
            RecyclerView recyclerView = getView().findViewById(R.id.recycler);

            // Now you can work with the recyclerView
            MyAdapter adapter = new MyAdapter(getActivity().getApplicationContext(), items);
            recyclerView.setAdapter(adapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        } else {
            Log.d("home", "onCreate: failed");
        }
    }
    private void updateUI(List<Item> items) {
        // Access the RecyclerView and set the adapter here
        RecyclerView recyclerView = getView().findViewById(R.id.recycler);
        MyAdapter adapter = new MyAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);

        // Set the layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    public static List<Item> mainSearch(){
        List<Item> items = new ArrayList<Item>();
        try{


            String baseURL = "https://api.sportmonks.com/v3/football/fixtures/between/" + LocalDate.now() + "/" + addMonth() + "?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:501";
            URL url = new URL(baseURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                //System.out.println(inputLine.toString());
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            String jsonString = content.toString();

            items = parseJson(jsonString);

            for (Item item : items) {
                System.out.println("Item(Title: " + item.getTitle() + ", date: " + item.getDate() + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
    public static String addMonth(){
        String date = LocalDate.now().plusMonths(1).toString();

        return date;
    }

    private static List<Item> parseJson(String jsonString) {
        List<Item> items = new ArrayList<>();

        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement root = jsonParser.parse(jsonString);

            if (root.isJsonObject()) {
                JsonObject jsonObject = root.getAsJsonObject();
                JsonArray dataArray = jsonObject.getAsJsonArray("data");

                for (JsonElement itemElement : dataArray) {
                    JsonObject jsonItem = itemElement.getAsJsonObject();

                    String name = jsonItem.get("name").getAsString();
                    String startTime = jsonItem.get("starting_at").getAsString();

                    Item item = new Item(name, startTime);
                    items.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return items;
    }
}