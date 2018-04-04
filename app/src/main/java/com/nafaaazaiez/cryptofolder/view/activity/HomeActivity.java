package com.nafaaazaiez.cryptofolder.view.activity;

import static com.nafaaazaiez.cryptofolder.Utils.Constants.ACTION;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.ADD;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.COIN_ID;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.CURRENCY_LIST;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.EURO;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.HOME;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.REQUEST_CODE;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.SOURCE;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.USD;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.nafaaazaiez.cryptofolder.R;
import com.nafaaazaiez.cryptofolder.Utils.CalculUtils;
import com.nafaaazaiez.cryptofolder.Utils.Constants;
import com.nafaaazaiez.cryptofolder.Utils.SharedPrefsUtils;
import com.nafaaazaiez.cryptofolder.adapter.RecyclerAdapterCoins;
import com.nafaaazaiez.cryptofolder.adapter.RecyclerAdapterCoins.OnItemClickListener;
import com.nafaaazaiez.cryptofolder.api.Callbacks;
import com.nafaaazaiez.cryptofolder.db.Coin;
import com.nafaaazaiez.cryptofolder.db.DBRepository;
import com.nafaaazaiez.cryptofolder.service.UpdateService;

public class HomeActivity extends BaseActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerAdapterCoins adapter;
    private DBRepository mDbRepository;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private TextView mTVActualBalanceValue, mTVactualBalanceCurrency, mTVbalancePurchaseValue, mTVbalancePurchaseCurrency,
        mTVgainLossVariation, mTVgainLossValue, mTVgainLossCurrency, mTVgainLossPercentageValue, mTVgainLossPercentage;
    private UpdateService updateService;
    private Intent mServiceIntent;
    private SharedPreferences sharedPref;
    private String currencyPref;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context, "Auto update", Toast.LENGTH_SHORT).show();
            bindViews();
            updateCoins();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViews();
        initServices();
        initViews();
        initClickListeners();
        bindViews();
        swipeRefreshLayout = findViewById(R.id.a_main_swipe);
        final Callbacks callbacks = new Callbacks(this);
        swipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Simulates a long running task (updating data)
                        callbacks.updateDB();
                        bindViews();
                        updateCoins();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 100);
            }
        });
    }

    private void initServices() {
        updateService = new UpdateService();
        mServiceIntent = new Intent(this, updateService.getClass());
        if (!isMyServiceRunning(updateService.getClass())) {
            startService(mServiceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
            .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    @Override
    protected void onDestroy() {
        SharedPrefsUtils prefsUtils = SharedPrefsUtils.getInstance(this);
        prefsUtils.enableApplicationRunning(false);
        stopService(mServiceIntent);
        super.onDestroy();

    }

    private void initViews() {

        sharedPref =
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        setSupportActionBar(mToolbar);
        mDbRepository = new DBRepository(this);
        adapter = new RecyclerAdapterCoins(this, new OnItemClickListener() {
            @Override
            public void onItemClick(Coin item) {
//                Toast.makeText(HomeActivity.this, item.getCoinName(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(HomeActivity.this, DetailsActivity.class);
                i.putExtra(SOURCE, HOME);
                i.putExtra(COIN_ID, item.getId());
                startActivity(i);

            }
        });

        // query all notes, sorted a-z by their text
        updateCoins();
        RecyclerView mRecyclerView = findViewById(R.id.activity_home_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerViewd
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);

    }

    private void bindViews() {
        double mActualBalanceDbl = mDbRepository.getActualBalance();
        double mTotalPurchasesDbl = mDbRepository.getTotalPurchases();
        String mActualBalance = CalculUtils.dblToStr(mActualBalanceDbl);
        String mTotalPurchases = CalculUtils.dblToStr(mTotalPurchasesDbl);
        String mPercentage = CalculUtils.percentage(mActualBalanceDbl, mTotalPurchasesDbl);
        String mGainLoss = CalculUtils.dblToStr(mActualBalanceDbl - mTotalPurchasesDbl);
        mTVActualBalanceValue.setText(mActualBalance);
        currencyPref = sharedPref.getString(CURRENCY_LIST, USD);

        switch (currencyPref) {
            case USD:
                mTVbalancePurchaseCurrency.setText(Constants.USD_SIGN);
                mTVgainLossCurrency.setText(Constants.USD_SIGN);
                mTVactualBalanceCurrency.setText(Constants.USD_SIGN);
                break;
            case EURO:
                mTVbalancePurchaseCurrency.setText(Constants.EURO_SIGN);
                mTVgainLossCurrency.setText(Constants.EURO_SIGN);
                mTVactualBalanceCurrency.setText(Constants.EURO_SIGN);
                break;
            default:
                mTVbalancePurchaseCurrency.setText(Constants.USD_SIGN);
                mTVgainLossCurrency.setText(Constants.USD_SIGN);
                mTVactualBalanceCurrency.setText(Constants.USD_SIGN);
                break;
        }

        mTVbalancePurchaseValue.setText(mTotalPurchases);
        mTVgainLossVariation
            .setText(mPercentage.charAt(0) == '-' ? "↓" : mPercentage.equals("0") ? "" : "↑");
        mTVgainLossValue.setText(mGainLoss);
        mTVgainLossPercentageValue.setText(mPercentage);
        mTVgainLossPercentage.setText("");
    }

    private void initClickListeners() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, PurchaseActivity.class);
                i.putExtra(SOURCE, HOME);
                i.putExtra(ACTION, ADD);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE) {
            bindViews();
            updateCoins();
        }
    }

    /**
     * find all views in the current layout
     * and assign them into variables
     */
    private void findViews() {
        mToolbar = findViewById(R.id.activity_home_toolbar);
        mFab = findViewById(R.id.activity_home_fab);
        mTVActualBalanceValue = findViewById(R.id.activity_home_actual_balance_value);
        mTVactualBalanceCurrency = findViewById(R.id.activity_home_actual_balance_currency);
        mTVbalancePurchaseValue = findViewById(R.id.activity_home_balance_purchase_value);
        mTVbalancePurchaseCurrency = findViewById(R.id.activity_home_balance_purchase_currency);
        mTVgainLossVariation = findViewById(R.id.activity_home_gain_loss_variation);
        mTVgainLossValue = findViewById(R.id.activity_home_gain_loss_value);
        mTVgainLossCurrency = findViewById(R.id.activity_home_gain_loss_currency);
        mTVgainLossPercentageValue = findViewById(R.id.activity_home_gain_loss_percentage_value);
        mTVgainLossPercentage = findViewById(R.id.activity_home_gain_loss_percentage);

    }

    private void updateCoins() {
        adapter.setCoins(mDbRepository.findAllCoinsWithPurchases());
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(mMessageReceiver, new IntentFilter(Constants.UPDATE));
        bindViews();
        updateCoins();
    }

    @Override
    protected void onPause() {
        this.unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_home_settings:
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
}
