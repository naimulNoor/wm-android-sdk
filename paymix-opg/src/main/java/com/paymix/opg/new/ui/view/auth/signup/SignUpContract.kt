package com.walletmix.paymixbusiness.ui.view.auth.signup

import com.walletmix.paymixbusiness.base.BaseContract
import com.walletmix.paymixbusiness.data.network.api_response.auth.SignUpResponseModel


interface SignUpContract {


    interface View : BaseContract.View{

        fun signupBtnDidTapped()


        fun signupDidSucceed(response: SignUpResponseModel)
    }


    interface Presenter : BaseContract.Presenter {
        fun doSignUp(dataMap: HashMap<String, String>)
    }

}