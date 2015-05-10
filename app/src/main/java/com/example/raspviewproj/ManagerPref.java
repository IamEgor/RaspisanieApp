package com.example.raspviewproj;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ManagerPref {


    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "Stations";
    public static final String APP_PREFERENCES_NAME_ONE = "Stations_one";

    private static ManagerPref sManagerPref;
    private SharedPreferences mSettings;
    private Context mAppContext;
    Set<String> temp;
    Set<String> temp_one;
    ArrayList<String> stations;
    ArrayList<String> stations_one;


    private ManagerPref(Context appContext) {

        mAppContext = appContext;

        mSettings = appContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        temp_one = new HashSet<String>();
        stations_one = new ArrayList<String>(temp_one);
        temp = new HashSet<String>();
        stations = new ArrayList<String>(temp);

    }

    public static ManagerPref get(Context c) {
        if (sManagerPref == null) {
            sManagerPref = new ManagerPref(c.getApplicationContext());
        }

        return sManagerPref;
    }

    public ArrayList<String> getStations_one() {
        return stations_one;
    }

    public ArrayList<String> getStations() {
        return stations;
    }

    public void delete_one(int index) {

        stations_one.remove(index);
        temp_one = new HashSet<String>(stations_one);
        Editor e = mSettings.edit();
        e.putStringSet(APP_PREFERENCES_NAME_ONE, temp_one);
        e.apply();

    }

    public void delete(int index) {

        stations.remove(index);
        temp = new HashSet<String>(stations);
        Editor e = mSettings.edit();
        e.putStringSet(APP_PREFERENCES_NAME, temp);
        e.apply();

    }

    public void put(String st1, String st2) {
        String sum = st1 + "-" + st2;
        stations.add(sum);
        temp = new HashSet<String>(stations);
        Editor e = mSettings.edit();
        e.putStringSet(APP_PREFERENCES_NAME, temp);
        e.apply();

    }

    public void put(String st1) {
        String sum = st1;
        stations_one.add(sum);
        temp_one = new HashSet<String>(stations);
        Editor e = mSettings.edit();
        e.putStringSet(APP_PREFERENCES_NAME_ONE, temp_one);
        e.apply();

    }

}
