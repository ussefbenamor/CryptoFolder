package com.nafaaazaiez.cryptofolder.adapter;

import static com.nafaaazaiez.cryptofolder.Utils.Constants.EURO;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.USD;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
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


public class RecyclerAdapterCoinSimple extends RecyclerView.Adapter<RecyclerAdapterCoinSimple.ViewHolder> implements
    Filterable {

    private static Context context;
    private static int lastPosition = -1;
    private final OnItemClickListener listener;
    private List<Coin> mDataset;
    private List<Coin> mDatasetFiltred;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapterCoinSimple(Context context, OnItemClickListener listener) {
        this.mDataset = new ArrayList<>();
        RecyclerAdapterCoinSimple.context = context;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapterCoinSimple.ViewHolder onCreateViewHolder(ViewGroup parent,
        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.row_coin_simple, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(mDatasetFiltred.get(position), position, listener);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDatasetFiltred.size();
    }

    public void setCoins(@NonNull List<Coin> coins) {
        mDataset = coins;
        mDatasetFiltred = coins;
        notifyDataSetChanged();
    }

    public Coin getCoin(int position) {
        return mDatasetFiltred.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mDatasetFiltred = mDataset;
                } else {
                    List<Coin> filteredList = new ArrayList<>();
                    for (Coin row : mDataset) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row
                            .getCoinName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    mDatasetFiltred = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDatasetFiltred;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDatasetFiltred = (ArrayList<Coin>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public interface OnItemClickListener {

        void onItemClick(Coin item);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mName, mAbbrv;
        public View view;
        public CircleImageView mIMGLogo;

        public ViewHolder(View v) {
            super(v);
            view = v;
            mIMGLogo = v.findViewById(R.id.row_coin_img_coin);
            mName = v.findViewById(R.id.row_coin_tv_name);
            mAbbrv = v.findViewById(R.id.row_coin_tv_abbr);
        }

        public void bind(final Coin item, final int position, final OnItemClickListener listener) {

            Glide.with(context)
                .load(item.getLogoURL())
                .into(mIMGLogo);

            mName.setText(item.getCoinName());
            mAbbrv.setText(item.getName());

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