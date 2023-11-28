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

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Game> games;
    private List<LiveGame> LiveGames;
    private OnItemClickListener onItemClickListener;


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
        Log.d("MyAdapter", "setItems: Setting items in adapter, count=" + games.size());

        // Sort the list of games by startTime in ascending order
        Collections.sort(games, new Comparator<Game>() {
            @Override
            public int compare(Game game1, Game game2) {
                // Assuming startTime is a String, you may need to convert it to a comparable format
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
                    onItemClickListener.onItemClick(currentGame.getGameID());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return games.size();
    }

    public interface OnItemClickListener {
        //void onBindViewHolder(MyViewHolder holder, int position);

        void onItemClick(int itemId);
    }



}
