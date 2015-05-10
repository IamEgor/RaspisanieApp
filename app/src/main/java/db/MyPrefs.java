package db;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Егор on 30.04.2015.
 */
public class MyPrefs {
    public static final String APP_PREFS = "app_prefs";
    public static final String STATION_PREFS = "station_prefs";
    public static final String UPDATE_DATE = "update_date";
    public static final String STATION_FROM = "station_from";
    public static final String STATION_TO = "station_to";
    public static final String STATION_SINGLE = "station_single";

    public static void setPrefs(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFS, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setPrefs(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFS, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static boolean getBoolPrefs(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFS, context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static String getStringPrefs(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFS, context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }
}
