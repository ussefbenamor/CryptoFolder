package com.nafaaazaiez.cryptofolder.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nafaaazaiez.cryptofolder.R;
import com.nafaaazaiez.cryptofolder.Utils.CalculUtils;
import com.nafaaazaiez.cryptofolder.Utils.Constants;
import com.nafaaazaiez.cryptofolder.Utils.SharedPrefsUtils;
import com.nafaaazaiez.cryptofolder.Utils.ViewUtils;
import com.nafaaazaiez.cryptofolder.adapter.RecyclerAdapterCoinSimple;
import com.nafaaazaiez.cryptofolder.api.Callbacks;
import com.nafaaazaiez.cryptofolder.db.Coin;
import com.nafaaazaiez.cryptofolder.db.DBRepository;
import com.nafaaazaiez.cryptofolder.db.Purchase;
import com.nafaaazaiez.cryptofolder.view.dialog.CoinsDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.Calendar;
import java.util.List;

public class PurchaseActivity extends BaseActivity {

    private final String TAG = PurchaseActivity.class.getCanonicalName();
    private String date_time = "";
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private CoinsDialog coinsDialog;
    private TextView mTVTotalCurrency, mTVSelectCoin, mTVTotal, mTVTitle;
    private CircleImageView mImgVSelectCoin;
    private ProgressBar mProgress;
    private LinearLayout mLLSelectCoinBackground, mLLFormDisabled, mLLRoot;
    private TextInputEditText mEdtAmount, mEdtPlatform, mEdtPrice, mEdtPurchaseDate;
    private DBRepository dbRepository;
    private String mAction;
    private long mPurchaseId;
    private String currencyPref;
    private String langPref;


