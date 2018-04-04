package com.nafaaazaiez.cryptofolder.Utils;

import java.text.DecimalFormat;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Ussef on 22/01/2018.
 */

public class CalculUtils {

    public static final String YYYY_MM_DD_HH_MM = "yyyy/MM/dd HH:mm";

    public static String percentage(double num1, double num2) {
        return new DecimalFormat("#####.#").format(((num1 - num2) / num1) * 100) + " %";
    }

    public static String nowToStr() {

        DateTime date = DateTime.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern(YYYY_MM_DD_HH_MM);
        String str = date.toString(fmt);
        return str;
    }

    public static String dblToStr(double num) {
        return new DecimalFormat(Constants.DOUBLE_FORMAT).format(num);
    }

    public static double strFrToDbl(String value) {
        String s = value.replaceAll(",", ".");
        s = s.replaceAll(" ", "");
        if (isNumeric(s)) {
            return Double.parseDouble(s);
        } else {
            return 0;
        }
    }

    public static double strToDbl(String value) {
        String s = value.replaceAll(",", "");
        if (isNumeric(s)) {
            return Double.parseDouble(s);
        } else {
            return 0;
        }
    }

    /**
     * Convert the purchase price
     *
     * @param purchasePrice purchase unit price
     * @param defaultCurr the current currency in the app
     * @param purchaseCurr the currency on purchase
     * @param USDToEUR the value of USD converted in EUR
     * @return double indicating the current price
     */
    public static double currentVal(double purchasePrice, String defaultCurr, String purchaseCurr,
        double USDToEUR) {
        if (defaultCurr.equals(purchaseCurr) && defaultCurr.equals(Constants.USD)) {//USD-USD
            return purchasePrice;
        } else if (defaultCurr.equals(purchaseCurr) && defaultCurr
            .equals(Constants.EURO)) {//EUR-EUR
            return purchasePrice;
        } else if (!defaultCurr.equals(purchaseCurr) && defaultCurr
            .equals(Constants.USD)) {//USD-EUR
            return purchasePrice / USDToEUR;
        } else if (!defaultCurr.equals(purchaseCurr) && defaultCurr
            .equals(Constants.EURO)) {//EUR-USD
            return purchasePrice * USDToEUR;
        } else {
            return purchasePrice;
        }
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
