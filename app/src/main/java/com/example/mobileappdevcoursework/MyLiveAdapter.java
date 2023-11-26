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
    private List<liveGame> liveGames;
    private OnItemClickListener onItemClickListener;


    public MyLiveAdapter(Context context, List<liveGame> games) {
        this.context = context;
        this.liveGames = games;
    }


    public MyLiveAdapter(Context context, List<liveGame> games, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.liveGames = games;
        this.onItemClickListener = onItemClickListener;
    }


    public void setLiveGames(List<liveGame> games){
        Log.d("MyAdapter", "setItems: Setting items in adapter, count=" + games.size());

        this.liveGames = games;
        notifyDataSetChanged(); // Notify the adapter that the data has changed

    }
    public void addGame(liveGame game) {
        liveGames.add(game);
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
        final liveGame currentGame = liveGames.get(position);

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
        return liveGames.size();
    }

    public interface OnItemClickListener {
        //void onBindViewHolder(MyViewHolder holder, int position);

        void onItemClick(int itemId);
    }



}
