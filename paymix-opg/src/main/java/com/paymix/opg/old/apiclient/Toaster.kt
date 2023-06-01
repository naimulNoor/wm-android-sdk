package com.paymix.opg.old.apiclient

import android.content.Context
import android.widget.Toast

public class PaymixMerchantActivity {

    fun simpleToast(context: Context?, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
    fun getString(name: String): String? {
        return "Your name is $name"
    }


}