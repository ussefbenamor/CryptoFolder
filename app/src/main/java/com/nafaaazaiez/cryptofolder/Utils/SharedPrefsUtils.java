package com.nafaaazaiez.cryptofolder.Utils;

/**
 * Created by Ussef on 25/01/2018.
 */

import static com.nafaaazaiez.cryptofolder.Utils.Constants.APPLICATION_IS_RUNNING;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.CURRENCY;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.INITIAL;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.NOTIFICATION;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.UPDATE;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.USD_VALUE;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Omar on 2/26/2017.
 */

public class SharedPrefsUtils {


    private static SharedPrefsUtils instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private SharedPrefsUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.MY_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPrefsUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsUtils(context);
        }
        return instance;
    }

    private String getStr(String key, String def) {
        return sharedPreferences.getString(key, def);
    }

    private void putStr(String key, String value) {
        editor.putString(key, value).commit();
    }

    private int getInt(String key, int def) {
        return sharedPreferences.getInt(key, def);
    }

    private void putInt(String key, int value) {
        editor.putInt(key, value).commit();
    }

    private void putBool(String key, boolean val) {
        editor.putBoolean(key, val).commit();
    }

    private boolean getBool(String key, boolean def) {
        return sharedPreferences.getBoolean(key, def);
    }


    public void enableNotification(boolean val) {
        putBool(NOTIFICATION, val);
    }

    public boolean isNotificationEnabled() {
        return getBool(NOTIFICATION, true);
    }

    public void enableDBInitialed(boolean val) {
        putBool(INITIAL, val);
    }

    public boolean isDBInitialed() {
        return getBool(INITIAL, false);
    }

    public boolean update() {
        return getBool(UPDATE, false);
    }

    public void setUpdate(boolean val) {
        putBool(UPDATE, val);
    }

    public boolean isApplicationRunning() {
        return getBool(APPLICATION_IS_RUNNING, false);
    }

    public void enableApplicationRunning(boolean val) {
        putBool(APPLICATION_IS_RUNNING, val);
    }

    public String getCurrency() {
        return getStr(CURRENCY, Constants.USD);
    }

    public void setCurrency(String val) {
        putStr(CURRENCY, val);
    }

    public String getUSDValue() {
        return getStr(USD_VALUE, "1");
    }

    public void setUSDValue(String val) {
        putStr(USD_VALUE, val);
    }


}

