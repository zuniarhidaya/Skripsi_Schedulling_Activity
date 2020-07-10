package com.example.scheduling_activity.ui.alarm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.scheduling_activity.ui.database.AppExecutors;
import com.example.scheduling_activity.ui.database.DatabaseHelper;
import com.example.scheduling_activity.ui.database.agenda.AgendaTable;

import java.util.List;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intaent.action.BOOT_COMPLETED")) {

            AppExecutors.getInstance().diskIO().execute(() -> {

                DatabaseHelper db = DatabaseHelper.getInstance(context);
                List<AgendaTable> agendas = db.agendaDao().getAgendaReminder();
                for (AgendaTable agendaTable : agendas) {
                    AlarmHelper.setAlarm(context, agendaTable.getTime(), agendaTable.getName());
                }

            });


        }
    }
}
