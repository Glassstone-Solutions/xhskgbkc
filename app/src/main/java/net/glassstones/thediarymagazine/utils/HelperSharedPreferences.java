package net.glassstones.thediarymagazine.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.glassstones.thediarymagazine.network.models.NI;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Thompson on 06/01/2016.
 * For Markit
 */
public class HelperSharedPreferences {

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

    public static boolean getSharedPreferencesBoolean(Context context, String key, boolean _default){
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

    public static String getJsonString(List<? extends NI> posts){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<NI>>() {
        }.getType();
        return gson.toJson(posts, listType);
    }

    public static List<NI> getPostsListFromSP(Context c, String key) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<NI>>() {
        }.getType();
        return gson.fromJson(HelperSharedPreferences.getSharedPreferencesString(c, key, "[]"), listType);
    }

}
