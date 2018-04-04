package com.nafaaazaiez.cryptofolder.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.nafaaazaiez.cryptofolder.R;
import com.nafaaazaiez.cryptofolder.adapter.RecyclerAdapterCoinSimple;
import com.nafaaazaiez.cryptofolder.db.Coin;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ussef on 18/01/2018.
 */

public class CoinsDialog extends Dialog implements OnClickListener {


    private static final String TAG = CoinsDialog.class.getSimpleName();
    private RecyclerAdapterCoinSimple adapter = null;
    private EditText filterText = null;
    private RecyclerView recyclerView;
    private List<Coin> coinList;

    public CoinsDialog(Context context, List<Coin> coins,
        RecyclerAdapterCoinSimple.OnItemClickListener listener) {
        super(context);
        setContentView(R.layout.dialog_list_coin);
        filterText = findViewById(R.id.dialog_list_coin_edit_text);
        TextWatcher filterTextWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                int count) {
                adapter.getFilter().filter(s);
            }
        };
        filterText.addTextChangedListener(filterTextWatcher);
        recyclerView = findViewById(R.id.recycler_dialog_coins);
        coinList = new ArrayList<>();
        adapter = new RecyclerAdapterCoinSimple(context, listener);
        adapter.setCoins(coins);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void cancel() {
        super.cancel();
        filterText.setText("");
    }

}
