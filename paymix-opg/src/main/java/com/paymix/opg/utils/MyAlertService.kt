package com.paymix.opg.utils


import android.app.AlertDialog
import android.content.Context

import android.widget.Toast



class MyAlertService{

    private var mToast: Toast? = null
    private var mAlert: AlertDialog? = null

    fun showToast(context: Context, message: String) {
        try {
            if (!mToast!!.view?.isShown!!) {
                createToast(context, message)
            } else {
                mToast!!.setText(message)
            }
        } catch (e: Exception) {
            createToast(context, message)
        }

    }

    fun showToast(context: Context, resId: Int) {
        showToast(context, context.getString(resId))
    }


    private fun createToast(context: Context, message: String) {
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        mToast!!.show()
    }

    fun showAlert(
        context: Context,
        title: String?,
        message: String
    ) {
        mAlert?.let {
            if (mAlert!!.isShowing) {
                mAlert!!.dismiss()
            }
            mAlert = null
        }

        mAlert = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Okey") { dialog, _ ->
                dialog.dismiss()
                mAlert = null
            }.create()
        mAlert!!.show()
    }


    fun showConfirmationAlert(
        context: Context,
        title: String?,
        message: String,
        negativeBtn: String?,
        positiveBtn: String,
        alertListener: AlertListener?
    ) {
        mAlert?.let { alert ->
            if (alert.isShowing) {
                alert.dismiss()
            }
            mAlert = null
        }
        mAlert = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setNegativeButton(negativeBtn) { dialog, _ ->
                alertListener?.let {
                    alertListener.negativeBtnDidTapped()
                    dialog.dismiss()
                    mAlert = null
                }
            }
            .setPositiveButton(positiveBtn) { dialog, _ ->
                alertListener?.let {
                    alertListener.positiveBtnDidTapped()
                }
            }
            .create()
        mAlert!!.show()
    }

    fun showConfirmationAlertwithOutNegitive(
        context: Context,
        title: String?,
        message: String,
        positiveBtn: String,
        alertListener: AlertListener?
    ) {
        mAlert?.let { alert ->
            if (alert.isShowing) {
                alert.dismiss()
            }
            mAlert = null
        }
        mAlert = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(positiveBtn) { dialog, _ ->
                alertListener?.let {
                    alertListener.positiveBtnDidTapped()
                }
            }
            .create()
        mAlert!!.show()
    }

    fun onDestroy() {
        if (mAlert != null) {
            mAlert = null
        }
        if (mToast != null) {
            mToast = null
        }
    }

    interface AlertListener {

        fun negativeBtnDidTapped()

        fun positiveBtnDidTapped()
    }
}