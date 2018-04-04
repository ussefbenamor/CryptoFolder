package com.nafaaazaiez.cryptofolder.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.nafaaazaiez.cryptofolder.Utils.Constants;
import java.util.Locale;

/**
 * Created by Ussef on 27/01/2018.
 */

public class BaseActivity extends AppCompatActivity {

    public void loadLocalLanguage() {
        SharedPreferences sharedPref = PreferenceManager
            .getDefaultSharedPreferences(getApplicationContext());

        String langPref = sharedPref.getString(Constants.LANGUAGES_LIST, Constants.EN);
        if (langPref.equalsIgnoreCase("")) {
            return;
        }
        Locale myLocale = new Locale(langPref);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources()
            .updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        loadLocalLanguage();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        loadLocalLanguage();
        super.onResume();
    }


    @Override
    protected void onPause() {
        loadLocalLanguage();
        super.onPause();
    }
}
