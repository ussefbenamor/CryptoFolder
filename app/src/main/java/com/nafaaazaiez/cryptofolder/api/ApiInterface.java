package com.nafaaazaiez.cryptofolder.api;

import com.nafaaazaiez.cryptofolder.api.response.CoinListResponse;
import com.nafaaazaiez.cryptofolder.api.response.ConvertResponse;
import com.nafaaazaiez.cryptofolder.api.response.PriceMutltiFullResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ussef on 23/01/2018.
 */

public interface ApiInterface {

    /**
     * https://min-api.cryptocompare.com/data/price?fsym=USD&tsyms=EUR
     */
    @GET("/data/price")
    Call<ConvertResponse> convertUSDtoEURO(@Query("fsym") String fsyms,
        @Query("tsyms") String tsyms);

    @GET("/data/all/coinlist")
    Call<CoinListResponse> findAllCoins();

    /**
     * https://min-api.cryptocompare.com/data/pricemultifull?fsyms=BTC&tsyms=USD,EUR
     */
    @GET("/data/pricemultifull")
    Call<PriceMutltiFullResponse> findPriceMutltiFull(@Query("fsyms") String fsyms,
        @Query("tsyms") String tsyms);

}
