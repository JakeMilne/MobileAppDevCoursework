package com.example.mobileappdevcoursework;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Item> items;
    private OnItemClickListener onItemClickListener;


    public MyAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    public MyAdapter(Context context, List<Item> items, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.items = items;
        this.onItemClickListener = onItemClickListener;
    }



    public void setItems(List<Item> items) {
        Log.d("MyAdapter", "setItems: Setting items in adapter, count=" + items.size());

        this.items = items;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    public void addItem(Item item) {
        items.add(item);
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
        final Item currentItem = items.get(position);

        holder.titleView.setText(currentItem.getTitle());
        holder.dateView.setText(currentItem.getDate());

        // Set click listener for the button
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(currentItem.getId());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        //void onBindViewHolder(MyViewHolder holder, int position);

        void onItemClick(int itemId);
    }



}
