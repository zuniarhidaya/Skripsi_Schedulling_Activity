package com.example.scheduling_activity.ui.database.criteria;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CriteriaDao {
    @Query("Select * from criteria")
    List<CriteriaTable> getCriteriaList();
    @Insert
    void insertCriteria(CriteriaTable criteria);
    @Update
    void updateCriteria(CriteriaTable criteria);
    @Delete
    void deleteCriteia(CriteriaTable criteria);
}
