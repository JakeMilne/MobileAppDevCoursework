package com.example.mobileappdevcoursework;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Item> items;

    public MyAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
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

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titleView.setText(items.get(position).getTitle());
        holder.dateView.setText(items.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
