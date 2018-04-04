package com.nafaaazaiez.cryptofolder.view.listener;

import static com.nafaaazaiez.cryptofolder.Utils.Constants.ACTION;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.DETAILS;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.EDIT;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.PURCHASE_ID;
import static com.nafaaazaiez.cryptofolder.Utils.Constants.SOURCE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.PopupMenu;
import com.nafaaazaiez.cryptofolder.R;
import com.nafaaazaiez.cryptofolder.db.DBRepository;
import com.nafaaazaiez.cryptofolder.db.Purchase;
import com.nafaaazaiez.cryptofolder.view.activity.DetailsActivity;
import com.nafaaazaiez.cryptofolder.view.activity.PurchaseActivity;

/**
 * Created by Ussef on 22/01/2018.
 */
public class MenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

    private final DetailsActivity mContext;
    private final Purchase mPurchase;
    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    DBRepository dbRepository = new DBRepository(mContext);
                    dbRepository.deletePurchase(mPurchase.getId());
                    mContext.bindViews();
                    mContext.updatePurchases();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    public MenuItemClickListener(DetailsActivity context, Purchase item) {
        this.mContext = context;
        this.mPurchase = item;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_purchase_edit:
//                Toast.makeText(mContext, mContext.getString(R.string.edit) + mPurchase.getId(),
//                    Toast.LENGTH_SHORT).show();
                Intent i = new Intent(mContext, PurchaseActivity.class);
                i.putExtra(SOURCE, DETAILS);
                i.putExtra(ACTION, EDIT);
                i.putExtra(PURCHASE_ID, mPurchase.getId());
                mContext.startActivity(i);
                return true;
            case R.id.action_purchase_delete:
//                Toast.makeText(mContext, mContext.getString(R.string.delete) + mPurchase.getId(),
//                    Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.delete_dialog_message)
                    .setPositiveButton(R.string.yes, dialogClickListener)
                    .setNegativeButton(R.string.no, dialogClickListener).show();
                return true;

            default:
        }
        return false;
    }
}
