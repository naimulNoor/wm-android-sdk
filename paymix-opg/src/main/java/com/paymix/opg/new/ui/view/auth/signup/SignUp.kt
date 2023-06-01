package com.walletmix.paymixbusiness.ui.view.auth.signup

import android.content.Context
import android.webkit.URLUtil
import com.walletmix.paymixbusiness.utils.ValidationUtils
import com.walletmix.paymixbusiness.utils.showToast

class SignUp(myContext: Context) {

    var myContext: Context;
    private lateinit var validation: ValidationUtils


    init {
        this.myContext = myContext;
    }

    fun checkSignupValidation(username: String,merchentName: String,email:String,contact:String,website: String, password: String,confirmPassword:String,didAcceptedTermsAndConditions:Boolean): HashMap<String, String>?{

        validation = ValidationUtils()
        when{

            username.isEmpty()->myContext.showToast("UserName must be Required")
            merchentName.isEmpty()->myContext.showToast("Merchant Name must be Required")
            email.isEmpty()->myContext.showToast("Email must be Required")
            contact.isEmpty()->myContext.showToast("Phone Number must be Required")
            website.isEmpty()->myContext.showToast("Website must be Required")
            password.isEmpty()->myContext.showToast("Password must be Required")
            confirmPassword.isEmpty()->myContext.showToast("Confirm Password must be Required")
            !didAcceptedTermsAndConditions -> myContext.showToast("The terms and conditions and privacy policy must be accepted.")



            validation.checkUsername(username) != null ->  myContext.showToast("Invalid UserName")
            validation.checkUsername(merchentName) != null ->  myContext.showToast("Invalid Merchent Name")
            validation.checkIfEmailIsValid(email) != null ->  myContext.showToast("Invalid Email Address")
            validation.checkValidPhoneNumber(contact) != null -> myContext.showToast("Invalid Contract Number")
            validation.checkvalidPassword(password) != null -> myContext.showToast("Minimum 10 Character Password and Must Contain 1 Upper-case Character, 1 Lower-case Character and 1 Special Character (@#\$%^&+=) ")
            validation.checkConfirmPassword(password,confirmPassword) != null  -> myContext.showToast("Password Do not Match")

            !URLUtil.isValidUrl(website)-> myContext.showToast("Invalid Website")
//            !Patterns.WEB_URL.matcher(website).matches()->myContext.showToast("Invalid Website")

            else -> {
                val signupData = HashMap<String, String>()
                signupData["username"] = username
                signupData["merchant_name"] = merchentName
                signupData["email"] = email
                signupData["contact"] = contact
                signupData["url"] = website
                signupData["password"] = password
                signupData["confirm_password"] = confirmPassword
                signupData["terms_and_condition"] = 1.toString()

                return signupData;
            }
        }
        return null
    }
}