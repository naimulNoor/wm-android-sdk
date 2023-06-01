package com.walletmix.paymixbusiness.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*

class AppAnalytics private constructor() {
    companion object {
        val sharedInstance = AppAnalytics()
    }

    // Add fund success >>>
    fun addPurchaseEvent(
        context: Context,
        amount: String,
        referenceId: String,
        paymentType: String
    ) {
        val firebaseParams = Bundle()
        firebaseParams.putString(FirebaseAnalytics.Param.CURRENCY, "BDT")
        firebaseParams.putString(FirebaseAnalytics.Param.VALUE, amount)
        firebaseParams.putString(FirebaseAnalytics.Param.TRANSACTION_ID, referenceId)
        firebaseParams.putString(FirebaseAnalytics.Param.ITEM_NAME, "easyload")
        firebaseParams.putString(FirebaseAnalytics.Param.PAYMENT_TYPE, paymentType)

        // Firebase
        FirebaseAnalytics.getInstance(context)
            .logEvent(FirebaseAnalytics.Event.PURCHASE, firebaseParams)

        // Facebook
        val facebookParams = Bundle()
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "BDT")
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "easyload")
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_VALUE_TO_SUM, amount)
        AppEventsLogger.newLogger(context)
            .logPurchase(amount.toBigDecimal(), Currency.getInstance("BDT"), facebookParams)
    }

    // Data pack purchase, Gift purchase, commission earn : Spend Reward Point
    fun addSpendCreditsEvent(
        context: Context,
        amount: Int,
        itemName: String
    ) {
        val itemParams = Bundle()
        itemParams.putString(FirebaseAnalytics.Param.CONTENT, itemName)
        itemParams.putString(FirebaseAnalytics.Param.CURRENCY, "POINT")
        itemParams.putInt(FirebaseAnalytics.Param.VALUE, amount)

        // Firebase
        FirebaseAnalytics.getInstance(context)
            .logEvent(FirebaseAnalytics.Event.SPEND_VIRTUAL_CURRENCY, itemParams)

        // Facebook
        val facebookParams = Bundle()
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT, itemName)
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "POINT")
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_VALUE_TO_SUM, "$amount")
        AppEventsLogger.newLogger(context)
            .logEvent(
                AppEventsConstants.EVENT_NAME_SPENT_CREDITS,
                amount.toDouble(),
                facebookParams
            )
    }


    // Successful mobile recharge
    fun addMobileRechargeEvent(
        context: Context,
        amount: String
    ) {
        // firebase
        val itemParams = Bundle()
        itemParams.putString(FirebaseAnalytics.Param.CURRENCY, "BDT")
        itemParams.putString(FirebaseAnalytics.Param.VALUE, amount)
        FirebaseAnalytics.getInstance(context)
            .logEvent("Mobile_Recharge", itemParams)

        // facebook
        val facebookParams = Bundle()
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "BDT")
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_VALUE_TO_SUM, amount)
        AppEventsLogger.newLogger(context)
            .logEvent("Mobile Recharge", amount.toDouble(), facebookParams)
    }

    // Successful mobile recharge
    fun addOfferRechargeEvent(
        context: Context,
        offerType: String,
        offerName: String,
        amount: String
    ) {
        // firebase
        val itemParams = Bundle()
        itemParams.putString(FirebaseAnalytics.Param.CURRENCY, "BDT")
        itemParams.putString(FirebaseAnalytics.Param.CONTENT_TYPE, offerType)
        itemParams.putString(FirebaseAnalytics.Param.CONTENT, offerName)
        itemParams.putString(FirebaseAnalytics.Param.VALUE, amount)
        FirebaseAnalytics.getInstance(context)
            .logEvent("Offer_Recharge", itemParams)

        // facebook
        val facebookParams = Bundle()
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "BDT")
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, offerType)
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT, offerName)
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_VALUE_TO_SUM, amount)
        AppEventsLogger.newLogger(context)
            .logEvent("Offer Recharge", amount.toDouble(), facebookParams)
    }

    // View offer list list
    fun addTapOfferCategoryEvent(
        context: Context,
        itemListName: String
    ) {
        // firebase
        val itemParams = Bundle()
        itemParams.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, itemListName)
        Log.d("EventTap",itemListName)
        FirebaseAnalytics.getInstance(context)
            .logEvent("Tap_$itemListName" + "_Offer", itemParams)

        // facebook
        val facebookParams = Bundle()
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, itemListName)
        AppEventsLogger.newLogger(context)
            .logEvent("Tap $itemListName Offer", facebookParams)
    }

    // View a specific offer details >>
    fun addViewOfferEvent(
        context: Context,
        itemId: Int,
        itemCategory: String,
        itemName: String,
        amount: String
    ) {
        // Firebase
        val itemParams = Bundle()
        itemParams.putInt(FirebaseAnalytics.Param.ITEM_ID, itemId)
        itemParams.putString(FirebaseAnalytics.Param.CONTENT_TYPE, itemCategory)
        itemParams.putString(FirebaseAnalytics.Param.CONTENT, itemName)
        itemParams.putString(FirebaseAnalytics.Param.CURRENCY, "BDT")
        itemParams.putString(FirebaseAnalytics.Param.VALUE, amount)

        FirebaseAnalytics.getInstance(context)
            .logEvent(FirebaseAnalytics.Event.SPEND_VIRTUAL_CURRENCY, itemParams)

        // facebook
        val facebookParams = Bundle()
        facebookParams.putInt(AppEventsConstants.EVENT_PARAM_CONTENT_ID, itemId)
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, itemCategory)
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT, itemName)
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "BDT")
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_VALUE_TO_SUM, amount)

        AppEventsLogger.newLogger(context)
            .logEvent("View Content", facebookParams)
    }

    //roni's code

    fun begainCheckout(
        context: Context,
        itemName: String,
    ) {
        // Firebase
        val itemParams = Bundle()

        itemParams.putString(FirebaseAnalytics.Param.CONTENT, itemName)

        //FirebaseAnalytics.getInstance(context).logEvent("Begin_Checkout", itemParams)
        FirebaseAnalytics.getInstance(context).logEvent( FirebaseAnalytics.Event.BEGIN_CHECKOUT, itemParams)

        // facebook
        val facebookParams = Bundle()

        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT, itemName)
