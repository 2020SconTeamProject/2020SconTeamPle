package com.example.sconproject2020.Home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.sconproject2020.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = intent.getStringExtra("name");
        String time = intent.getStringExtra("time");
        Log.e("name",name);
        Log.e("time",time);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentTitle(name);
        builder.setContentText(time);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.createNotificationChannel(new NotificationChannel
                    ("channel","default",NotificationManager.IMPORTANCE_DEFAULT));
        }
        notificationManager.notify(1234, builder.build());
    }
}
