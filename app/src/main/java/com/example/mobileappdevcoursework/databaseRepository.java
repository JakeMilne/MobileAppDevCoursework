package com.example.mobileappdevcoursework;

import android.content.Context;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

public class databaseRepository {

    private GameDAO mGameDao;
    private UserDAO mUserDao;
    private FollowedGameDAO mFollowedGameDao;

    private static databaseRepository INSTANCE;

    private Context context;


    private databaseRepository(Context context){
        super();
        this.context = context;

        // setup for taskDao for accessing the database
        mGameDao = GameDatabase.getDatabase(context).GameDAO();
        mUserDao = UserDatabase.getDatabase(context).UserDAO();
        mFollowedGameDao = FollowedGameDatabase.getDatabase(context).followedGameDAO();
    }

    public static databaseRepository getRepository(Context context){
        if (INSTANCE == null){
            synchronized (databaseRepository.class) {
                if (INSTANCE == null)
                    INSTANCE = new databaseRepository(context);
            }
        }
        return INSTANCE;
    }

    public Game getGame(int id){
        return mGameDao.findGameById(id);
    }

    public List<Game> getAll(){
        return mGameDao.getAll();
    }

   public void insertGames(List<Game> games){
        mGameDao.insertGames(games);
   }

    public void deleteAll(){
        mGameDao.deleteAll();
    }

    public int getLeague(){return mUserDao.getLeague();}

    public String getName(){return mUserDao.getName();}

    public void updateUser(user userUpdate){mUserDao.updateUser(userUpdate);};

    public void deleteUser(){mUserDao.deleteUser();};

    public List<FollowedGame> getAllFollowed(){return mFollowedGameDao.getAllFollowed();};



    public void updateFollowedGames(FollowedGame game){mFollowedGameDao.updateFollowedGames(game);};

    public void deleteFollowedGame(int ID){mFollowedGameDao.deleteFollowedGame(ID);};


}
