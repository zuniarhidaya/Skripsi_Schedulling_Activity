package com.example.scheduling_activity.ui.register;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.auth.User;

public class UserModel {
    private String userID;
    private String email;
    private String password;
    private String jabatan;
    private String nip;
    private String nama;


    public UserModel() {
    }

    public UserModel(String userID, String email, String password, String jabatan, String nip, String nama) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.jabatan = jabatan;
        this.nip = nip;
        this.nama = nama;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}

