package com.nafaaazaiez.cryptofolder.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.nafaaazaiez.cryptofolder.App;
import com.nafaaazaiez.cryptofolder.Utils.CalculUtils;
import com.nafaaazaiez.cryptofolder.Utils.Constants;
import com.nafaaazaiez.cryptofolder.Utils.SharedPrefsUtils;
import com.nafaaazaiez.cryptofolder.db.CoinDao.Properties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.greenrobot.greendao.query.Query;

/**
 * Created by Ussef on 21/01/2018.
 */

public class DBRepository {

    private final PurchaseDao purchaseDao;
    private final CoinDao coinDao;
    private final AlertDao alertDao;
    private final SharedPrefsUtils prefsUtils;
    private final SharedPreferences sharedPref;
    private final String langPref;
    private double usdValuePref;
    private Query<Purchase> purchaseQuery;
    private String currencyPref;

    public DBRepository(Context context) {

        DaoSession daoSession = ((App) context.getApplicationContext()).getDaoSession();
        this.purchaseDao = daoSession.getPurchaseDao();
        this.coinDao = daoSession.getCoinDao();
        this.alertDao = daoSession.getAlertDao();
        prefsUtils = SharedPrefsUtils.getInstance(context.getApplicationContext());
        sharedPref =
            PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        langPref = sharedPref.getString("languages_list", Constants.USD);

    }

    public double getUsdValuePref() {
        if (langPref.equals(Constants.FR)) {
            usdValuePref = CalculUtils.strFrToDbl(prefsUtils.getUSDValue());
        } else {
            usdValuePref = CalculUtils.strToDbl(prefsUtils.getUSDValue());
        }
        return usdValuePref;
    }

    public List<Purchase> findAllPurchasesByCoinId(long coinId) {

        purchaseQuery = this.purchaseDao.queryBuilder()
            .where(PurchaseDao.Properties.CoinId.eq(coinId))
            .orderDesc(PurchaseDao.Properties.Date).build();
        return purchaseQuery.list();
    }


    public int countAllPurchasesByCoinId(long coinId) {
        purchaseQuery = this.purchaseDao.queryBuilder()
            .where(PurchaseDao.Properties.CoinId.eq(coinId))
            .orderDesc(PurchaseDao.Properties.Date).build();
        return purchaseQuery.list().size();
    }


    public List<Coin> findAllCoins() {
        return coinDao.loadAll();
    }

    public List<Coin> findAllCoinsWithPurchases() {
        List<Purchase> purchaseList = purchaseDao.loadAll();
        List<Coin> coinList = new ArrayList<>();
        HashMap<String, Coin> coinHashMap = new HashMap<>();
        for (Purchase purchase : purchaseList) {
            coinHashMap
                .put(purchase.getCoinId().toString(), this.findCoinWithId(purchase.getCoinId()));
        }

        for (Entry<String, Coin> entry : coinHashMap.entrySet()) {
            String key = entry.getKey();
            Coin value = entry.getValue();
            coinList.add(value);
        }
        return coinList;
    }

    public List<String> findAllCoinsNamesWithPurchases() {
        List<Purchase> purchaseList = purchaseDao.loadAll();
        List<String> coinList = new ArrayList<>();
        HashMap<String, Coin> coinHashMap = new HashMap<>();
        for (Purchase purchase : purchaseList) {
            coinHashMap
                .put(purchase.getCoinId().toString(), this.findCoinWithId(purchase.getCoinId()));
        }

        for (Entry<String, Coin> entry : coinHashMap.entrySet()) {
            Coin coin = entry.getValue();
            coinList.add(coin.getName());
        }
        return coinList;
    }

    public Coin findCoinWithId(long coinId) {
        return coinDao.load(coinId);
    }

    public Purchase findPurchaseWithId(long purchaseId) {
        return purchaseDao.load(purchaseId);
    }

    public String[] findAllCoinsNames() {
        List<Coin> coinList = this.coinDao.loadAll();
        List<String> names = new ArrayList<>();
        for (Coin coin : coinList) {
            names.add(coin.getName());
        }
        String[] stockArr = new String[names.size()];
        stockArr = names.toArray(stockArr);
        return stockArr;
    }

