package com.example.mobileappdevcoursework.data;

import android.content.Context;

import java.util.List;

public class DatabaseRepository {

    private GameDAO mGameDao;
    private UserDAO mUserDao;
    private FollowedGameDAO mFollowedGameDao;

    private static DatabaseRepository INSTANCE;

    private Context context;


    private DatabaseRepository(Context context){
        super();
        this.context = context;

        // setup for taskDao for accessing the database
        mGameDao = GameDatabase.getDatabase(context).GameDAO();
        mUserDao = UserDatabase.getDatabase(context).UserDAO();
        mFollowedGameDao = FollowedGameDatabase.getDatabase(context).followedGameDAO();
    }

    public static DatabaseRepository getRepository(Context context){
        if (INSTANCE == null){
            synchronized (DatabaseRepository.class) {
                if (INSTANCE == null)
                    INSTANCE = new DatabaseRepository(context);
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

    public void updateUser(User userUpdate){mUserDao.updateUser(userUpdate);};

    public void deleteUser(){mUserDao.deleteUser();};

    int getEventCount(int id){return mFollowedGameDao.getEventCount(id);};

    public List<FollowedGame> getAllFollowed(){return mFollowedGameDao.getAllFollowed();};



    public void updateFollowedGames(FollowedGame game){mFollowedGameDao.updateFollowedGames(game);};

    public void deleteFollowedGame(int ID){mFollowedGameDao.deleteFollowedGame(ID);};


}
