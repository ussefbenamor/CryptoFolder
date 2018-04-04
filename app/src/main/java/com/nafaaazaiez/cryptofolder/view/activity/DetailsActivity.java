package com.nafaaazaiez.cryptofolder.view.activity;

import static com.nafaaazaiez.cryptofolder.Utils.Constants.COIN_ID;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.CURRENCY_LIST;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.DETAILS;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.EURO;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.REQUEST_CODE;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.USD;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.nafaaazaiez.cryptofolder.R;
import com.nafaaazaiez.cryptofolder.Utils.CalculUtils;
import com.nafaaazaiez.cryptofolder.Utils.Constants;
import com.nafaaazaiez.cryptofolder.adapter.RecyclerAdapterPurchase;
import com.nafaaazaiez.cryptofolder.adapter.RecyclerAdapterPurchase.OnItemClickListener;
import com.nafaaazaiez.cryptofolder.db.Coin;
import com.nafaaazaiez.cryptofolder.db.DBRepository;
import com.nafaaazaiez.cryptofolder.db.Purchase;
import com.nafaaazaiez.cryptofolder.view.listener.MenuItemClickListener;
import de.hdodenhof.circleimageview.CircleImageView;
import java.text.DecimalFormat;

public class DetailsActivity extends BaseActivity {

    private RecyclerAdapterPurchase adapter;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private DBRepository mDbRepository;
    private long mCoinId;
    private TextView mTVCoinName, mTVCoinActualPrice, mTVCoinActualTotal,
        mTVCoinActualAmount, mTVCoinGainLossPer, mTVCoinGainLossVar,
        mTVCoinMaxToday, mTVCoinMinToday, mTVCoinGainLossCur,
        mTVCoinTotalPurchaseCur, mTVCoinActualPriceCur, mTVCoinActualTotalCur,
        mTVCoinAmountCur, mTVCoinTotalPurchase, mTVCoinGainLoss;
    private CircleImageView mImgCoin;
    private SharedPreferences sharedPref;
    private String currencyPref;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(context, "Auto Update", Toast.LENGTH_SHORT).show();
            bindViews();
            updatePurchases();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        findViews();
        getDataFromIntent();
        initViews();
        bindViews();
        initClickListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_details_alert:
                Intent i = new Intent(DetailsActivity.this, AlertActivity.class);
                i.putExtra(Constants.ACTION, Constants.ADD);
                i.putExtra(COIN_ID, mCoinId);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    public void bindViews() {
        Coin mCoin = mDbRepository.findCoinWithId(mCoinId);
        Glide.with(this)
            .load(mCoin.getLogoURL())
            .into(mImgCoin);
        String mPercentage = CalculUtils
            .percentage(mCoin.getActualPrice(), mDbRepository.getAvgUnitPricePurchases(mCoinId));
// TODO UPDATE PRICE
        String mActualPrice = new DecimalFormat(Constants.DOUBLE_FORMAT)
            .format(mCoin.getActualPrice());
        String mActualTotal = new DecimalFormat(Constants.DOUBLE_FORMAT)
            .format(mDbRepository.getTotalAmountPurchases(mCoinId) * mCoin.getActualPrice());
        String mGainLoss = new DecimalFormat(Constants.DOUBLE_FORMAT)
            .format(mCoin.getActualPrice() - mDbRepository.getAvgUnitPricePurchases(mCoinId));
        String mTotalPurchase = new DecimalFormat(Constants.DOUBLE_FORMAT)
            .format(mDbRepository.getTotalPricePurchases(mCoinId));
        String mActualAmount = new DecimalFormat(Constants.DOUBLE_FORMAT)
            .format(mDbRepository.getTotalAmountPurchases(mCoinId));
        String mMaxDayPrice = new DecimalFormat(Constants.DOUBLE_FORMAT)
            .format(mCoin.getMaxDayPrice());
        String mMinDayPrice = new DecimalFormat(Constants.DOUBLE_FORMAT)
            .format(mCoin.getMinDayPrice());
        currencyPref = sharedPref.getString(CURRENCY_LIST, USD);

        switch (currencyPref) {
            case USD:
                mTVCoinGainLossCur.setText(Constants.USD_SIGN);
                mTVCoinTotalPurchaseCur.setText(Constants.USD_SIGN);
                mTVCoinActualPriceCur.setText(Constants.USD_SIGN);
                mTVCoinActualTotalCur.setText(Constants.USD_SIGN);
                break;
            case EURO:
                mTVCoinGainLossCur.setText(Constants.EURO_SIGN);
                mTVCoinTotalPurchaseCur.setText(Constants.EURO_SIGN);
                mTVCoinActualPriceCur.setText(Constants.EURO_SIGN);
                mTVCoinActualTotalCur.setText(Constants.EURO_SIGN);
                break;
            default:
                mTVCoinGainLossCur.setText(Constants.USD_SIGN);
                mTVCoinTotalPurchaseCur.setText(Constants.USD_SIGN);
                mTVCoinActualPriceCur.setText(Constants.USD_SIGN);
                mTVCoinActualTotalCur.setText(Constants.USD_SIGN);
                break;
        }

        mTVCoinName.setText(mCoin.getName());
        mTVCoinActualPrice.setText(mActualPrice);
        mTVCoinActualTotal.setText(mActualTotal);
        mTVCoinActualAmount.setText(mActualAmount);
        mTVCoinTotalPurchase.setText(mTotalPurchase);
        mTVCoinGainLossPer.setText(mPercentage);
        mTVCoinGainLossVar
            .setText(mPercentage.charAt(0) == '-' ? "↓" : mPercentage.equals("0") ? "" : "↑");
        mTVCoinGainLoss.setText(mGainLoss);
        mTVCoinMaxToday.setText(mMaxDayPrice);
        mTVCoinMinToday.setText(mMinDayPrice);
        mTVCoinAmountCur.setText(mCoin.getName());
        mToolbar.setTitle(mCoin.getCoinName());
    }

