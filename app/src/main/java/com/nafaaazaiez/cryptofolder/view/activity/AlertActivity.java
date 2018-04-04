package com.nafaaazaiez.cryptofolder.view.activity;

import static com.nafaaazaiez.cryptofolder.Utils.Constants.CURRENCY_LIST;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nafaaazaiez.cryptofolder.R;
import com.nafaaazaiez.cryptofolder.Utils.CalculUtils;
import com.nafaaazaiez.cryptofolder.Utils.Constants;
import com.nafaaazaiez.cryptofolder.Utils.ViewUtils;
import com.nafaaazaiez.cryptofolder.db.Alert;
import com.nafaaazaiez.cryptofolder.db.Coin;
import com.nafaaazaiez.cryptofolder.db.DBRepository;
import de.hdodenhof.circleimageview.CircleImageView;

public class AlertActivity extends BaseActivity {


    private final String TAG = PurchaseActivity.class.getCanonicalName();


    private TextView mSwitchText, mTVTitle, mTVCurrent;
    private CircleImageView mImgVSelectCoin;
    private ProgressBar mProgress;
    private LinearLayout mLLSelectCoinBackground;
    private EditText mEdtBelow, mEdtAbove;
    private DBRepository dbRepository;
    private String currencyPref;
    private long mCoinId;
    private SwitchCompat mSwitch;
    private LinearLayout mDisableForm, mInputForm;
    private String langPref;


    /**
     * @inheritDoc
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        dbRepository = new DBRepository(this);
        SharedPreferences sharedPref =
            PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        currencyPref = sharedPref.getString(CURRENCY_LIST, Constants.USD);
        langPref = sharedPref.getString("languages_list", Constants.USD);
        findViews();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Bundle intent = getIntent().getExtras();
        assert intent != null;
        mCoinId = intent.getLong(Constants.COIN_ID, 0L);
        Coin mCoin = dbRepository.findCoinWithId(mCoinId);
        Glide.with(AlertActivity.this).load(mCoin.getLogoURL())
            .into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource,
                    Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mLLSelectCoinBackground.setBackground(resource);
                    }
                }
            });

        if (dbRepository.findCountAlertWithCoinId(mCoinId) == 0) {
            mImgVSelectCoin.setImageResource(R.color.transparent);
            mTVCurrent.setText(CalculUtils.dblToStr(mCoin.getActualPrice()));
        } else {
            Alert mAlert = dbRepository.findAlertWithCoinId(mCoinId);
            mImgVSelectCoin.setImageResource(android.R.color.transparent);
            mTVTitle.setText(getString(R.string.edit_an_alert));
            mEdtBelow.setText(CalculUtils.dblToStr(mAlert.getBelow()));
            mEdtAbove.setText(CalculUtils.dblToStr(mAlert.getAbove()));
            mTVCurrent.setText(CalculUtils.dblToStr(mCoin.getActualPrice()));
            toggleAlert(mAlert.getEnabled());

        }
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                toggleAlert(isChecked);
            }
        });
    }

    private void toggleAlert(boolean enabled) {
        mSwitch.setChecked(enabled);
        if (enabled) {
            mDisableForm.setVisibility(View.GONE);
            mInputForm.setVisibility(View.VISIBLE);
            mSwitchText.setText(mSwitch.getTextOn());
        } else {
            mDisableForm.setVisibility(View.VISIBLE);
            mInputForm.setVisibility(View.GONE);
            mSwitchText.setText(mSwitch.getTextOff());
        }
    }

    /**
     * find all views in the current layout
     * and assign them into variables
     */
    private void findViews() {
        mProgress = findViewById(R.id.activity_alert_progress_bar);
        mTVTitle = findViewById(R.id.activity_alert_tv_title);
        mImgVSelectCoin = findViewById(R.id.activity_alert_image_select);
        mLLSelectCoinBackground = findViewById(R.id.activity_alert_image_circle_background);
        mEdtBelow = findViewById(R.id.activity_alert_input_below);
        mEdtAbove = findViewById(R.id.activity_alert_input_above);
        mTVCurrent = findViewById(R.id.activity_alert_input_current);
        mSwitch = findViewById(R.id.activity_alert_switch);
        mDisableForm = findViewById(R.id.activity_alert_disable_form);
        mInputForm = findViewById(R.id.activity_alert_input_form);
        mSwitchText = findViewById(R.id.activity_alert_switch_text);
    }


    /**
     * this method is executed when the user click
     * the view on the parameter to verify user inputs
     * and add the purchase if all conditions are satisfied
     *
     * @param view : the complete button on the purchase layout
     */
    public void complete(View view) {

        boolean mEmptyBelow, mEmptyAbove;
        mEmptyBelow = ViewUtils.checkEmpty(mEdtBelow);
        mEmptyAbove = ViewUtils.checkEmpty(mEdtAbove);
        mProgress.setVisibility(View.VISIBLE);
        if (!mSwitch.isChecked()) {
            if (dbRepository.findCountAlertWithCoinId(mCoinId) > 0) {
                Alert alert = dbRepository.findAlertWithCoinId(mCoinId);
                dbRepository
                    .updateAlert(alert.getAbove(), alert.getBelow(), mSwitch.isChecked(),
                        currencyPref, mCoinId);
                Toast.makeText(this,
                    getString(R.string.updated_alert),
                    Toast.LENGTH_SHORT)
                    .show();
            } else {
                long mId = dbRepository
                    .insertAlert(0, 0, mSwitch.isChecked(), currencyPref, mCoinId);
                Toast.makeText(this, getString(R.string.new_alert_inserted), Toast.LENGTH_SHORT)
                    .show();
            }
            backToActivity();
        } else {
            if (mEmptyBelow && mEmptyAbove) {
                Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show();
            } else {
                double mAbove;
                double mBelow;
                if (langPref.equals(Constants.FR)) {
                    mAbove = CalculUtils.strFrToDbl(mEdtAbove.getText().toString());
                    mBelow = CalculUtils.strFrToDbl(mEdtBelow.getText().toString());
                } else {
                    mAbove = CalculUtils.strToDbl(mEdtAbove.getText().toString());
                    mBelow = CalculUtils.strToDbl(mEdtBelow.getText().toString());
                }

                if (mAbove < mBelow) {
                    mProgress.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, R.string.above_gt_below, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dbRepository.findCountAlertWithCoinId(mCoinId) > 0) {
                    dbRepository
                        .updateAlert(mAbove, mBelow, mSwitch.isChecked(), currencyPref, mCoinId);
                    Toast.makeText(this,
                        getString(R.string.updated_alert),
                        Toast.LENGTH_SHORT)
                        .show();
                } else {
                    long mId = dbRepository
                        .insertAlert(mAbove, mBelow, mSwitch.isChecked(), currencyPref, mCoinId);
                    Toast.makeText(this, getString(R.string.new_alert_inserted), Toast.LENGTH_SHORT)
                        .show();
                }
                backToActivity();
            }
        }
    }

    private void backToActivity() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
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

}
