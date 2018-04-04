package com.nafaaazaiez.cryptofolder.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import com.nafaaazaiez.cryptofolder.Utils.Constants;
import com.nafaaazaiez.cryptofolder.Utils.SharedPrefsUtils;
import com.nafaaazaiez.cryptofolder.api.Callbacks;


public class UpdateService extends Service {

    private static final String TAG = UpdateService.class.getCanonicalName();
    private SharedPrefsUtils prefsUtils;
    private SharedPreferences sharedPref;

    public UpdateService() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Back Service onCreate");
        prefsUtils = SharedPrefsUtils.getInstance(this.getApplicationContext());
        sharedPref =
            PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Back Service On Start Command");
        final int currentId = startId;
        final Callbacks callbacks = new Callbacks(this);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                do {
                    int syncPref = Integer.valueOf(sharedPref.getString("sync_frequency", "30"));
                    long endTime = System.currentTimeMillis() + syncPref * 1000;
                    while (System.currentTimeMillis() < endTime) {
                        synchronized (this) {
                            try {
                                wait(endTime - System.currentTimeMillis());
                                callbacks.updateDB();
                                broadcastNotification(getApplicationContext());
                            } catch (Exception e) {
                            }
                        }
                    }
                    Log.i(TAG, "Back Service Running " + currentId);
                } while (prefsUtils.isApplicationRunning());

                stopSelf();
            }
        };
        Thread t = new Thread(r);
        t.start();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Back Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Back Service onDestroy");
    }

    private void broadcastNotification(Context context) {
        Intent intent = new Intent(Constants.UPDATE);
        context.sendBroadcast(intent);
    }
}
