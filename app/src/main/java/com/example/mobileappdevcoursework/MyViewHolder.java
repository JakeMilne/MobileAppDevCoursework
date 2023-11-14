package com.example.mobileappdevcoursework;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
public class MyViewHolder extends RecyclerView.ViewHolder{

//https://www.youtube.com/watch?v=TAEbP_ccjsk accessed 14/11/2023 at 1pm
    TextView titleView, dateView;

    public MyViewHolder(@NonNull View itemView){
        super(itemView);
        titleView = itemView.findViewById(R.id.title);
        dateView = itemView.findViewById(R.id.date);
    }


}
