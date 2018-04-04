package com.nafaaazaiez.cryptofolder.Utils;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ussef on 19/01/2018.
 */

public class ViewUtils {

    public static void enableClick(boolean state, View... textViews) {

        for (View view : textViews) {
            if (view instanceof Button) {
                view.setClickable(state);
            } else if (view instanceof EditText) {
                view.setClickable(state);
                view.setFocusable(state);
                view.setFocusableInTouchMode(state);
            } else if (view instanceof ImageView) {
                view.setClickable(false);
            }
        }
    }

    public static boolean checkEmpty(View... textViews) {
        boolean result = false;
        for (View view : textViews) {
            if (view instanceof Button) {
                result |= TextUtils.isEmpty(((Button) view).getText().toString());
            } else if (view instanceof EditText) {
                result |= TextUtils.isEmpty(((EditText) view).getText().toString());
            } else if (view instanceof TextView) {
                result |= TextUtils.isEmpty(((TextView) view).getText().toString());
            }
        }
        return result;
    }

    /**
     * Show a message in a nice interface rather that a toast
     *
     * @param v parent view
     * @param message the content of the message
     * @param duration 0 SHORT_DURATION, 1 LONG_DURATION
     */
    public static void showSnackbar(View v, String message, int duration) {
        Snackbar.make(v, message, duration)
            .show();
    }

}
