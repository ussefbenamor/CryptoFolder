package com.nafaaazaiez.cryptofolder.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Created by Ussef on 25/01/2018.
 */

public class PriceMutltiFullResponse {

    @SerializedName("RAW")
    public Map<String, Map<String, Raw>> raw;
    @SerializedName("DISPLAY")
    public Map<String, Map<String, Display>> display;

    public class Raw {

        /**
         * Type
         */
        @SerializedName("TYPE")
        public String type;
        /**
         * Shortened names of markets used for data
         */
        @SerializedName("MARKET")
        public String market;
        /**
         * From symbol in trading pair
         */
        @SerializedName("FROMSYMBOL")
        public String fromSymbol;
        /**
         * To symbol in trading pair
         */
        @SerializedName("TOSYMBOL")
        public String toSymbol;
        /**
         * Number of flags used
         */
        @SerializedName("FLAGS")
        public int flags;
        /**
         * Coin price
         */
        @SerializedName("PRICE")
        public double price;
        /**
         * Unix time of last update
         */
        @SerializedName("LASTUPDATE")
        public int lastUpdate;
        /**
         * Last volume
         */
        @SerializedName("LASTVOLUME")
        public double lastVolume;
        /**
         * Last volume to
         */
        @SerializedName("LASTVOLUMETO")
        public double lastVolumeTo;
        /**
         * Last trade ID
         */
        @SerializedName("LASTTRADEID")
        public double lastTradeID;
        /**
         * Volume of day
         */
        @SerializedName("VOLUMEDAY")
        public double volumeDay;
        /**
         * Volume to of day
         */
        @SerializedName("VOLUMEDAYTO")
        public double volumeDayTo;
        /**
         * 24 hour volume
         */
        @SerializedName("VOLUME24HOUR")
        public double volume24Hour;
        /**
         * 24 hour volume to
         */
        @SerializedName("VOLUME24HOURTO")
        public double volume24HourTo;
        /**
         * Day open
         */
        @SerializedName("OPENDAY")
        public double openDay;
        /**
         * Day high
         */
        @SerializedName("HIGHDAY")
        public double highDay;
        /**
         * Day low
         */
        @SerializedName("LOWDAY")
        public double lowDay;
        /**
         * 24 hour open
         */
        @SerializedName("OPEN24HOUR")
        public double open24Hour;
        /**
         * 24 hour high
         */
        @SerializedName("HIGH24HOUR")
        public double high24Hour;
        /**
         * 24 hour low
         */
        @SerializedName("LOW24HOUR")
        public double low24Hour;
        /**
         * Last market
         */
        @SerializedName("LASTMARKET")
        public String lastMarket;
        /**
         * 24 hour change
         */
        @SerializedName("CHANGE24HOUR")
        public double change24Hour;
        /**
         * 24 hour percent change
         */
        @SerializedName("CHANGEPCT24HOUR")
        public double changePct24Hour;
        /**
         * Day change
         */
        @SerializedName("CHANGEDAY")
        public double changeDay;
        /**
         * Day percent change
         */
        @SerializedName("CHANGEPCTDAY")
        public double changePctDay;
        /**
         * Coin supply
         */
        @SerializedName("SUPPLY")
        public double supply;
        /**
         * Coin market capitalization
         */
        @SerializedName("MKTCAP")
        public double marketCap;
        /**
         * 24 hour total volume
         */
        @SerializedName("TOTALVOLUME24H")
        public double totalVolume24Hour;
        /**
         * 24 hour total volume to
         */
        @SerializedName("TOTALVOLUME24HTO")
        public double totalVolume24HourTo;

        @Override
        public String toString() {
            return String.format("%s-%s: %f", fromSymbol, toSymbol, volume24Hour);
        }
    }

    public class Display {

        public class Example {

            @SerializedName("FROMSYMBOL")
            @Expose
            private String fromSymbol;
            @SerializedName("TOSYMBOL")
            @Expose
            private String toSymbol;
            @SerializedName("MARKET")
            @Expose
            private String market;
            @SerializedName("PRICE")
            @Expose
            private String price;
            @SerializedName("LASTUPDATE")
            @Expose
            private String lastUpdate;
            @SerializedName("LASTVOLUME")
            @Expose
            private String lastVolume;
            @SerializedName("LASTVOLUMETO")
            @Expose
            private String lastVolumeTo;
            @SerializedName("LASTTRADEID")
            @Expose
            private String lastTradeId;
            @SerializedName("VOLUMEDAY")
            @Expose
            private String volumeDay;
            @SerializedName("VOLUMEDAYTO")
            @Expose
            private String volumeDayTo;
            @SerializedName("VOLUME24HOUR")
            @Expose
            private String volume24Hour;
            @SerializedName("VOLUME24HOURTO")
            @Expose
            private String volume24HourTo;
            @SerializedName("OPENDAY")
            @Expose
            private String openDay;
            @SerializedName("HIGHDAY")
            @Expose
            private String highDay;
            @SerializedName("LOWDAY")
            @Expose
            private String lowDay;
            @SerializedName("OPEN24HOUR")
            @Expose
            private String open24Hour;
            @SerializedName("HIGH24HOUR")
            @Expose
            private String high24Hour;
            @SerializedName("LOW24HOUR")
            @Expose
            private String low24Hour;
            @SerializedName("LASTMARKET")
            @Expose
            private String lastMarket;
            @SerializedName("CHANGE24HOUR")
            @Expose
            private String change24Hour;
            @SerializedName("CHANGEPCT24HOUR")
            @Expose
            private String changePct24Hour;
            @SerializedName("CHANGEDAY")
            @Expose
            private String changeDay;
            @SerializedName("CHANGEPCTDAY")
            @Expose
            private String changePctDay;
            @SerializedName("SUPPLY")
            @Expose
            private String supply;
            @SerializedName("MKTCAP")
            @Expose
            private String mktCap;
            @SerializedName("TOTALVOLUME24H")
            @Expose
            private String totalVolume24H;
            @SerializedName("TOTALVOLUME24HTO")
            @Expose
            private String totalVolume24HTo;
        }
    }
}