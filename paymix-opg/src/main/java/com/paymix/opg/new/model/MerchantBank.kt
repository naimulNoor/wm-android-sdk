package com.walletmix.paymixbusiness.model

import java.util.*


class MerchantBank(
    bank_name:String,
    bank_branch:String,
    bank_account_name:String,
    bank_account_no:String,

) {

    var bank_name:String=""
    var bank_branch:String=""
    var bank_account_name:String=""
    var bank_account_no:String=""

    init {
        this.bank_name= bank_name
        this.bank_branch= bank_branch
        this.bank_account_name= bank_account_name
        this.bank_account_no= bank_account_no
    }
}