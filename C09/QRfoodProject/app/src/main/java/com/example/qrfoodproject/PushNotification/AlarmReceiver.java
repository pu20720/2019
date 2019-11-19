package com.example.qrfoodproject.PushNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service = new Intent(context, NotificationService.class);
        service.setData((Uri.parse("custom://" + System.currentTimeMillis())));

        Log.d("PushNotification", "Time is up, broadcast receiver trying to ask NotificationService to work");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(service);
        }else{
            context.startService(service);
        }
    }
}