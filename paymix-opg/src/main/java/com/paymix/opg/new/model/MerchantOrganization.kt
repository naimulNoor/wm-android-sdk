package com.walletmix.paymixbusiness.model

import java.util.*


class MerchantOrganization(
     contact_no:String,
     org_name:String,
     org_address:String,
     org_phone:String,
     org_product:String,
     org_mobile:String,
     nid:String,
     business_type:String,
     passport_no:String
) {

    var contact_no:String=""
    var org_name:String=""
    var org_address:String=""
    var org_phone:String=""
    var org_product:String=""
    var org_mobile:String=""
    var nid:String=""
    var business_type:String=""
    var passport_no:String=""

    init {

        this.contact_no= contact_no
        this.org_name= org_name
        this.org_address= org_address
        this.org_phone= org_phone
        this.org_product= org_product
        this.org_mobile= org_mobile
        this.nid= nid
        this.business_type= business_type
        this.passport_no= passport_no
    }
}