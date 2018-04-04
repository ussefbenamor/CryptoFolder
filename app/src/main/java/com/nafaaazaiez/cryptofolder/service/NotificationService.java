package com.nafaaazaiez.cryptofolder.service;


import static com.nafaaazaiez.cryptofolder.Utils.Constants.CURRENCY_LIST;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.USD;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.nafaaazaiez.cryptofolder.R;
import com.nafaaazaiez.cryptofolder.Utils.CalculUtils;
import com.nafaaazaiez.cryptofolder.Utils.Constants;
import com.nafaaazaiez.cryptofolder.Utils.SharedPrefsUtils;
import com.nafaaazaiez.cryptofolder.api.Callbacks;
import com.nafaaazaiez.cryptofolder.db.Alert;
import com.nafaaazaiez.cryptofolder.db.Coin;
import com.nafaaazaiez.cryptofolder.db.DBRepository;
import com.nafaaazaiez.cryptofolder.view.activity.SplashActivity;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class NotificationService extends Service {

    private static final String TAG = NotificationService.class.getCanonicalName();
    private SharedPrefsUtils prefsUtils;
    private SharedPreferences sharedPref;

    public NotificationService() {
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
                    int syncPref = Integer
                        .valueOf(sharedPref.getString(Constants.SYNC_FREQUENCY, "3600"));
                    long endTime = System.currentTimeMillis() + 5000;
                    while (System.currentTimeMillis() < endTime) {
                        synchronized (this) {
                            try {
                                wait(endTime - System.currentTimeMillis());
                                checkPriceIsAlertAble();
                            } catch (Exception e) {
                                e.printStackTrace();
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

    private void checkPriceIsAlertAble() {
        DBRepository mDbRepository = new DBRepository(this);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String currencyPref = sharedPref.getString(CURRENCY_LIST, USD);
        boolean notifSettingsEnabled = sharedPref.getBoolean("notifications_alert", true);
        boolean notifSettingsVibrate = sharedPref
            .getBoolean("notifications_new_message_vibrate", true);
        String notifSettingsSound = sharedPref
            .getString("notifications_new_message_ringtone", "DEFAULT_SOUND");
        if (notifSettingsEnabled) {
            List<Alert> mAlerts = mDbRepository.findAllAlerts();
            for (Alert alert : mAlerts) {
                if (alert.getEnabled()) {
                    Coin mCoin = mDbRepository.findCoinWithId(alert.getCoinId());
                    double actualPrice = CalculUtils
                        .currentVal(mCoin.getActualPrice(), currencyPref, alert.getCurrency(),
                            mDbRepository.getUsdValuePref());
                    if (actualPrice >= alert.getAbove()) {
                        String primaryText =
                            mCoin.getCoinName() + " price is now above " + CalculUtils
                                .dblToStr(alert.getAbove());
                        String secondaryText =
                            "The actual price is " + CalculUtils.dblToStr(actualPrice);
                        sendNotification(alert.getId(), getString(R.string.app_name), primaryText,
                            secondaryText,
                            notifSettingsVibrate, notifSettingsSound, mCoin.getLogoURL());
                    } else if (actualPrice <= alert.getBelow()) {
                        String primaryText =
                            mCoin.getCoinName() + " price is now below " + CalculUtils
                                .dblToStr(alert.getAbove());
                        String secondaryText =
                            "The actual price is " + CalculUtils.dblToStr(actualPrice);
                        sendNotification(alert.getId(), getString(R.string.app_name), primaryText,
                            secondaryText,
                            notifSettingsVibrate, notifSettingsSound, mCoin.getLogoURL());
                    }
                    alert.setEnabled(false);
                }
            }
        }
    }

    private void sendNotification(long id, String title, String primary, String secondary,
        boolean vibrate, String ringtone, String imageURL) {
        Bitmap icon = null;
        try {
            icon = Glide.with(this).asBitmap().load(imageURL).submit(100, 100).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (icon == null) {
            icon = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.ic_launcher);
        }
        NotificationCompat.Builder builder =
            new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(primary)
                .setSubText(secondary);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        builder.setDefaults(Notification.FLAG_AUTO_CANCEL)
            .setVibrate(vibrate ? new long[]{1000, 1000, 1000, 1000, 1000} : null)
            .setSound(Uri.parse(ringtone));
        Intent targetIntent = new Intent(this, SplashActivity.class);
        PendingIntent contentIntent = PendingIntent
            .getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(
            Context.NOTIFICATION_SERVICE);

        if (nManager != null) {
            nManager.notify((int) id, builder.build());
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Back Service onDestroy");
    }

}
