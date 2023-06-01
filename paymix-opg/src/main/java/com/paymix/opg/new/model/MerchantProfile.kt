package com.walletmix.paymixbusiness.model

import java.util.*

class MerchantProfile(
     merchant_name:String,
//     username:String,
//     email:String,
//     contact:String,
//     url:String
) {

    var merchant_name:String=""
//    var username:String=""
//    var email:String=""
//    var contact:String=""
//    var url:String=""

    init {
        this.merchant_name= merchant_name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
//        this.username= username
//        this.email= email
//        this.contact= contact
//        this.url= url
    }
}