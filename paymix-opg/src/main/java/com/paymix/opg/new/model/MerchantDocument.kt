package com.walletmix.paymixbusiness.model

import java.util.*


class MerchantDocument(
    logo:String,
    profile_photo:String,
    nid_copy:String,
    passport:String,
    trade_licence:String,
    agreement:String,
    suspension_letter:String,
    tin_bin:String,
    brand_color:String
) {

    var logo:String=""
    var profile_photo:String=""
    var nid_copy:String=""
    var passport:String=""
    var trade_licence:String=""
    var agreement:String=""
    var suspension_letter:String=""
    var tin_bin:String=""
    var brand_color:String=""

    init {
         this.logo=logo
         this.profile_photo=profile_photo
         this.nid_copy=nid_copy
         this.passport=passport
         this.trade_licence=trade_licence
         this.agreement=agreement
         this.suspension_letter=suspension_letter
         this.tin_bin=tin_bin
         this.brand_color=brand_color

    }
}