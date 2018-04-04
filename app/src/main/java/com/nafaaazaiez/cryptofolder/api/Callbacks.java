package com.nafaaazaiez.cryptofolder.api;

import static com.nafaaazaiez.cryptofolder.Utils.Constants.DATA;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.FAILURE;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.OK_DATA_NULL;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.OK_DATA_OK;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import com.nafaaazaiez.cryptofolder.R;
import com.nafaaazaiez.cryptofolder.Utils.CalculUtils;
import com.nafaaazaiez.cryptofolder.Utils.ConnectivityUtils;
import com.nafaaazaiez.cryptofolder.Utils.Constants;
import com.nafaaazaiez.cryptofolder.Utils.SharedPrefsUtils;
import com.nafaaazaiez.cryptofolder.api.response.CoinListResponse;
import com.nafaaazaiez.cryptofolder.api.response.CoinListResponse.CoinEntry;
import com.nafaaazaiez.cryptofolder.api.response.ConvertResponse;
import com.nafaaazaiez.cryptofolder.api.response.PriceMutltiFullResponse;
import com.nafaaazaiez.cryptofolder.api.response.PriceMutltiFullResponse.Raw;
import com.nafaaazaiez.cryptofolder.db.DBRepository;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ussef on 05/02/2018.
 */

public class Callbacks {

    private static final String TAG = Callbacks.class.getSimpleName();

    private final ApiInterface apiService;
    private final DBRepository mDbRepository;
    private final SharedPreferences sharedPref;
    private final String currencyPref;
    private final Context mContext;
    private final SharedPrefsUtils prefsUtils;

    public Callbacks(Context context) {
        this.mContext = context;
        prefsUtils = SharedPrefsUtils.getInstance(this.mContext);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        mDbRepository = new DBRepository(this.mContext);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        currencyPref = sharedPref.getString(Constants.CURRENCY_LIST, Constants.USD);
    }

