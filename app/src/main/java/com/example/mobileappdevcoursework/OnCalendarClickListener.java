package com.example.mobileappdevcoursework;

import android.content.Intent;
import android.provider.CalendarContract;
import android.text.format.DateUtils;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OnCalendarClickListener implements View.OnClickListener {
    private gameInstance thisGame;

    public OnCalendarClickListener(gameInstance thisGame) {
        this.thisGame = thisGame;
    }

    @Override
    public void onClick(View v) {
        // Handle the calendar button click logic directly here
        // You can use startTime to perform any specific action

        Intent i = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, thisGame.getGameName())
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, getMilliseconds(thisGame.getStartTime()))
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getMilliseconds(thisGame.getStartTime()) + DateUtils.HOUR_IN_MILLIS + DateUtils.HOUR_IN_MILLIS)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, thisGame.getVenue())
                .putExtra(CalendarContract.Events.DESCRIPTION, thisGame.getGameName() + " at " + thisGame.getStartTime());

        v.getContext().startActivity(i);
    }

    private long getMilliseconds(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
