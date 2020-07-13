package com.example.scheduling_activity.ui.alarm.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmHelper {
    public static void setAlarm(Context context, Long time, String name) {

        // name = nama agenda
        Intent i = new Intent(context, AlarmReceiver.class).putExtra("name", name);

        //buat pending intent (intent yang ter pending ketika alarm muncul)
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (System.currentTimeMillis() < time) {
            //buat alarm
            //RTC Wakeup, tipe waktu di itung ketika device mati
            assert alarmManager != null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }else{
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }

        }


    }


}
