package com.walletmix.paymixbusiness.ui.view.splash

import com.walletmix.paymixbusiness.base.BasePresenter
import com.walletmix.paymixbusiness.data.network.SSDisposableSingleObserver
import com.walletmix.paymixbusiness.data.network.api_response.utils.SplashResponseModel
import com.walletmix.paymixbusiness.data.network.api_service.UtilsApiService
import javax.inject.Inject

class SplashPresenter
@Inject constructor(view: SplashContract.View) : BasePresenter<SplashContract.View>(view), SplashContract.Presenter {


    @Inject
    lateinit var utilsApiService: UtilsApiService
    override fun getAppVersion() {
        compositeDisposable?.add(
            utilsApiService.getSplashResources()
                .subscribeOn(appSchedulerProvider.io())
                .unsubscribeOn(appSchedulerProvider.computation())
                .observeOn(appSchedulerProvider.ui())
                .subscribeWith(object : SSDisposableSingleObserver<SplashResponseModel, SplashContract.View>(mView) {
                    override fun onRequestSuccess(response: SplashResponseModel) {
                        mView?.appVersionDidReceived(response)
                    }

                })
        )
    }


}
