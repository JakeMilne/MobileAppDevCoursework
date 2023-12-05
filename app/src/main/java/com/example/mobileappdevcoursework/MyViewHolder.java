package com.example.mobileappdevcoursework;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
public class MyViewHolder extends RecyclerView.ViewHolder{

    //EASY TUTO, 2021. RecyclerView Android Studio Tutorial | 2023. [online video].  27 July 2021. Available from: https://www.youtube.com/watch?v=TAEbP_ccjsk [Accessed 14 November 2023].

    TextView titleView, dateView;
    public Button button;

    public MyViewHolder(@NonNull View itemView){
        super(itemView);
        titleView = itemView.findViewById(R.id.title);
        dateView = itemView.findViewById(R.id.date);
        button = itemView.findViewById(R.id.button);
    }


}
