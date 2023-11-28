package com.example.mobileappdevcoursework;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyLiveAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<LiveGame> LiveGames;
    private OnItemClickListener onItemClickListener;


    public MyLiveAdapter(Context context, List<LiveGame> games) {
        this.context = context;
        this.LiveGames = games;
    }


    public MyLiveAdapter(Context context, List<LiveGame> games, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.LiveGames = games;
        this.onItemClickListener = onItemClickListener;
    }


    public void setLiveGames(List<LiveGame> games){
        Log.d("MyAdapter", "setItems: Setting items in adapter, count=" + games.size());

        this.LiveGames = games;
        notifyDataSetChanged(); // Notify the adapter that the data has changed

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
        holder.dateView.setText(currentGame.getDate());

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