    public double getTotalPricePurchases(long coinId) {
        double mSum = 0;
        currencyPref = sharedPref.getString(Constants.CURRENCY_LIST, Constants.USD);
        if (langPref.equals(Constants.FR)) {
            usdValuePref = CalculUtils.strFrToDbl(prefsUtils.getUSDValue());
        } else {
            usdValuePref = CalculUtils.strToDbl(prefsUtils.getUSDValue());
        }
        List<Purchase> purchaseList = this.purchaseDao.queryBuilder().
            where(PurchaseDao.Properties.CoinId.eq(coinId)).list();
        for (Purchase purchase : purchaseList) {
            mSum += CalculUtils
                .currentVal(purchase.getUnitPrice(), currencyPref, purchase.getCurrency(),
                    usdValuePref) * purchase.getAmount();
        }
        return mSum;
    }

    public double getTotalAmountPurchases(long coinId) {
        double mSum = 0;
        List<Purchase> purchaseList = purchaseDao.queryBuilder().
            where(PurchaseDao.Properties.CoinId.eq(coinId)).list();
        for (Purchase purchase : purchaseList) {
            mSum += purchase.getAmount();
        }
        return mSum;
    }

    public double getAvgUnitPricePurchases(long coinId) {
        double mSum = 0, mCount = 0;
        currencyPref = sharedPref.getString(Constants.CURRENCY_LIST, Constants.USD);
        if (langPref.equals(Constants.FR)) {
            usdValuePref = CalculUtils.strFrToDbl(prefsUtils.getUSDValue());
        } else {
            usdValuePref = CalculUtils.strToDbl(prefsUtils.getUSDValue());
        }
        List<Purchase> purchaseList = this.purchaseDao.queryBuilder().
            where(PurchaseDao.Properties.CoinId.eq(coinId)).list();
        for (Purchase purchase : purchaseList) {
            mSum += CalculUtils
                .currentVal(purchase.getUnitPrice(), currencyPref, purchase.getCurrency(),
                    usdValuePref) * purchase.getAmount();
            mCount += purchase.getAmount();
        }
        return mCount != 0 ? mSum / mCount : 0;
    }

    public long getCoinIdFromName(String name) {
        Query<Coin> coinQuery = this.coinDao.queryBuilder().
            where(CoinDao.Properties.Name.eq(name)).build();
        Coin mCoin = coinQuery.unique();
        return mCoin != null ? mCoin.getId() : -1;
    }

    public Coin getCoinFromName(String name) {
        Query<Coin> coinQuery = this.coinDao.queryBuilder().
            where(CoinDao.Properties.Name.eq(name)).build();
        Coin mCoin = coinQuery.uniqueOrThrow();
        return mCoin;
    }

    public long getCoinExistsByName(String name) {
        return this.coinDao.queryBuilder().
            where(Properties.Name.eq(name)).count();
    }

    public String getCoinNameFromId(long id) {
        Query<Coin> coinQuery = this.coinDao.queryBuilder().
            where(CoinDao.Properties.Id.eq(id)).build();
        Coin mCoin = coinQuery.uniqueOrThrow();
        return mCoin.getName();
    }

    public long insertPurchase(String coinName, double amount, double unitPrice, String platform,
        String date) {
        long result;
        Purchase purchase = new Purchase();
        currencyPref = sharedPref.getString(Constants.CURRENCY_LIST, Constants.USD);
        purchase.setUnitPrice(unitPrice);
        purchase.setPlatform(platform);
        purchase.setAmount(amount);
        purchase.setCoinId(this.getCoinIdFromName(coinName));
        purchase.setCurrency(currencyPref);
        purchase.setDate(date);
        result = this.purchaseDao.insert(purchase);
        return result;
    }

    public long insertCoin(String name, String coinName, String logoURL, double minDayPrice,
        double maxDayPrice, double actualPrice, String creationDate) {

        long mResult;
        Coin mCoin = new Coin();
        mCoin.setName(name);
        mCoin.setCoinName(coinName);
        mCoin.setLogoURL(logoURL);
        mCoin.setActualPrice(actualPrice);
        mCoin.setMaxDayPrice(maxDayPrice);
        mCoin.setMinDayPrice(minDayPrice);
        mCoin.setCreationDate(creationDate);
        mResult = this.coinDao.insert(mCoin);
        return mResult;
    }

    public long insertAlert(double above, double below, boolean enabled, String currency,
        Long coinId) {
        long mResult;
        Alert mAlert = new Alert();
        mAlert.setAbove(above);
        mAlert.setBelow(below);
        mAlert.setEnabled(enabled);
        mAlert.setCurrency(currency);
        mAlert.setCoinId(coinId);
        mResult = this.alertDao.insert(mAlert);
        return mResult;
    }

