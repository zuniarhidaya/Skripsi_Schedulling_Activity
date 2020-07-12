package com.example.scheduling_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * User: jpotts
 * Date: 8/27/13
 * Time: 7:00 PM
 */

public class SessionManager {

    SharedPreferences pref;
    Editor editor;
    Context context;

    private int PRIVATE_MODE = 0;
    private static String PREF_NAME = "";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAMA = "name";
    //public static final String KEY_EMAIL = "email";
    //public static final String KEY_PASSWORD = "password";
    public static final String KEY_JABATAN = "jabatan";
    //public static final String KEY_TELPON = "telpon";
    public static final String KEY_NIP = "nip";
    public static final String KEY_IS_CRITERIA = "criteria";



    public static final String IS_LABEL = "IsLabelling";

    // Constructor
    public SessionManager(Context context){
        this.context = context;
        PREF_NAME = context.getPackageName();
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String nama, String jabatan, String nip){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAMA, nama);
        //editor.putString(KEY_EMAIL, email);
        //editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_JABATAN, jabatan);
        //editor.putString(KEY_TELPON, telpon);
        editor.putString(KEY_NIP, nip);
        editor.commit();
    }

    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            redirectOnLogout(LoginActivity.class);
        }
    }


    public void logoutUser(){
        clearUserSettings();
        redirectOnLogout(LoginActivity.class);
    }

    public void redirectOnLogout(Class loginActivity){

        Intent i = new Intent(context, loginActivity);

        // clear stack and start new activity
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }

    public void clearUserSettings(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

//    public boolean isLabelling(){
//        return pref.getBoolean(IS_LABEL, false);
//    }

//    public void setLabelling(){
//        editor.putBoolean(IS_LABEL, true);
//    }

    public String getName() {
        return pref.getString(KEY_NAMA, "");
    }
    public void setName(String nama) {
        editor.putString(KEY_NAMA, nama);
        editor.commit();
    }

//    public String getEmail(){ return pref.getString(KEY_EMAIL, ""); }
//    public void setEmail(String email) {
//        editor.putString(KEY_EMAIL, "");
//        editor.commit();
//    }

    public String getJabatan(){ return pref.getString(KEY_JABATAN, ""); }
    public void setJabatan(String jabatan) {
        editor.putString(KEY_JABATAN, "");
        editor.commit();
    }

//    public String getTelpon(){ return pref.getString(KEY_TELPON, ""); }
//    public void setTelpon(String telpon) {
//        editor.putString(KEY_TELPON, "");
//        editor.commit();
//    }

    public String getNip(){ return pref.getString(KEY_NIP, ""); }
    public void setNip(String nip) {
        editor.putString(KEY_NIP, "");
        editor.commit();
    }


    public boolean getStatusCriteria(){ return pref.getBoolean(KEY_IS_CRITERIA, false); }
    public void setDoneCriteria(boolean criteria) {
        editor.putBoolean(KEY_IS_CRITERIA, criteria);
        editor.commit();
    }
}
