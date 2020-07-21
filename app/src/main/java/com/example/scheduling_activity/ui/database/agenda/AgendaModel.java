package com.example.scheduling_activity.ui.database.agenda;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

public class AgendaModel {
    private String id = UUID.randomUUID().toString();
    private String name = null;
    private String meeting = null;
    private String jabatan = null;
    private String status = null;
    private String jarak = null;
    private String absensi = null;
    private String tanggal = null;
    private String hari = null;
    private int awal = 0;
    private int akhir = 0;
    private boolean isReminder = false;
    private Long time = 0L;
    private boolean isKaryawan = false;

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

    public AgendaModel() {

    }

    public AgendaModel(String name, String meeting, String jabatan, String status, String jarak, String absensi, String tanggal, String hari, int awal, int akhir, boolean isReminder, Long time, boolean isKaryawan) {
        this.name = name;
        this.meeting = meeting;
        this.jabatan = jabatan;
        this.status = status;
        this.jarak = jarak;
        this.absensi = absensi;
        this.tanggal = tanggal;
        this.hari = hari;
        this.awal = awal;
        this.akhir = akhir;
        this.isReminder = isReminder;
        this.time = time;
        this.isKaryawan = isKaryawan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public void setKaryawan(boolean karyawan) {
        isKaryawan = karyawan;
    }

    public boolean getKaryawan() {
        return isKaryawan;
    }
}
