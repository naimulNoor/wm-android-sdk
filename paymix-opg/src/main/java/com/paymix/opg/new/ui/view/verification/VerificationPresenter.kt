package com.walletmix.paymixbusiness.ui.view.verification

import com.walletmix.paymixbusiness.base.BasePresenter
import javax.inject.Inject

class VerificationPresenter  @Inject constructor(view: VerificationContract.View) : BasePresenter<VerificationContract.View>(view), VerificationContract.Presenter {
}