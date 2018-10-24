package com.monamedia.vmt.view.confirmDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by android on 29/11/2017.
 */

public class ConfirmDialog extends AlertDialog.Builder {
    public ConfirmDialog(Context context, String title, String msg, String msgConfirm, String msgCancel,
                         final ConfirmDialogCallBack callBack) {
        super(context);
        setTitle(title);
        setMessage(msg);
        setPositiveButton(msgConfirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
                callBack.onConfirm();
            }
        });
        setNegativeButton(msgCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
                callBack.onCancel();
            }
        });
    }
}
