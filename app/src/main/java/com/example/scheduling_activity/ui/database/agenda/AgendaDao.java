package com.example.scheduling_activity.ui.database.agenda;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AgendaDao {
    @Query("Select * from agenda")
    List<AgendaTable> getAgendaList();
    @Insert
    void insertAgenda(AgendaTable agenda);
    @Update
    void updateAgenda(AgendaTable agenda);
    @Delete
    void deleteAgenda(AgendaTable agenda);
    @Query("Select * from agenda where tanggal = :date")
    List<AgendaTable> filterDate(String date) ;
}