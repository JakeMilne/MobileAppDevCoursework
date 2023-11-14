package com.example.mobileappdevcoursework;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.*;
import java.io.*;
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
        home fragment = new home();
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
        View rootView = getView();
        if (rootView != null) {
            RecyclerView recyclerView = rootView.findViewById(R.id.recycler);
            List<Item> items = new ArrayList<Item>();
            try {
                getGames(items);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Now you can work with the recyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            recyclerView.setAdapter(new MyAdapter(getActivity().getApplicationContext(), items));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }
    public List<Item> getGames(List<Item> items) throws IOException {
        //call api and fill items
        //items.add(new Item(title, date)


        //https://api.sportmonks.com/v3/football/fixtures/between/2022-09-01/2022-09-30?api_token=YOUR_TOKEN

        URL url =  new URL("https://api.sportmonks.com/v3/football/fixtures/between/2023-11-14/2023-12-14?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:501");


        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //connection.setRequestMethod("GET");
        //connection.setRequestProperty("Api-Token", apiKey);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine.toString());
            content.append(inputLine);
        }

        String jsonString = content.toString();



        in.close();
        connection.disconnect();

        jsonString = jsonString.replaceAll("//[^\\n]*", "");
        //JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        //System.out.println(jsonObject.getAsJsonObject("data").getAsJsonArray("statistics").get(0));
        //System.out.println(jsonObject.getAsJsonObject("data").getAsJsonArray("statistics").get(1));
        //JsonArray eventsArray = jsonObject.getAsJsonObject("data").getAsJsonArray("events");

        //JsonElement dataElement = jsonObject.get("data");
        // JsonObject dataObject = dataElement.getAsJsonObject();

        //for (String key : dataObject.keySet()) {
        //   JsonElement value = dataObject.get(key);
        //System.out.println(key + ": " + value);
        //}
        //System.out.println(dataObject.get("name"));
        //System.out.println(content.toString());
        System.out.println(jsonString);
        return items;
    }
}