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
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.nafaaazaiez.cryptofolder.R;
import com.nafaaazaiez.cryptofolder.Utils.CalculUtils;
import com.nafaaazaiez.cryptofolder.Utils.Constants;
import com.nafaaazaiez.cryptofolder.db.Coin;
import com.nafaaazaiez.cryptofolder.db.DBRepository;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ussef on 05/07/2017.
 */


public class RecyclerAdapterCoins extends RecyclerView.Adapter<RecyclerAdapterCoins.ViewHolder> {

    private static DBRepository dbRepository;
    private static String currencyPref;
    private static SharedPreferences sharedPref;
    private static Activity context;
    private static int lastPosition = -1;
    private final OnItemClickListener listener;
    private List<Coin> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapterCoins(Activity context, OnItemClickListener listener) {
        this.mDataset = new ArrayList<>();
        RecyclerAdapterCoins.context = context;
        this.listener = listener;
        dbRepository = new DBRepository(context);
        sharedPref =
            PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapterCoins.ViewHolder onCreateViewHolder(ViewGroup parent,
        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.row_coin, parent, false);
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

    public void setCoins(@NonNull List<Coin> coins) {
        mDataset = coins;
        notifyDataSetChanged();
    }

    public Coin getCoin(int position) {
        return mDataset.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(Coin item);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTVHeadName, mTVHeadAbbr, mTVHeadAmount, mTVHeadAmountCurr, mTVHeadTotalActual,
            mTVHeadTotalActualCurr, mTVHeadActualUnitPrice, mTVHeadActualUnitPriceCurr, mTVHeadActualTotal,
            mTVHeadActualTotalCurr, mTVHeadPurchaseUnitPrice, mTVHeadPurchaseUnitPriceCurr, mTVHeadPurchaseTotal,
            mTVHeadPurchaseTotalCurr, mTVHeadGLUnitPrice, mTVHeadGLUnitPriceCurr, mTVHeadGLTotal, mTVHeadGLTotalCurr,
            mTVHeadGLPercentage;
        public View view;
        public CircleImageView mIMGLogo;

        public ViewHolder(View v) {
            super(v);
            view = v;
            mIMGLogo = v.findViewById(R.id.row_coin_img_head_coin);
            mTVHeadName = v.findViewById(R.id.row_coin_tv_head_name);
            mTVHeadAbbr = v.findViewById(R.id.row_coin_tv_head_abbr);
            mTVHeadAmount = v.findViewById(R.id.row_coin_tv_head_amount);
            mTVHeadAmountCurr = v.findViewById(R.id.row_coin_tv_head_amount_currency);
            mTVHeadTotalActual = v.findViewById(R.id.row_coin_tv_head_actual_total);
            mTVHeadTotalActualCurr = v
                .findViewById(R.id.row_coin_tv_head_actual_total_currency);
            mTVHeadActualUnitPrice = v.findViewById(R.id.row_coin_tv_body_actual_unit_price_value);
            mTVHeadActualUnitPriceCurr = v
                .findViewById(R.id.row_coin_tv_body_actual_unit_price_currency);
            mTVHeadActualTotal = v.findViewById(R.id.row_coin_tv_body_actual_total_price_value);
            mTVHeadActualTotalCurr = v
                .findViewById(R.id.row_coin_tv_body_actual_total_price_currency);
            mTVHeadPurchaseUnitPrice = v
                .findViewById(R.id.row_coin_tv_body_purchase_unit_price_value);
            mTVHeadPurchaseUnitPriceCurr = v
                .findViewById(R.id.row_coin_tv_body_purchase_unit_price_currency);
            mTVHeadPurchaseTotal = v.findViewById(R.id.row_coin_tv_body_purchase_total_price_value);
            mTVHeadPurchaseTotalCurr = v
                .findViewById(R.id.row_coin_tv_body_purchase_total_price_currency);
            mTVHeadGLUnitPrice = v.findViewById(R.id.row_coin_tv_body_gl_unit_price_value);
            mTVHeadGLUnitPriceCurr = v.findViewById(R.id.row_coin_tv_body_gl_unit_price_currency);
            mTVHeadGLTotal = v.findViewById(R.id.row_coin_tv_body_gl_total_price_value);
            mTVHeadGLTotalCurr = v.findViewById(R.id.row_coin_tv_body_gl_total_price_currency);
            mTVHeadGLPercentage = v.findViewById(R.id.row_coin_tv_body_gl_percentage);
        }

        public void bind(final Coin item, final int position, final OnItemClickListener listener) {

            //TODO:modify currency
            currencyPref = sharedPref.getString(Constants.CURRENCY_LIST, USD);
            switch (currencyPref) {
                case USD:
                    mTVHeadTotalActualCurr.setText(Constants.USD_SIGN);
                    mTVHeadActualUnitPriceCurr.setText(Constants.USD_SIGN);
                    mTVHeadActualTotalCurr.setText(Constants.USD_SIGN);
                    mTVHeadPurchaseUnitPriceCurr.setText(Constants.USD_SIGN);
                    mTVHeadPurchaseTotalCurr.setText(Constants.USD_SIGN);
                    mTVHeadGLTotalCurr.setText(Constants.USD_SIGN);
                    mTVHeadGLUnitPriceCurr.setText(Constants.USD_SIGN);
                    break;
                case EURO:
                    mTVHeadTotalActualCurr.setText(Constants.EURO_SIGN);
                    mTVHeadActualUnitPriceCurr.setText(Constants.EURO_SIGN);
                    mTVHeadActualTotalCurr.setText(Constants.EURO_SIGN);
                    mTVHeadPurchaseUnitPriceCurr.setText(Constants.EURO_SIGN);
                    mTVHeadPurchaseTotalCurr.setText(Constants.EURO_SIGN);
                    mTVHeadGLTotalCurr.setText(Constants.EURO_SIGN);
                    mTVHeadGLUnitPriceCurr.setText(Constants.EURO_SIGN);
                    break;
                default:
                    mTVHeadTotalActualCurr.setText(Constants.USD_SIGN);
                    mTVHeadActualUnitPriceCurr.setText(Constants.USD_SIGN);
                    mTVHeadActualTotalCurr.setText(Constants.USD_SIGN);
                    mTVHeadPurchaseUnitPriceCurr.setText(Constants.USD_SIGN);
                    mTVHeadPurchaseTotalCurr.setText(Constants.USD_SIGN);
                    mTVHeadGLTotalCurr.setText(Constants.USD_SIGN);
                    mTVHeadGLUnitPriceCurr.setText(Constants.USD_SIGN);
                    break;
            }

            String mActualUnitPrice = CalculUtils.dblToStr(item.getActualPrice());
            String mGLUnitPrice = CalculUtils.dblToStr(
                item.getActualPrice() - dbRepository.getAvgUnitPricePurchases(item.getId()));
            String mGLPercentage = CalculUtils.percentage(item.getActualPrice(),
                dbRepository.getAvgUnitPricePurchases(item.getId()));
            String mGLTotalPrice = CalculUtils.dblToStr(
                (dbRepository.getTotalAmountPurchases(item.getId()) * item.getActualPrice())
                    - dbRepository.getTotalPricePurchases(item.getId()));
            String mActualTotal = CalculUtils.dblToStr(
                dbRepository.getTotalAmountPurchases(item.getId()) * item.getActualPrice());
            String mPurchaseUnitPrice = CalculUtils
                .dblToStr(dbRepository.getAvgUnitPricePurchases(item.getId()));
            String mAmount = CalculUtils
                .dblToStr(dbRepository.getTotalAmountPurchases(item.getId()));
            String mTotalPurchases = CalculUtils
                .dblToStr(dbRepository.getTotalPricePurchases(item.getId()));
            Glide.with(context)
                .load(item.getLogoURL())
                .into(mIMGLogo);

            mTVHeadName.setText(item.getCoinName());
            mTVHeadAbbr.setText(item.getName());
            mTVHeadAmount.setText(mAmount);
            mTVHeadAmountCurr.setText(item.getName());
            mTVHeadTotalActual.setText(mActualTotal);
            mTVHeadActualUnitPrice.setText(mActualUnitPrice);
            mTVHeadActualTotal.setText(mActualTotal);
            mTVHeadPurchaseUnitPrice.setText(mPurchaseUnitPrice);
            mTVHeadPurchaseTotal.setText(mTotalPurchases);

            if (mGLTotalPrice.charAt(0) == '-') {
                mTVHeadGLTotal.setTextColor(context.getResources().getColor(R.color.colorRed));
                mTVHeadGLUnitPrice.setTextColor(context.getResources().getColor(R.color.colorRed));
                mTVHeadGLPercentage.setTextColor(context.getResources().getColor(R.color.colorRed));
            } else {
                mTVHeadGLTotal.setTextColor(context.getResources().getColor(R.color.colorGreen));
                mTVHeadGLUnitPrice
                    .setTextColor(context.getResources().getColor(R.color.colorGreen));
                mTVHeadGLPercentage
                    .setTextColor(context.getResources().getColor(R.color.colorGreen));
            }
            mTVHeadGLUnitPrice.setText(mGLUnitPrice);
            mTVHeadGLTotal.setText(mGLTotalPrice);
            mTVHeadGLPercentage.setText(mGLPercentage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
            if (position > lastPosition) {

                Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
                view.startAnimation(animation);
                animation.setDuration(200);
                lastPosition = position;
            }
        }
    }

}