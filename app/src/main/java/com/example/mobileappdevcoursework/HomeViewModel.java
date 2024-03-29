package com.example.mobileappdevcoursework;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mobileappdevcoursework.data.DatabaseRepository;
import com.example.mobileappdevcoursework.data.Game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
public class HomeViewModel extends AndroidViewModel {

    //ANDROID DEVELOPERS, 2023. LiveData overview. [online]. Palo Alto, California: Android. Available from: https://developer.android.com/topic/libraries/architecture/livedata [Accessed 28 november 2023].
    private MutableLiveData<List<Game>> gamesLiveData;
    private Executor executor = Executors.newSingleThreadExecutor(); // Executor for background tasks
    private DatabaseRepository databaseRepository;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        gamesLiveData = new MutableLiveData<>();
        databaseRepository = databaseRepository.getRepository(application); //GameDatabase is used here to store/fetch games for offline use
    }

    public LiveData<List<Game>> getItems() {
        return gamesLiveData;
    }


    //function to get upcoming games
    public void loadData() {

        // using a background thread since it contains network and database operations
        executor.execute(new Runnable() {
            @Override
            public void run() {

                List<Game> localData = databaseRepository.getAll(); //gets stored games in the database
                gamesLiveData.postValue(localData); //updates recycler view

                List<Game> apiData = mainSearch(); //gets upcoming games from the API

                databaseRepository.deleteAll(); //deletes existing games from the database from

                databaseRepository.insertGames(apiData); //adds games from the API to the database for the next time loadData is used

                gamesLiveData.postValue(apiData); //updating recycler view, with the new set of upcoming games
            }
        });
    }

    public static void showTypes(){
        //this function prints all events and their IDs, should be used if adding more events to the json parsing functions.
        //This is no longer used, however could be useful if someone were to update the app.
        //
        try{
            String baseURL = "https://api.sportmonks.com/v3/core/types?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr";
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
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    //function that calls the API for upcoming games
    public List<Game> mainSearch(){
        List<Game> games = new ArrayList<Game>();

        int leagueID = databaseRepository.getLeague();
        try{
            //LocalDate.now() and addMonth() are used so the API finds games within the next month,
            // this may not show games towards the end of the period, I think this has to do with there being a limit
            // on how many games the api is willing to send

            String baseURL = "https://api.sportmonks.com/v3/football/fixtures/between/" + LocalDate.now() + "/" + addMonth() + "?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:" + leagueID;
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
            games = JsonParse.parseJson(jsonString);



        } catch (Exception e) {
            e.printStackTrace();
        }

        return games;
    }

    //returns today's date + 1 month, used to show all games within the next month in mainSearch()
    public static String addMonth(){
        String date = LocalDate.now().plusMonths(1).toString();

        return date;
    }


}

