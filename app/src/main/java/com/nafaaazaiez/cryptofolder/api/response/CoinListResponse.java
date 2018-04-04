package com.nafaaazaiez.cryptofolder.api.response;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Created by Ussef on 23/01/2018.
 */

public class CoinListResponse {

    /**
     * Indicates request success
     */
    @SerializedName("Response")
    public String response;
    /**
     * A message provided by the API
     */
    @SerializedName("Message")
    public String message;
    /**
     * All coins provided by the API
     */
    @SerializedName("Data")
    public Map<String, CoinEntry> coins;

    /**
     * Represents a coin on CryptoCompare's website
     */
    public class CoinEntry {

        /**
         * The ID of the coin on CryptoCompare's website
         */
        @SerializedName("Id")
        public int id;
        /**
         * The URL for the coin on CryptoCompare's website
         */
        @SerializedName("Url")
        public String url;
        /**
         * The URL for the coin's image on CryptoCompare's website
         */
        @SerializedName("ImageUrl")
        public String imageUrl;
        /**
         * The coin's symbol
         */
        @SerializedName("Name")
        public String name;
        /**
         * The coin's symbol
         */
        @SerializedName("Symbol")
        public String symbol;
        /**
         * The coin's name
         */
        @SerializedName("CoinName")
        public String coinName;
        /**
         * The coin's full name
         */
        @SerializedName("FullName")
        public String fullName;
        /**
         * The coin's algorithm
         */
        @SerializedName("Algorithm")
        public String algorithm;
        /**
         * The type of proof used by the coin
         */
        @SerializedName("ProofType")
        public String proofType;
        /**
         * Indicates if the coin has been fully premined
         */
        @SerializedName("FullyPremined")
        public String fullyPremined;
        /**
         * Total supply of the coin
         */
        @SerializedName("TotalCoinSupply")
        public String totalCoinSupply;
        /**
         * The value of all the premined coins
         */
        @SerializedName("PreMinedValue")
        public String preMinedValue;
        /**
         * The total number of coins
         */
        @SerializedName("TotalCoinsFreeFloat")
        public String totalCoinsFreeFloat;
        /**
         * Sorting order of the coin on CryptoCompare's website
         */
        @SerializedName("SortOrder")
        public int sortOrder;
        /**
         * Indicates if the coin is sponsored on CryptoCompare's website
         */
        @SerializedName("Sponsored")
        public boolean sponsored;
    }

}
