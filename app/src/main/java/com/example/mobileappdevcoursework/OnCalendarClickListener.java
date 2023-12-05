package com.example.mobileappdevcoursework;

import android.content.Intent;
import android.provider.CalendarContract;
import android.text.format.DateUtils;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//EL KHATIB, S., 2012. How to pass parameters to OnClickListener?. [online]. Place of publication: StackOverflow.com. Available from: https://stackoverflow.com/a/10614751 [Accessed 28th november 2023].
//custom OnClickListener so I can pass a GameInstance into the calendar app.
public class OnCalendarClickListener implements View.OnClickListener {
    private GameInstance thisGame;

    public OnCalendarClickListener(GameInstance thisGame) {
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
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getMilliseconds(thisGame.getStartTime()) + DateUtils.HOUR_IN_MILLIS + DateUtils.HOUR_IN_MILLIS) // adds 2 hours to the kick off time which is roughly the length of a game
                .putExtra(CalendarContract.Events.EVENT_LOCATION, thisGame.getVenue())
                .putExtra(CalendarContract.Events.DESCRIPTION, thisGame.getGameName() + " at " + thisGame.getStartTime());

        v.getContext().startActivity(i);
    }

    //converting the date string into milliseconds
    //so that the calendar can load the kick-off time correctly
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
