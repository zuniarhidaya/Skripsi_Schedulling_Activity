package com.example.scheduling_activity.ui.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.scheduling_activity.ui.database.agenda.AgendaDao;
import com.example.scheduling_activity.ui.database.agenda.AgendaTable;
import com.example.scheduling_activity.ui.database.criteria.CriteriaDao;
import com.example.scheduling_activity.ui.database.criteria.CriteriaTable;

@Database(entities = {CriteriaTable.class, AgendaTable.class},

        exportSchema = false, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {
    private static final String DB_NAME = "criteria_db";
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseHelper.class,
                    DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
    public abstract CriteriaDao criteriaDao();
    public abstract AgendaDao agendaDao();
}
