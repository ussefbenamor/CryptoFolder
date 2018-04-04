package com.nafaaazaiez.cryptofolder.view.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nafaaazaiez.cryptofolder.R;
import com.nafaaazaiez.cryptofolder.Utils.SharedPrefsUtils;
import com.nafaaazaiez.cryptofolder.api.Callbacks;
import com.nafaaazaiez.cryptofolder.service.NotificationService;
import com.nafaaazaiez.cryptofolder.service.UpdateService;


public class SplashActivity extends FragmentActivity {

    private static final String TAG = SplashActivity.class.getCanonicalName();
    private Animation animation;
    private ImageView logo;
    private TextView loading;
    private UpdateService updateService;
    private Intent mServiceIntent;
    private SharedPrefsUtils prefsUtils;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo = findViewById(R.id.logo_img);
        loading = findViewById(R.id.loading);
        progress = findViewById(R.id.activity_splash_progress);
        Callbacks callbacks = new Callbacks(this);
        prefsUtils = SharedPrefsUtils.getInstance(this);
        if (savedInstanceState == null) {
            flyIn();
        }
        if (!prefsUtils.isDBInitialed()) {
            progress.setVisibility(View.VISIBLE);
            loading.setText(R.string.setting_up_first_time);
            callbacks.initDB();
            progress.setVisibility(View.INVISIBLE);
            loading.setText(R.string.welcome_message);
        }
        startUpdateService();
        startNotificationService();
        goToHome();

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
            .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i(TAG, "isMyServiceRunning?" + true + "");
                return true;
            }
        }
        Log.i(TAG, "isMyServiceRunning?" + false + "");
        return false;
    }

    private void startUpdateService() {
        prefsUtils.enableApplicationRunning(true);
        updateService = new UpdateService();
        mServiceIntent = new Intent(this, updateService.getClass());
        if (!isMyServiceRunning(updateService.getClass())) {
            startService(mServiceIntent);
        }
    }

    private void startNotificationService() {
        NotificationService notificationService = new NotificationService();
        mServiceIntent = new Intent(this, notificationService.getClass());
        if (!isMyServiceRunning(notificationService.getClass())) {
            startService(mServiceIntent);
        }
    }


    private void flyIn() {
        animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        logo.startAnimation(animation);
        loading.startAnimation(animation);
    }

    private void endSplash() {
        animation = AnimationUtils.loadAnimation(this,
            android.R.anim.fade_out);
        logo.startAnimation(animation);
        loading.startAnimation(animation);

        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {

                Intent intent = new Intent(SplashActivity.this,
                    HomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

    }

    private void goToHome() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                endSplash();
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}