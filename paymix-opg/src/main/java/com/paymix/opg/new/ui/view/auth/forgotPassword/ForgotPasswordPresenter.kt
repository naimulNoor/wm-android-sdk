package com.walletmix.paymixbusiness.ui.view.auth.forgotPassword

import com.walletmix.paymixbusiness.base.BasePresenter
import javax.inject.Inject

class ForgotPasswordPresenter @Inject constructor(view: ForgotPasswordContract.View) :

    BasePresenter<ForgotPasswordContract.View>(view),
    ForgotPasswordContract.Presenter
{

}
