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

//adapter for the live scores fragment
public class MyLiveAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<LiveGame> LiveGames;
    private OnItemClickListener onItemClickListener;
    private static final String TAG = "MyLiveAdapter";


    public MyLiveAdapter(Context context, List<LiveGame> games) {
        this.context = context;
        this.LiveGames = games;
    }


    //constructor that includes the OnClicklistener for item
    public MyLiveAdapter(Context context, List<LiveGame> games, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.LiveGames = games;
        this.onItemClickListener = onItemClickListener;
    }


    public void setLiveGames(List<LiveGame> games){
        Log.d(TAG, "setItems: Setting items in adapter, count=" + games.size());


        // Sort the list of games by startTime in ascending order
        Collections.sort(games, new Comparator<LiveGame>() {
            @Override
            public int compare(LiveGame game1, LiveGame game2) {
                String startTime1 = game1.getStartTime();
                String startTime2 = game2.getStartTime();

                if (startTime1 == null && startTime2 == null) {
                    return 0;
                } else if (startTime1 == null) {
                    return -1;
                } else if (startTime2 == null) {
                    return 1;
                }

                return game1.getStartTime().compareTo(game2.getStartTime());
            }
        });

        LiveGames.clear(); //gets rid of anything in the recyclerview before adding the new sorted list
        LiveGames.addAll(games);

        notifyDataSetChanged();// Notify the adapter that the data has changed

    }
    public void addGame(LiveGame game) {
        LiveGames.add(game);
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
        final LiveGame currentGame = LiveGames.get(position);

        holder.titleView.setText(currentGame.getTitle());
        holder.dateView.setText(currentGame.getStartTime());

        // Set click listener for the button
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(currentGame.getId());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return LiveGames.size();
    }

    public interface OnItemClickListener {
        //void onBindViewHolder(MyViewHolder holder, int position);

        void onItemClick(int itemId);
    }



}
