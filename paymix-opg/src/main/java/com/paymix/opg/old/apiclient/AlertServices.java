package com.paymix.opg.old.apiclient;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

class AlertServices {

   static void showAlertDialog(Context context,
                                      final String alertTitle,
                                      final String alertMessage,
                                      final String positiveBtnText,
                                      final String negativeBtnText,
                                      final AlertDialogListener alertDialogListener) {
       AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
       alertDialog.setTitle(alertTitle);
       alertDialog.setMessage(alertMessage);
       alertDialog.setCancelable(false);
       alertDialog.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               if (alertDialogListener != null)
                   alertDialogListener.onPositiveButtonClicked();
               dialog.dismiss();
           }
       });
       alertDialog.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               if (alertDialogListener != null)
                   alertDialogListener.onNegativeButtonClicked();
               dialog.dismiss();
           }
       });
       alertDialog.show();
   }


   public interface AlertDialogListener {

       void onPositiveButtonClicked();

       void onNegativeButtonClicked();
   }

}
