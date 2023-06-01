package com.walletmix.paymixbusiness.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.walletmix.paymixbusiness.R


class Navigator {

    companion object {
        val sharedInstance = Navigator()
    }

    fun <T> navigate(context: Context,target: Class<T>) {
        val intent = Intent(context, target)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
        (context as AppCompatActivity).overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    fun <T> back(context: Context, target: Class<T>) {
        val intent = Intent(context, target)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
        (context as AppCompatActivity).overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    fun <T> navigateWithBundle(
        context: Context,
        target: Class<T>,
        bundleKey: String,
        bundle: Bundle
    ) {
        val intent = Intent(context, target)
        intent.putExtra(bundleKey, bundle)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }

    fun <T> backWithBundle(
        context: Context,
        target: Class<T>,
        bundleKey: String,
        bundle: Bundle
    ) {
        val intent = Intent(context, target)
        intent.putExtra(bundleKey, bundle)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
        (context as AppCompatActivity).overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }


}