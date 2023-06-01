package com.walletmix.paymixbusiness.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * Created  on 19/08/2019.
 */
class KeyboardUtils {

    companion object {
        val sharedInstance = KeyboardUtils()
    }

    fun hideSoftInput(activity: AppCompatActivity) {
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        val imm = activity
                .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showSoftInput(edit: EditText, context: Context) {
        edit.isFocusable = true
        edit.isFocusableInTouchMode = true
        edit.requestFocus()
        val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(edit, 0)
    }

    fun toggleSoftInput(context: Context) {
        val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}