    public void initDB() {
        if (ConnectivityUtils.isConnected(this.mContext)) {
            final Callback<CoinListResponse> initCallback = new Callback<CoinListResponse>() {
                @Override
                public void onResponse(@NonNull Call<CoinListResponse> call,
                    @NonNull Response<CoinListResponse> response) {
                    int code = response.code();
                    if (code == 200) {
                        Log.d(TAG, "200");
                        CoinListResponse data = response.body();
                        if (data != null && data.response.equals(Constants.SUCCESS)) {
                            Log.d(TAG, DATA + data.response);
                            mDbRepository.deleteAllCoins();
                            for (Entry<String, CoinEntry> entry : data.coins.entrySet()) {
                                String key = entry.getKey();
                                CoinEntry value = entry.getValue();
                                Log.d(TAG, key);
                                mDbRepository.insertCoin(key, value.coinName,
                                    Constants.BASE_URL_IMG + value.imageUrl,
                                    0, 0,
                                    0, CalculUtils.nowToStr());
                            }
                        } else {
                            Log.d(TAG, FAILURE);
                        }
                    }
                    Log.d(TAG, code + "");
                }

                @Override
                public void onFailure(@NonNull Call<CoinListResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            };
            Call<CoinListResponse> initialCall = apiService.findAllCoins();
            initialCall.enqueue(initCallback);
            prefsUtils.enableDBInitialed(true);
        }
    }

    public void updateDB() {
        if (ConnectivityUtils.isConnected(this.mContext)) {
            final List<String> coins = mDbRepository.findAllCoinsNamesWithPurchases();
            final Callback<CoinListResponse> initDBCallback = new Callback<CoinListResponse>() {
                @Override
                public void onResponse(@NonNull Call<CoinListResponse> call,
                    @NonNull Response<CoinListResponse> response) {
                    int code = response.code();
                    if (code == 200) {
                        Log.d(TAG, "200");
                        CoinListResponse data = response.body();
                        if (data != null && data.response.equals(Constants.SUCCESS)) {
                            Log.d(TAG, mContext.getString(R.string.data_received) + data.response);

                            mDbRepository.deleteAllCoins();
                            for (Entry<String, CoinEntry> entry : data.coins.entrySet()) {
                                String key = entry.getKey();
                                CoinEntry value = entry.getValue();
                                Log.d(TAG, key);
                                mDbRepository.insertCoin(key, value.coinName,
                                    Constants.BASE_URL_IMG + value.imageUrl,
                                    0, 0,
                                    0, CalculUtils.nowToStr());

                            }
                            Log.d(TAG, OK_DATA_OK + data.response);
                        } else {
                            Log.d(TAG, OK_DATA_NULL + (data != null ? data.response : null));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CoinListResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, FAILURE);
                    t.printStackTrace();
                }
            };
            final Callback<CoinListResponse> updateDBCallback = new Callback<CoinListResponse>() {
                @Override
                public void onResponse(@NonNull Call<CoinListResponse> call,
                    @NonNull Response<CoinListResponse> response) {
                    int code = response.code();
                    if (code == 200) {
                        Log.d(TAG, "200");
                        CoinListResponse data = response.body();
                        if (data != null && data.response.equals(Constants.SUCCESS)) {
                            Log.d(TAG, mContext.getString(R.string.data_received) + data.response);
                            if (mDbRepository.findAllCoins().size() < data.coins.size()) {
                                for (Entry<String, CoinEntry> entry : data.coins.entrySet()) {
                                    String key = entry.getKey();
                                    CoinEntry value = entry.getValue();
                                    Log.d(TAG, key);
                                    if (mDbRepository.getCoinIdFromName(key) == -1) {
                                        mDbRepository.insertCoin(key, value.coinName,
                                            Constants.BASE_URL_IMG + value.imageUrl,
                                            0, 0,
                                            0, CalculUtils.nowToStr());
                                    }

                                }
                            }

                            Log.d(TAG, OK_DATA_OK + data.response);
                        } else {
                            Log.d(TAG, OK_DATA_NULL + (data != null ? data.response : null));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CoinListResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, FAILURE);
                    t.printStackTrace();
                }
            };

            final Callback<PriceMutltiFullResponse> priceCallback = new Callback<PriceMutltiFullResponse>() {
                public int mCounter;

                @Override
                public void onResponse(@NonNull Call<PriceMutltiFullResponse> call,
                    @NonNull Response<PriceMutltiFullResponse> response) {
                    int code = response.code();
                    if (code == 200) {
                        Log.d(TAG, "200");
                        PriceMutltiFullResponse data = response.body();
                        if (data != null && data.raw != null) {
                            for (Entry<String, Map<String, Raw>> entry : data.raw.entrySet()) {
                                String key = entry.getKey();
                                Map<String, Raw> value = entry.getValue();
                                Raw usdValues = value.get(currencyPref);
                                Log.d(TAG, key);
                                mDbRepository.updateCoin(key, "",
                                    "",
                                    usdValues.lowDay, usdValues.highDay,
                                    usdValues.price, "");
                            }
                            Log.d(TAG, OK_DATA_OK);


                        } else {
                            Log.d(TAG, "200 ok data null");

                        }
                    }
                    mCounter++;
                }

                @Override
                public void onFailure(@NonNull Call<PriceMutltiFullResponse> call,
                    @NonNull Throwable t) {
                    Log.d(TAG, FAILURE);
                    t.printStackTrace();

                }
            };
            Log.d(TAG, "Server");
            if (!prefsUtils.isDBInitialed()) {
                Call<CoinListResponse> initialCall = apiService.findAllCoins();
                initialCall.enqueue(initDBCallback);
                prefsUtils.enableDBInitialed(true);
            } else {
                Call<CoinListResponse> updateCall = apiService.findAllCoins();
                updateCall.enqueue(updateDBCallback);
            }
            final Callback<ConvertResponse> convertCallback = new Callback<ConvertResponse>() {
                @Override
                public void onResponse(@NonNull Call<ConvertResponse> call,
                    @NonNull Response<ConvertResponse> response) {
                    int code = response.code();
                    if (code == 200) {
                        Log.d(TAG, "200");
                        ConvertResponse data = response.body();
                        if (data != null && data.getEUR() != null) {
                            Log.d(TAG, DATA + data.toString());
                            prefsUtils.setUSDValue(CalculUtils.dblToStr(data.getEUR()));
                            Log.d(TAG, OK_DATA_OK);
                        } else {
                            Log.d(TAG, OK_DATA_NULL);
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ConvertResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, FAILURE);
                    t.printStackTrace();
                }
            };

            Call<ConvertResponse> convertCall = apiService
                .convertUSDtoEURO(Constants.USD, Constants.EURO);
            convertCall.enqueue(convertCallback);
            for (String coin :
                coins) {
                Call<PriceMutltiFullResponse> priceCall = apiService
                    .findPriceMutltiFull(coin, currencyPref);
                priceCall.enqueue(priceCallback);
                if (!priceCall.isExecuted()) {
                    do {
                    } while (!priceCall.isExecuted());
                }
            }
            Log.d(TAG, "HERE");
        }
    }

    public void getUpdatedPrice(String coin) {
        if (ConnectivityUtils.isConnected(this.mContext)) {
            final DBRepository mDbRepository = new DBRepository(this.mContext);

            final Callback<PriceMutltiFullResponse> priceCallback = new Callback<PriceMutltiFullResponse>() {
                public int mCounter;

                @Override
                public void onResponse(@NonNull Call<PriceMutltiFullResponse> call,
                    @NonNull Response<PriceMutltiFullResponse> response) {
                    int code = response.code();
                    if (code == 200) {
                        Log.d(TAG, "200");
                        PriceMutltiFullResponse data = response.body();
                        if (data != null && data.raw != null) {
                            for (Entry<String, Map<String, Raw>> entry : data.raw.entrySet()) {
                                String key = entry.getKey();
                                Map<String, Raw> value = entry.getValue();
                                Raw usdValues = value.get(currencyPref);
                                Log.d(TAG, key);
                                mDbRepository.updateCoin(key, "",
                                    "",
                                    usdValues.lowDay, usdValues.highDay,
                                    usdValues.price, "");
                            }
                            Log.d(TAG, OK_DATA_OK);

                        } else {
                            Log.d(TAG, "200 ok data null");

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PriceMutltiFullResponse> call,
                    @NonNull Throwable t) {
                    Log.d(TAG, FAILURE);
                    t.printStackTrace();
                    mCounter++;
                }
            };
            Log.d(TAG, "Server");
            Call<PriceMutltiFullResponse> priceCall = apiService
                .findPriceMutltiFull(coin, currencyPref);
            priceCall.enqueue(priceCallback);
            if (!priceCall.isExecuted()) {
                do {
                } while (!priceCall.isExecuted());
            }
        }
    }
}
