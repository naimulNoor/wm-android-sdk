package com.walletmix.paymixbusiness.ui.view.auth.resetPassword

import android.content.Context
import com.walletmix.paymixbusiness.utils.ValidationUtils
import com.walletmix.paymixbusiness.utils.showToast

class ResetPassword (myContext: Context){

    var myContext: Context;
    private lateinit var validation: ValidationUtils


    init {
        this.myContext = myContext;
    }

    fun checkResetPasswordValidation(oldPassword: String,password: String, confirmPassword:String): HashMap<String, String>?{

        validation = ValidationUtils()
        when{

            oldPassword.isEmpty()->myContext.showToast("Old Password must be Required")
            password.isEmpty()->myContext.showToast("Password must be Required")
            confirmPassword.isEmpty()->myContext.showToast("Confirm Password must be Required")

            validation.checkvalidPassword(password) != null -> myContext.showToast("Minimum 10 Character Password and Must Contain 1 Upper-case Character, 1 Lower-case Character and 1 Special Character (@#\$%^&+=) ")
            validation.checkConfirmPassword(password,confirmPassword) != null  -> myContext.showToast("Password Do not Match")

            else -> {
                val resetPasswordData = HashMap<String, String>()

                resetPasswordData["old_password"] = oldPassword
                resetPasswordData["password"] = password
                resetPasswordData["password_confirmation"] = confirmPassword


                return resetPasswordData;
            }
        }
        return null
    }
}