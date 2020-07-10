package com.example.scheduling_activity.ui.database.agenda;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "agenda")
public class AgendaTable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name = null;
    @ColumnInfo(name = "meeting")
    private String meeting = null;
    @ColumnInfo(name = "jabatan")
    private String jabatan = null;
    @ColumnInfo(name = "status")
    private String status = null;
    @ColumnInfo(name = "jarak")
    private String jarak = null;
    @ColumnInfo(name = "absensi")
    private String absensi = null;
    @ColumnInfo(name = "tanggal")
    private String tanggal = null;
    @ColumnInfo(name = "hari")
    private String hari = null;
    @ColumnInfo(name = "awal")
    private int awal = 0;
    @ColumnInfo(name = "akhir")
    private int akhir = 0;
    @ColumnInfo(name = "reminder")
    private boolean isReminder = false;
    @ColumnInfo(name = "time")
    private Long time = 0L;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getAwal() {
        return awal;
    }

    public void setAwal(int awal) {
        this.awal = awal;
    }

    public int getAkhir() {
        return akhir;
    }

    public void setAkhir(int akhir) {
        this.akhir = akhir;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public AgendaTable() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeeting() {
        return meeting;
    }

    public void setMeeting(String meeting) {
        this.meeting = meeting;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }

    public String getAbsensi() {
        return absensi;
    }

    public void setAbsensi(String absensi) {
        this.absensi = absensi;
    }

    public boolean isReminder() {
        return isReminder;
    }

    public void setReminder(boolean reminder) {
        isReminder = reminder;
    }
}
