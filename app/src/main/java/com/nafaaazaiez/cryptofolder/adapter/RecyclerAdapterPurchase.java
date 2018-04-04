package com.nafaaazaiez.cryptofolder.adapter;

import static com.nafaaazaiez.cryptofolder.Utils.Constants.EURO;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.USD;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.nafaaazaiez.cryptofolder.R;
import com.nafaaazaiez.cryptofolder.Utils.CalculUtils;
import com.nafaaazaiez.cryptofolder.Utils.Constants;
import com.nafaaazaiez.cryptofolder.Utils.SharedPrefsUtils;
import com.nafaaazaiez.cryptofolder.db.DBRepository;
import com.nafaaazaiez.cryptofolder.db.Purchase;
import java.util.ArrayList;
import java.util.List;
import net.danlew.android.joda.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Ussef on 05/07/2017.
 */


public class RecyclerAdapterPurchase extends
    RecyclerView.Adapter<RecyclerAdapterPurchase.ViewHolder> {

    private static Activity mContext;
    private static int lastPosition = -1;
    private final DBRepository dbRepository;
    private final OnItemClickListener listener;
    private final SharedPreferences sharedPref;
    private final SharedPrefsUtils prefsUtils;
    private List<Purchase> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapterPurchase(Activity context, OnItemClickListener listener) {
        this.mDataset = new ArrayList<>();
        RecyclerAdapterPurchase.mContext = context;
        dbRepository = new DBRepository(mContext);
        this.listener = listener;
        sharedPref =
            PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        prefsUtils = SharedPrefsUtils.getInstance(context.getApplicationContext());


    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapterPurchase.ViewHolder onCreateViewHolder(ViewGroup parent,
        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.row_purchase, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(mDataset.get(position), position, listener);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setPurchases(@NonNull List<Purchase> purchases) {
        mDataset.clear();
        mDataset = purchases;
        notifyDataSetChanged();
    }

    public Purchase getPurchase(int position) {
        return mDataset.get(position);
    }

    public interface OnItemClickListener {

        void onItemClick(Purchase item);

        void onOptionsClick(View view, Purchase item);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public ImageView mIMGOptions;
        TextView mTVAmountValueHead, mTVAmountCurrHead, mTVAmountValueBody,
            mTVPlatform, mTVDate, mTVTotalPriceCurr, mTVTotalPriceCurrBody, mTVTotalPriceValue,
            mTVTotalPriceValueBody, mTVUnitPriceCurr, mTVUnitPriceValue;
        private String mCoinName;
        private String currencyPref;
        private String langPref;
        private double usdValuePref;

        public ViewHolder(View v) {
            super(v);
            view = v;
            mTVAmountValueHead = v.findViewById(R.id.row_purchase_amount_value);
            mTVAmountCurrHead = v.findViewById(R.id.row_purchase_amount_currency);
            mTVAmountValueBody = v.findViewById(R.id.row_purchase_amount_body);
            mIMGOptions = v.findViewById(R.id.row_purchase_img_more_options);
            mTVPlatform = v.findViewById(R.id.row_purchase_platform);
            mTVDate = v.findViewById(R.id.row_purchase_purchase_date);
            mTVTotalPriceCurr = v.findViewById(R.id.row_purchase_total_price_currency);
            mTVTotalPriceCurrBody = v.findViewById(R.id.row_purchase_total_price_currency_body);
            mTVTotalPriceValue = v.findViewById(R.id.row_purchase_total_price_value);
            mTVTotalPriceValueBody = v.findViewById(R.id.row_purchase_total_price_value_body);
            mTVUnitPriceCurr = v.findViewById(R.id.row_purchase_unit_price_currency);
            mTVUnitPriceValue = v.findViewById(R.id.row_purchase_unit_price_value);
        }

        public void bind(final Purchase item, final int position,
            final OnItemClickListener listener) {

            currencyPref = sharedPref.getString(Constants.CURRENCY_LIST, USD);
            langPref = sharedPref.getString("languages_list", Constants.USD);

            if (langPref.equals(Constants.FR)) {
                usdValuePref = CalculUtils.strFrToDbl(prefsUtils.getUSDValue());
            } else {
                usdValuePref = CalculUtils.strToDbl(prefsUtils.getUSDValue());
            }
            double currentUnitPrice = CalculUtils
                .currentVal(item.getUnitPrice(), currencyPref, item.getCurrency(),
                    usdValuePref);

            mTVAmountValueHead.setText(CalculUtils.dblToStr(item.getAmount()));

            if (mCoinName == null) {
                mCoinName = dbRepository.getCoinNameFromId(item.getCoinId());
                mTVAmountCurrHead.setText(mCoinName);
            } else {
                mTVAmountCurrHead.setText(mCoinName);
            }
            mTVAmountValueBody.setText(CalculUtils.dblToStr(item.getAmount()));
            mTVPlatform.setText(item.getPlatform());

            switch (currencyPref) {
                case USD:
                    mTVTotalPriceCurr.setText(Constants.USD_SIGN);
                    mTVTotalPriceCurrBody.setText(Constants.USD_SIGN);
                    mTVUnitPriceCurr.setText(Constants.USD_SIGN);
                    break;
                case EURO:
                    mTVTotalPriceCurr.setText(Constants.EURO_SIGN);
                    mTVTotalPriceCurrBody.setText(Constants.EURO_SIGN);
                    mTVUnitPriceCurr.setText(Constants.EURO_SIGN);
                    break;
                default:
                    mTVTotalPriceCurr.setText(Constants.USD_SIGN);
                    mTVTotalPriceCurrBody.setText(Constants.USD_SIGN);
                    mTVUnitPriceCurr.setText(Constants.USD_SIGN);
                    break;
            }
            //TODO : modify currency

            mTVTotalPriceValue
                .setText(CalculUtils.dblToStr(item.getAmount() * currentUnitPrice));
            mTVTotalPriceValueBody
                .setText(CalculUtils.dblToStr(item.getAmount() * currentUnitPrice));
            mTVUnitPriceValue.setText(CalculUtils.dblToStr(currentUnitPrice));
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm");
            DateTime dt = formatter.parseDateTime(item.getDate());
            String result = String.valueOf(DateUtils.getRelativeTimeSpanString(mContext, dt));
            mTVDate.setText(result);
            mIMGOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onOptionsClick(view, item);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });

            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(mContext,
                    android.R.anim.slide_in_left);
                view.startAnimation(animation);
                animation.setDuration(200);
                lastPosition = position;
            }

        }
    }


}