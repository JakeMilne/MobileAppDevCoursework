    package com.example.mobileappdevcoursework;

    import android.app.Application;

    import androidx.lifecycle.AndroidViewModel;
    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.MutableLiveData;

    import com.example.mobileappdevcoursework.data.DatabaseRepository;

    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.util.ArrayList;
    import java.util.List;

    public class LiveViewModel extends AndroidViewModel {

        private MutableLiveData<List<LiveGame>> liveGamesLiveData;
        private DatabaseRepository databaseRepository;

        private boolean isDataLoading = false;

        public LiveViewModel(Application application) {
            super(application);
            liveGamesLiveData = new MutableLiveData<>();
            databaseRepository = DatabaseRepository.getRepository(application);


        }



        public LiveData<List<LiveGame>> getGames() {
            return liveGamesLiveData;
        }

        public void loadData() {
            if (isDataLoading) {
                // Don't start a new data loading process if one is already in progress
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isDataLoading = true;

                    List<LiveGame> liveGames = liveSearch();

                    // Check if data was successfully loaded
                    if (!liveGames.isEmpty()) {
                        liveGamesLiveData.postValue(liveGames);
                    }

                    isDataLoading = false;
                }
            }).start();
        }

        public List<LiveGame> liveSearch() {
            List<LiveGame> liveGames = new ArrayList<>();
            int leagueID = databaseRepository.getLeague();

            try {
                //System.out.println(leagueID);
                URL url = new URL("https://api.sportmonks.com/v3/football/livescores/inplay?api_token=vHnHu2OZtUGbhPvHGl9NhDXH5iv7lSGOSPvOhJ6gYwD91Q9X3NoA2CjA1xzr&include=events;participants&filters=fixtureLeagues:" + leagueID);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                connection.disconnect();

                if (content != null) {
                    String jsonString = content.toString();
                    System.out.println("here " + jsonString);



                    liveGames = jsonParser.parseLiveJson(jsonString, leagueID);
                    System.out.println(liveGames);
                    liveGamesLiveData.postValue(liveGames);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return liveGames;
        }
    }
