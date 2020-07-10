package com.example.scheduling_activity;


import androidx.room.Entity;

@Entity(tableName = "hasil")
public class HasilKonversi {
    private String name = null;
    private int meeting = 0;
    private int jabatan = 0;
    private int status = 0;
    private int jarak = 0;
    private int absensi = 0;
    private String tanggal = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMeeting() {
        return meeting;
    }

    public void setMeeting(int meeting) {
        this.meeting = meeting;
    }

    public int getJabatan() {
        return jabatan;
    }

    public void setJabatan(int jabatan) {
        this.jabatan = jabatan;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getJarak() {
        return jarak;
    }

    public void setJarak(int jarak) {
        this.jarak = jarak;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getAbsensi() {
        return absensi;
    }

    public void setAbsensi(int absensi) {
        this.absensi = absensi;
    }
}
