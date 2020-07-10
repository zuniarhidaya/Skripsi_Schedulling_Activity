package com.example.scheduling_activity.ui.alarm.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.scheduling_activity.R;

@SuppressLint("ServiceCast")
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("ALARM", "onReceive");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String name = "Reminder Agenda";
            String description = "Pengingat untuk Agenda";
            NotificationChannel channel = new NotificationChannel("ReminderId", name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.ALARM_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ReminderId")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Pengingat Agenda")
                .setContentText(intent.getStringExtra("name"))
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int randomId = (int) (System.currentTimeMillis() / 100);

        assert notificationManager != null;
        notificationManager.notify(randomId, builder.build());

    }


}
