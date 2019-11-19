package com.example.qrfoodproject.PushNotification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.qrfoodproject.Profile.checkNutrition_push;
import com.example.qrfoodproject.R;

public class NotificationService extends IntentService{

    private NotificationManager manager;
    Context context;


    public NotificationService() {
        super("NotificationService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //THREAD WORKS IN BACKGROUND
        context = this.getApplicationContext();
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = context.getResources();
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String toShow = checkNutrition_push.checkAndReturnResult(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            //當使用者的版本高於Oreo時，Android要求開發者需要另外為元素添加「channel(頻道)」，類似為通知作分類
            //優點是允許一個app有多種notification的呈現方式
            //缺點是我一開始不知道的時候寫到快中風幹你娘

            Log.d("PushNotification", "Receive intent and works via First function");

            String CHANNEL_ID = "Nutrition_channel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Food nutrition ", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);


            Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("QRFood")
                    .setContentText(toShow)
                    .setChannelId(CHANNEL_ID).build();

            ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);


            manager.notify(1337, notification);

            //weird test
            startForeground(1337, notification);


        }else{

            //當使用者的系統低於Oreo時
            //這裡我是沒有實際測試過，就是亂尻上來的code...不過想想這個app最低版本都設這麼高了
            //好像就沒什麼用到這裡的機會...吧

            Log.d("PushNotification", "Receive intent and works via Second function");

            Notification notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher_round))
                    .setTicker("Tick")
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setContentTitle("QRFood")
                    .setContentText("Hey, how's going on there?")
                    .build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
            notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            notification.ledARGB = 0xFFFFA500;
            notification.ledOnMS = 800;
            notification.ledOffMS = 1000;
            manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1, notification);
        }

    }
}