    /**
     * @inheritDoc
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        dbRepository = new DBRepository(this);
        SharedPreferences sharedPref =
            PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        currencyPref = sharedPref.getString("currency_list", Constants.USD);
        langPref = sharedPref.getString("languages_list", Constants.USD);
        findViews();
        getDataFromIntent();
        coinSelectionSetup();
    }

    /**
     * This method allow extracting data from
     * the out coming intent
     * this intent have as content
     * source : the source activity
     * coinName : if the source is home then null else the name of the coin in the details activity
     * coinImage : same as coinName
     */
    private void getDataFromIntent() {
        Bundle intent = getIntent().getExtras();
        assert intent != null;
        String mSource = intent.getString(Constants.SOURCE, null);
        mAction = intent.getString(Constants.ACTION, null);
        long mCoinId = intent.getLong(Constants.COIN_ID, 0L);
        mTVTotalCurrency
            .setText(
                currencyPref.equals(Constants.USD_SIGN) ? Constants.USD_SIGN : Constants.EURO_SIGN);
        if (mAction.equals(Constants.ADD)) {
            if (mSource.equals(Constants.DETAILS)) {
                Coin mCoin = dbRepository.findCoinWithId(mCoinId);
                Glide.with(PurchaseActivity.this).load(mCoin.getLogoURL())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource,
                            Transition<? super Drawable> transition) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                mLLSelectCoinBackground.setBackground(resource);
                            }
                        }
                    });

                mImgVSelectCoin.setImageResource(R.color.transparent);
                mTVSelectCoin.setText(mCoin.getName());
                initListeners();
            } else {
                mImgVSelectCoin.setImageResource(R.drawable.ic_money);
                mLLSelectCoinBackground
                    .setBackgroundResource(R.drawable.ic_circle_white);
                mLLRoot.setBackgroundResource(R.color.light_grey_transparent);
                mLLFormDisabled.setVisibility(View.VISIBLE);
                mEdtAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sum_grey, 0, 0, 0);
                mEdtPlatform
                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_platform_grey, 0, 0, 0);
                mEdtPrice
                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_money_grey, 0, 0, 0);
                mEdtPurchaseDate
                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calender_grey, 0, 0, 0);
                mTVTotal.setTextColor(getResources().getColor(R.color.dark_grey));
                ViewUtils.enableClick(false, mEdtAmount, mEdtPlatform, mEdtPrice,
                    mEdtPurchaseDate);
            }
        } else if (mAction.equals(Constants.EDIT)) {
            //TODO implement interface Edit purchase

            mPurchaseId = intent.getLong(Constants.PURCHASE_ID, 0L);
            Purchase mPurchase = dbRepository.findPurchaseWithId(mPurchaseId);
            Coin mCoin = dbRepository.findCoinWithId(mPurchase.getCoinId());
            initListeners();
            Glide.with(this).load(mCoin.getLogoURL()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource,
                    Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mLLSelectCoinBackground.setBackground(resource);
                    }
                }
            });
            mImgVSelectCoin.setImageResource(android.R.color.transparent);
            mTVSelectCoin.setText(mCoin.getName());
            mTVTitle.setText(getString(R.string.edit_a_purchase));
            mEdtAmount.setText(CalculUtils.dblToStr(mPurchase.getAmount()));
            SharedPrefsUtils prefsUtils = SharedPrefsUtils.getInstance(getApplicationContext());
            double usdValuePref = CalculUtils.strToDbl(prefsUtils.getUSDValue());
            mEdtPrice.setText(CalculUtils.dblToStr(CalculUtils
                .currentVal(mPurchase.getUnitPrice(), currencyPref, mPurchase.getCurrency(),
                    usdValuePref)));
            mEdtPlatform.setText(mPurchase.getPlatform());
            mEdtPurchaseDate.setText(mPurchase.getDate());
            mTVTotal.setText(CalculUtils.dblToStr(mPurchase.getTotal()));
        }

    }

    /**
     * find all views in the current layout
     * and assign them into variables
     */
    private void findViews() {
        mLLRoot = findViewById(R.id.activity_purchase_root);
        mProgress = findViewById(R.id.activity_purchase_progress_bar);
        mLLFormDisabled = findViewById(R.id.activity_purchase_disable_form);
        mTVTitle = findViewById(R.id.activity_purchase_tv_title);
        mTVSelectCoin = findViewById(R.id.activity_purchase_text_select_coin);
        mImgVSelectCoin = findViewById(R.id.activity_purchase_image_select);
        mLLSelectCoinBackground = findViewById(R.id.activity_purchase_image_circle_background);
        mTVTotal = findViewById(R.id.activity_purchase_text_total);
        mEdtAmount = findViewById(R.id.activity_purchase_input_amount);
        mEdtPlatform = findViewById(R.id.activity_purchase_input_platform);
        mEdtPrice = findViewById(R.id.activity_purchase_input_price);
        mEdtPurchaseDate = findViewById(R.id.activity_purchase_input_purchase_date);
        mTVTotalCurrency = findViewById(R.id.activity_purchase_text_total_currency);
    }

    private void coinSelectionSetup() {
        final List<Coin> coins = dbRepository.findAllCoins();
        coinsDialog = new CoinsDialog(this, coins,
            new RecyclerAdapterCoinSimple.OnItemClickListener() {


                @Override
                public void onItemClick(Coin item) {

                    mTVSelectCoin.setText(item.getName());
                    mImgVSelectCoin.setImageResource(android.R.color.transparent);
                    Coin mCoin = dbRepository.getCoinFromName(item.getName());
                    Glide.with(PurchaseActivity.this).load(mCoin.getLogoURL())
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource,
                                Transition<? super Drawable> transition) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    mLLSelectCoinBackground.setBackground(resource);
                                }
                            }
                        });
                    ViewUtils.enableClick(true, mEdtAmount, mEdtPlatform, mEdtPrice,
                        mEdtPurchaseDate);
                    initListeners();
                    if (coinsDialog.isShowing()) {
                        coinsDialog.cancel();
                    } else {
                        Log.d(TAG, "Error : cannot click on  a non showing dialog");
                    }
                    mLLRoot.setBackgroundResource(R.color.white);
                    mLLFormDisabled.setVisibility(View.GONE);
                    mEdtAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sum, 0, 0, 0);
                    mEdtPlatform
                        .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_platform, 0, 0, 0);
                    mEdtPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_money, 0, 0, 0);
                    mEdtPurchaseDate
                        .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calender, 0, 0, 0);
                    mTVTotal.setTextColor(
                        PurchaseActivity.this.getResources().getColor(R.color.colorPrimary));
                }
            });
        coinsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT);
        coinsDialog.setCancelable(true);

    }

    /**
     * add focus listener to the purchase date input
     * This modifies the default EditText Focus behavior
     * and show the date and time dialog to select the
     * date and time values
     */
    private void initListeners() {
        mEdtPurchaseDate.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                mEdtPurchaseDate.setEnabled(false);
                datePicker();
                return date_time != null;
            }


        });
        mEdtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeTheTotal();
            }
        });
        mEdtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeTheTotal();
            }
        });
    }

    private void changeTheTotal() {
        if (mEdtAmount.getText().length() > 0 && mEdtPrice.getText().length() > 0) {
            String mAmount = mEdtAmount.getText().toString();
            String mUnit = mEdtPrice.getText().toString();
            mTVTotal.setText(
                CalculUtils.dblToStr(CalculUtils.strToDbl(mAmount) * CalculUtils.strToDbl(mUnit)));
        } else {
            mTVTotal.setText("0");
        }
    }

    /**
     * this method is executed when the user click
     * the view on the parameter to verify user inputs
     * and add the purchase if all conditions are satisfied
     *
     * @param view : the complete button on the purchase layout
     */
    public void complete(View view) {

        boolean mEmptyInput;
        mEmptyInput = ViewUtils.checkEmpty(mEdtAmount, mEdtPrice);
        if (mEmptyInput) {
            Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
        } else {
            broadcastNotification(this);
            mProgress.setVisibility(View.VISIBLE);
            Callbacks callbacks = new Callbacks(this);

            if (mAction.equals(Constants.ADD)) {
                callbacks.getUpdatedPrice(mTVSelectCoin.getText().toString());
                String mDateEmpty = CalculUtils.nowToStr();
                Log.d(TAG, mDateEmpty);
                String mCoinName = mTVSelectCoin.getText().toString();
                String mDate = mEdtPurchaseDate.getText().toString().length() == 0 ? mDateEmpty
                    : mEdtPurchaseDate.getText().toString();
                String mPlatform = mEdtPlatform.getText().toString().length() == 0 ? " "
                    : mEdtPlatform.getText().toString();
                double mUnitPrice;
                double mAmount;
                if (langPref.equals(Constants.FR)) {
                    mUnitPrice = CalculUtils.strFrToDbl(mEdtPrice.getText().toString());
                    mAmount = CalculUtils.strFrToDbl(mEdtAmount.getText().toString());
                } else {
                    mUnitPrice = CalculUtils.strToDbl(mEdtPrice.getText().toString());
                    mAmount = CalculUtils.strToDbl(mEdtAmount.getText().toString());
                }
                long mId = dbRepository
                    .insertPurchase(mCoinName, mAmount, mUnitPrice, mPlatform, mDate);
                Toast.makeText(this, R.string.purchase_inserted, Toast.LENGTH_SHORT)
                    .show();
            } else if (mAction.equals(Constants.EDIT)) {
                callbacks.getUpdatedPrice(mTVSelectCoin.getText().toString());
                String mDateEmpty = CalculUtils.nowToStr();
                String mCoinName = mTVSelectCoin.getText().toString();
                String mDate = mEdtPurchaseDate.getText().toString().length() == 0 ? mDateEmpty
                    : mEdtPurchaseDate.getText().toString();
                String mPlatform = mEdtPlatform.getText().toString().length() == 0 ? " "
                    : mEdtPlatform.getText().toString();
                double mUnitPrice;
                double mAmount;
                if (langPref.equals(Constants.FR)) {
                    mUnitPrice = CalculUtils.strFrToDbl(mEdtPrice.getText().toString());
                    mAmount = CalculUtils.strFrToDbl(mEdtAmount.getText().toString());
                } else {
                    mUnitPrice = CalculUtils.strToDbl(mEdtPrice.getText().toString());
                    mAmount = CalculUtils.strToDbl(mEdtAmount.getText().toString());
                }
                dbRepository
                    .updatePurchase(mPurchaseId, mCoinName, mAmount, mUnitPrice, mPlatform, mDate);
                Toast.makeText(this, getString(R.string.purchase_updated) + mPurchaseId,
                    Toast.LENGTH_SHORT)
                    .show();

            }

            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();

        }
    }

    /**
     * this method is executed when the user click
     * the view on the parameter and close the current
     * activity and back to the previous activity
     *
     * @param view : the close button on the purchase layout
     */
    public void close(View view) {
        finish();
    }

    private void datePicker() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    date_time = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                    //*************Call Time Picker Here ********************
                    timePicker();
                }
            }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    mHour = hourOfDay;
                    mMinute = minute;

                    mEdtPurchaseDate.setText(date_time.concat(" ")
                        .concat(String.valueOf(hourOfDay))
                        .concat(":")
                        .concat(String.valueOf(minute)));
                    mEdtPurchaseDate.setEnabled(true);
                    //TODO disable Edt time date click
                }
            }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void coinPicker(View view) {
        coinsDialog.show();
    }

    private void broadcastNotification(Context context) {
        Intent intent = new Intent(Constants.UPDATE);
        context.sendBroadcast(intent);
    }
}
