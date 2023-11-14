package com.example.mobileappdevcoursework;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
public class MyViewHolder extends RecyclerView.ViewHolder{


    TextView titleView, dateView;

    public MyViewHolder(@NonNull View itemView){
        super(itemView);
        titleView = itemView.findViewById(R.id.title);
        dateView = itemView.findViewById(R.id.date);
    }


}