    private void getDataFromIntent() {

        Bundle intent = getIntent().getExtras();
        assert intent != null;
        String mSource = intent.getString(Constants.SOURCE, null);
        mCoinId = intent.getLong(COIN_ID, 0L);

    }

    private void initClickListeners() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailsActivity.this, PurchaseActivity.class);
                i.putExtra(Constants.SOURCE, DETAILS);
                i.putExtra(Constants.ACTION, Constants.ADD);
                i.putExtra(COIN_ID, mCoinId);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE) {
            bindViews();
            updatePurchases();
        }
    }

    private void initViews() {

        sharedPref =
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDbRepository = new DBRepository(this);
        adapter = new RecyclerAdapterPurchase(this, new OnItemClickListener() {
            @Override
            public void onItemClick(Purchase item) {
//                Toast.makeText(DetailsActivity.this, item.getId().toString(), Toast.LENGTH_SHORT)
//                    .show();
            }

            @Override
            public void onOptionsClick(View view, Purchase item) {
                PopupMenu popup = new PopupMenu(DetailsActivity.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_purchase, popup.getMenu());
                popup.setOnMenuItemClickListener(
                    new MenuItemClickListener(DetailsActivity.this, item));
                popup.show();

            }
        });

        // get the purchase DAO
        updatePurchases();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    private void findViews() {
        mRecyclerView = findViewById(R.id.activity_details_recycler);
        mToolbar = findViewById(R.id.activity_details_toolbar);
        mFab = findViewById(R.id.activity_details_fab);
        mImgCoin = findViewById(R.id.activity_details_imgv_coin);
        mTVCoinName = findViewById(R.id.activity_details_tv_coin_name);
        mTVCoinActualPrice = findViewById(R.id.activity_details_tv_actual_price_value);
        mTVCoinActualTotal = findViewById(R.id.activity_details_tv_actual_total_value);
        mTVCoinActualAmount = findViewById(R.id.activity_details_tv_amount_value);
        mTVCoinGainLossPer = findViewById(R.id.activity_details_tv_gain_loss_percentage);
        mTVCoinGainLossVar = findViewById(R.id.activity_details_tv_gain_loss_variation);
        mTVCoinMaxToday = findViewById(R.id.activity_details_tv_max_today_value);
        mTVCoinMinToday = findViewById(R.id.activity_details_tv_min_today_value);
        mTVCoinGainLoss = findViewById(R.id.activity_details_tv_gain_loss_value);
        mTVCoinGainLossCur = findViewById(R.id.activity_details_tv_gain_loss_currency);
        mTVCoinTotalPurchaseCur = findViewById(R.id.activity_details_tv_total_purchase_currency);
        mTVCoinActualPriceCur = findViewById(R.id.activity_details_tv_actual_price_currency);
        mTVCoinActualTotalCur = findViewById(R.id.activity_details_tv_actual_total_currency);
        mTVCoinAmountCur = findViewById(R.id.activity_details_tv_amount_currency);
        mTVCoinTotalPurchase = findViewById(R.id.activity_details_tv_total_purchase_value);
    }

    public void updatePurchases() {
        adapter.setPurchases(mDbRepository.findAllPurchasesByCoinId(mCoinId));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(mMessageReceiver, new IntentFilter(Constants.UPDATE));
        bindViews();
        updatePurchases();
    }

    @Override
    protected void onPause() {
        this.unregisterReceiver(mMessageReceiver);
        super.onPause();
    }
}
