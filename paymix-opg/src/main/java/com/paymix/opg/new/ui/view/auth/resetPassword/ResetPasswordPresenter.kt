package com.walletmix.paymixbusiness.ui.view.auth.resetPassword

import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.ui.view.auth.resetPassword.ResetPasswordContract
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.base.BasePresenter
import com.walletmix.paymixbusiness.data.network.SSDisposableSingleObserver
import com.walletmix.paymixbusiness.data.network.api_service.UserAuthApiService
import javax.inject.Inject

class ResetPasswordPresenter  @Inject constructor(view: ResetPasswordContract.View) :

    BasePresenter<ResetPasswordContract.View>(view),
    ResetPasswordContract.Presenter
{

    @Inject
    lateinit var authApiService: UserAuthApiService

    override fun initChangePin(dataMap: HashMap<String, String>) {

        if (this::authApiService.isInitialized) {
            mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
            compositeDisposable?.add(
                authApiService.doResetPassword(dataMap)
                    .subscribeOn(appSchedulerProvider.io())
                    .unsubscribeOn(appSchedulerProvider.computation())
                    .observeOn(appSchedulerProvider.ui())
                    .subscribeWith(object :
                        SSDisposableSingleObserver<MerchantUpdateResponse, ResetPasswordContract.View>(mView) {
                        override fun onRequestSuccess(response: MerchantUpdateResponse) {
                            mView?.initChangePinDidSucceed(response)
                        }
                    })
            )
        }
    }


}

