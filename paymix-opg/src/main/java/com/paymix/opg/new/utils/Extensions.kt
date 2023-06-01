package com.walletmix.paymixbusiness.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.*
import android.text.style.AlignmentSpan
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import java.nio.charset.StandardCharsets


fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {

    val centeredText: Spannable = SpannableString(message)
    centeredText.setSpan(
        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
        0, message.length - 1,
        Spannable.SPAN_INCLUSIVE_INCLUSIVE
    )
    Toast.makeText(this.applicationContext, centeredText, duration).show()
}

fun killAllToast(toastList:ArrayList<Toast>){
    for (t in toastList) {
        t.cancel()
    }
    toastList.clear()
}


fun String.encodeToBase64(): String {
    return Base64.encodeToString(this.toByteArray(), Base64.DEFAULT)
}

fun String.decodeFromBase64(): String {
    return String(Base64.decode(this.toByteArray(), Base64.DEFAULT), StandardCharsets.UTF_8)
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun AppCompatEditText.addAsteriskSign() {
    val colored = "*"
    val builder = SpannableStringBuilder()
    builder.append(this.hint)
    val start = builder.length
    builder.append(colored)
    val end = builder.length

    builder.setSpan(
        ForegroundColorSpan(Color.RED), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.hint = builder
}

fun TextView.addAsteriskSign() {
    val colored = "*"
    val builder = SpannableStringBuilder()
    builder.append(this.text)
    val start = builder.length
    builder.append(colored)
    val end = builder.length

    builder.setSpan(
        ForegroundColorSpan(Color.RED), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.text = builder
}

fun Intent.clearStack() {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
}

fun AppCompatButton.disableFor1Sec() {
    this.isClickable = false
    this.postDelayed({ this.isClickable = true }, 1000)
}

fun Activity.dismissKeyboard() {
    val inputMethodManager = getSystemService( Context.INPUT_METHOD_SERVICE ) as InputMethodManager
    if( inputMethodManager.isAcceptingText )
        inputMethodManager.hideSoftInputFromWindow( this.currentFocus!!.windowToken, /*flags:*/ 0)
}
