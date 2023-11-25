package com.example.mobileappdevcoursework;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LiveViewModel extends ViewModel {

    private MutableLiveData<List<Item>> itemsLiveData;

    public LiveViewModel() {
        itemsLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Item>> getItems() {
        return itemsLiveData;
    }

    public void loadData() {
        // Perform network operations in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Item> items = liveSearch();
                // Post the result to the LiveData on the main thread
                itemsLiveData.postValue(items);
            }
        }).start();
    }

    public static List<Item> liveSearch(){
        List<Item> items = new ArrayList<Item>();
        try{

            URL url =  new URL("https://api.sportmonks.com/v3/football/livescores/inplay?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:501");


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

            if(content != null){
                String jsonString = content.toString();
                System.out.println(jsonString);
                items = jsonParser.parseJson(jsonString);
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
//        for (Item item : items) {
//            System.out.println("Item(Title: " + item.getTitle() + ", date: " + item.getDate() + ")");
//        }
        return items;
    }
}
