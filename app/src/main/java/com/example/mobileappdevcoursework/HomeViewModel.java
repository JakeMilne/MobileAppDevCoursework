package com.example.mobileappdevcoursework;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Item>> itemsLiveData;
    private Executor executor = Executors.newSingleThreadExecutor(); // Executor for background tasks


    public HomeViewModel() {
        itemsLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Item>> getItems() {
        return itemsLiveData;
    }

    public void loadData() {
        // Perform network operations in a background thread
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Item> items = mainSearch();
                // Post the result to the LiveData on the main thread
                itemsLiveData.postValue(items);
            }
        });
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
            System.out.println(jsonString);
            items = jsonParser.parseJson(jsonString);



        } catch (Exception e) {
            e.printStackTrace();
        }
//        for (Item item : items) {
//            System.out.println("Item(Title: " + item.getTitle() + ", date: " + item.getDate() + ")");
//        }
        return items;
    }
    public static String addMonth(){
        String date = LocalDate.now().plusMonths(1).toString();

        return date;
    }


}