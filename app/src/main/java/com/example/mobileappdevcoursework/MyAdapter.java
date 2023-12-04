package com.example.mobileappdevcoursework;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappdevcoursework.data.Game;

//adapter used for the recyclerview in HomeFragment
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Game> games;
    private OnItemClickListener onItemClickListener;
    private static final String TAG = "MyAdapter";



    public MyAdapter(Context context, List<Game> games) {
        this.context = context;
        this.games = games;
    }

    public MyAdapter(Context context, List<Game> games, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.games = games;
        this.onItemClickListener = onItemClickListener;
    }



    public void setGames(List<Game> games) {
        Log.d(TAG, "setItems: Setting items in adapter, count=" + games.size());

        // Sort the list of games by startTime in ascending order
        Collections.sort(games, new Comparator<Game>() {//sorting the list of games, so the ones that start soonest appear at the top
            @Override
            public int compare(Game game1, Game game2) {
                return game1.getStartTime().compareTo(game2.getStartTime());
            }
        });

        this.games = games;
        notifyDataSetChanged();
    }

    public void setLiveGames(List<LiveGame> games){

    }
    public void addGame(Game game) {
        games.add(game);
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Game currentGame = games.get(position);

        holder.titleView.setText(currentGame.getGameName());
        holder.dateView.setText(currentGame.getStartTime());

        // Set click listener for the button
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(currentGame.getGameID()); //used to
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return games.size();
    }

    public interface OnItemClickListener {


        void onItemClick(int itemId);
    }



}
