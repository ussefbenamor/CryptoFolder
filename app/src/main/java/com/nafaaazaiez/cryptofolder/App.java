package com.nafaaazaiez.cryptofolder;

/**
 * Created by Ussef on 20/01/2018.
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import com.nafaaazaiez.cryptofolder.Utils.Constants;
import com.nafaaazaiez.cryptofolder.db.DaoMaster;
import com.nafaaazaiez.cryptofolder.db.DaoMaster.DevOpenHelper;
import com.nafaaazaiez.cryptofolder.db.DaoSession;
import java.util.Locale;
import net.danlew.android.joda.JodaTimeAndroid;
import org.greenrobot.greendao.database.Database;

public class App extends Application {

    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    private static final boolean ENCRYPTED = false;

    private DaoSession daoSession;
    private Locale locale = null;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        DevOpenHelper helper = new DevOpenHelper(this,
            ENCRYPTED ? Constants.CRYPTO_DB_ENCRYPTED : Constants.CRYPTO_DB);
        Database db =
            ENCRYPTED ? helper.getEncryptedWritableDb(Constants.SUPER_SECRET)
                : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        daoSession.clear();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("languages_list", "");
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources()
                .updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (locale != null) {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig,
                getBaseContext().getResources().getDisplayMetrics());
        }
    }


}
