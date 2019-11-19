package com.example.qrfoodproject.PushNotification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;

public class ManageAlarm {

    //when user triggered the bottom of "activate push notification"

    public static void addSpecificTime(Context context){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0 , alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));


        //  alarmManager.cancel(pendingIntent);

        Calendar timeOfNow = Calendar.getInstance();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 17);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);

        if (timeOfNow.after(startTime)){
            //check if user activate the function when current time is over 17:00
            startTime.add(Calendar.DATE, 1);
            //Add one day, that mean make it activate at next 17:00
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                startTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Log.d("PushNotification", "Button has been pushed and triggered on!");
    }
}