    public void updateAlert(double above, double below, boolean enabled, String currency,
        Long coinId) {
        Alert mAlert = this.findAlertWithCoinId(coinId);
        mAlert.setAbove(above);
        mAlert.setBelow(below);
        mAlert.setEnabled(enabled);
        mAlert.setCurrency(currency);
        mAlert.setCoinId(coinId);
        this.alertDao.update(mAlert);
    }

    public long findCountAlertWithCoinId(long coinId) {
        long alertCount = this.alertDao.queryBuilder()
            .where(AlertDao.Properties.CoinId.eq(coinId))
            .orderDesc(AlertDao.Properties.Id).count();
        return alertCount;
    }

    public List<Alert> findAllAlerts() {
        return this.alertDao.loadAll();
    }

    public Alert findAlertWithCoinId(long coinId) {
        Query<Alert> alertQuery = this.alertDao.queryBuilder()
            .where(AlertDao.Properties.CoinId.eq(coinId))
            .orderDesc(AlertDao.Properties.Id).build();
        return alertQuery.uniqueOrThrow();
    }

    public void updatePurchase(long id, String coinName, double amount, double unitPrice,
        String platform,
        String date) {
        Purchase purchase = new Purchase();
        currencyPref = sharedPref.getString(Constants.CURRENCY_LIST, Constants.USD);
        purchase.setId(id);
        purchase.setUnitPrice(unitPrice);
        purchase.setPlatform(platform);
        purchase.setAmount(amount);
        purchase.setCurrency(currencyPref);
        purchase.setCoinId(this.getCoinIdFromName(coinName));
        purchase.setDate(date);
        this.purchaseDao.update(purchase);
    }

    public void updateCoin(String name, String coinName, String logoURL,
        double minDayPrice,
        double maxDayPrice, double actualPrice, String creationDate) {
        Coin mCoin = new Coin();
        Coin mCoinInitial = this.findCoinWithId(this.getCoinIdFromName(name));
        mCoin.setId(this.getCoinIdFromName(name));
        mCoin.setName(name);
        mCoin.setCoinName(coinName.equals("") ? mCoinInitial.getCoinName() : coinName);
        mCoin.setLogoURL(logoURL.equals("") ? mCoinInitial.getLogoURL() : logoURL);
        mCoin.setCreationDate(
            creationDate.equals("") ? mCoinInitial.getCreationDate() : creationDate);
        mCoin.setActualPrice(actualPrice == 0 ? mCoinInitial.getActualPrice() : actualPrice);
        mCoin.setMaxDayPrice(maxDayPrice == 0 ? mCoinInitial.getMaxDayPrice() : maxDayPrice);
        mCoin.setMinDayPrice(minDayPrice == 0 ? mCoinInitial.getMinDayPrice() : minDayPrice);
        this.coinDao.update(mCoin);
    }


    public void deletePurchase(long purchaseId) {
        this.purchaseDao.deleteByKey(purchaseId);
    }

    public void deleteCoin(long coinId) {
        this.coinDao.deleteByKey(coinId);
    }

    public void clearAll() {
        this.purchaseDao.deleteAll();
        this.coinDao.deleteAll();
    }

    public void deleteAllCoins() {
        this.coinDao.deleteAll();
    }

    public void deleteAllPurchases() {
        this.purchaseDao.deleteAll();
    }

    public double getTotalPurchases() {
        double mSum = 0;
        currencyPref = sharedPref.getString(Constants.CURRENCY_LIST, Constants.USD);
        if (langPref.equals(Constants.FR)) {
            usdValuePref = CalculUtils.strFrToDbl(prefsUtils.getUSDValue());
        } else {
            usdValuePref = CalculUtils.strToDbl(prefsUtils.getUSDValue());
        }
        List<Purchase> purchaseList = this.purchaseDao.loadAll();
        for (Purchase purchase : purchaseList) {
            mSum += CalculUtils
                .currentVal(purchase.getUnitPrice(), currencyPref, purchase.getCurrency(),
                    usdValuePref) * purchase.getAmount();
        }
        return mSum;
    }

    public double getActualBalance() {
        double mSum = 0;
        List<Coin> coinList = this.coinDao.loadAll();
        for (Coin mCoin : coinList) {
            mSum += mCoin.getActualPrice() * this.getTotalAmountPurchases(mCoin.getId());
        }
        return mSum;
    }

}
