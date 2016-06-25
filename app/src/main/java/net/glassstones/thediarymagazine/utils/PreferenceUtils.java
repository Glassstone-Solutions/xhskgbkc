package net.glassstones.thediarymagazine.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Thompson on 06/01/2016.
 * For Markit
 */
public class PreferenceUtils {

    public static void putSharedPreferencesInt(Context context, String key, int value){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=preferences.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static void putSharedPreferencesBoolean(Context context, String key, boolean val){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=preferences.edit();
        edit.putBoolean(key, val);
        edit.apply();
    }

    public static void putSharedPreferencesString(Context context, String key, String val){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=preferences.edit();
        edit.putString(key, val);
        edit.apply();
    }

    public static void putSharedPreferencesFloat(Context context, String key, float val){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=preferences.edit();
        edit.putFloat(key, val);
        edit.apply();
    }

    public static void putSharedPreferencesLong(Context context, String key, long val){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=preferences.edit();
        edit.putLong(key, val);
        edit.apply();
    }

    public static String getSharedPreferencesString(Context context, String key, String _default){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, _default);
    }

    public static int getSharedPreferencesInt(Context context, String key, int _default){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, _default);
    }

    public static boolean getBoolean(Context context, String key, boolean _default){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, _default);
    }

    public static long getSharedPreferencesLong(Context context, String key, long _default){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(key, _default);
    }

    public static float getSharedPreferencesFloat(Context context, String key, float _default){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getFloat(key, _default);
    }

    public static boolean getBoolean(Context context, Preferences preferences) {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(preferences.name(), false);
    }

    public static void putBoolean(final Context context, final Preferences preferences, final boolean value) {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(preferences.name(), value).commit();
    }

    public enum Preferences {

        DATA_BASE_LOADED, LAST_UPDATE, LAST_UPDATE_MILLIS, PROPERTY_REG_ID, NOTIFICATION_ON, NOTIFICATION_SOUND, NOTIFICATION_VIBRATION, PROPERTY_APP_VERSION, DOWNLOAD_IMAGES
    }

}