//        AppEventsLogger.newLogger(context)
//            .logEvent("Begin Checkout", facebookParams)

        AppEventsLogger.newLogger(context)
            .logEvent(
                AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT,
                facebookParams
            )
    }

    fun AddtoCart (
        context: Context,
        itemName: String,

    ) {
        // Firebase
        val itemParams = Bundle()
        itemParams.putString(FirebaseAnalytics.Param.CONTENT, itemName)
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.ADD_TO_CART, itemParams)

        // facebook
        val facebookParams = Bundle()
        facebookParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT, itemName)
        //AppEventsLogger.newLogger(context).logEvent("Add To Cart", facebookParams)

        AppEventsLogger.newLogger(context)
            .logEvent(
                AppEventsConstants.EVENT_NAME_ADDED_TO_CART,
                facebookParams
            )
    }

    // Add Custom Event >>
    fun addCustomEvent(
        context: Context,
        eventName: String
    ) {
        // Firebase
        if (eventName.split(" ").size > 1) {
            var name = ""
            for (item in eventName.split(" ")) {

                name += "_$item"
            }
            name.replaceFirst("_", "")
            var filter =""
            filter= name.substring(1);
            FirebaseAnalytics.getInstance(context).logEvent(filter, Bundle())
        } else {
            FirebaseAnalytics.getInstance(context).logEvent(eventName, Bundle())
        }
        // facebook
        AppEventsLogger.newLogger(context).logEvent(eventName)
    }

}