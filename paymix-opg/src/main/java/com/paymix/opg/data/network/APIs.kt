package com.paymix.opg.data.network

object APIs {

    //Local
    //const val BASE_URL = "http://192.168.68.104/walletmix-payment-gateway/api"
    const val BASE_URL = "https://sandboxdev.walletmix.com/api/"

    //office local
    //const val BASE_URL="http://192.168.68.134:8080/walletmix-payment-gateway/api/"



    // APP VERSION
    const val GET_APP_VERSION = "$BASE_URL/app-version"

    // Dashboard
    const val GET_merchant_PROFILE = "${BASE_URL}get-profile-details"


    // AUTH
    const val LOGIN = "${BASE_URL}login"
    const val REGISTRATION = "${BASE_URL}signup"
    const val LOGOUT = "${BASE_URL}log-out"

    const val UPDATE_MERCHANT_PASSWORD = "${BASE_URL}update-merchant-password"

    //PROFILE

    const val GET_MERCHANT_PROFILE = "${BASE_URL}get-profile-details"

    const val UPDATE_MERCHANT_DETAILS = "${BASE_URL}update-merchant-details"
    const val UPDATE_BANK_DETAILS = "${BASE_URL}update-merchant-bank-details"
    const val UPDATE_MERCHANT_PROFILE = "${BASE_URL}update-merchant-profile-details"
    const val UPDATE_ATTACHMENT = "${BASE_URL}update-merchant-documents"






    const val STORE_DEVICE_TOKEN = "${BASE_URL}store-device-token"

    const val SPLASH = "${BASE_URL}splash"

    //DashBoard
    const val HOMEPAGE = "${BASE_URL}home-page"

    //Transaction
    const val TRANSACTION_LIST = "${BASE_URL}transaction-list"
    const val TRANSACTION_DETAILS = "${BASE_URL}transaction-details"
    const val TRANSACTION_SUMMERY = "${BASE_URL}transaction-summery"
    const val UPLOAD_INVOICE = "${BASE_URL}upload-invoice/{id}"
    const val VIEW_INVOICE = "${BASE_URL}view-invoice/"

    ///comments
    const val COMMENT_LIST = "${BASE_URL}transaction-comments"


    val NO_AUTH_URLs = arrayOf(
        "$BASE_URL$GET_APP_VERSION",
        "$BASE_URL$LOGIN",
        "$BASE_URL$REGISTRATION",

    )

    val CACHING_URLs = arrayOf(
        //banner
        "$BASE_URL$LOGIN"
    )

}