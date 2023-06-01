package com.walletmix.paymixbusiness.ui.view.auth.login

import android.content.Context
import com.walletmix.paymixbusiness.utils.ValidationUtils
import com.walletmix.paymixbusiness.utils.showToast
import com.walletmix.paymixbusiness.R

class Login(myContext: Context) {

    var myContext: Context;
    private lateinit var validation: ValidationUtils

    init {
        this.myContext = myContext;
    }


    fun checkValidation(username: String, password: String, ): HashMap<String, String>? {
        when {
            username.isEmpty() -> myContext.showToast(myContext.getString(R.string.empty_validation))
            password.isEmpty() -> myContext.showToast(myContext.getString(R.string.empty_validation))



            else -> {
                val loginData = HashMap<String, String>()
                loginData["username"] = username
                loginData["password"] = password
                return loginData;
            }
        }


        return null
    }
}
