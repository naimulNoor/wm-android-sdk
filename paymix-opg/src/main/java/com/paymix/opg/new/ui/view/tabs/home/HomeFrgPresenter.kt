package com.walletmix.paymixbusiness.ui.view.tabs.home




import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.base.BasePresenter
import com.walletmix.paymixbusiness.data.network.SSDisposableSingleObserver

import com.walletmix.paymixbusiness.data.network.api_service.UtilsApiService
import com.walletmix.paymixbusiness.data.network.api_response.HomePageResponseModel
import javax.inject.Inject

class HomeFrgPresenter @Inject constructor(view: HomeFrgContract.View) :
    BasePresenter<HomeFrgContract.View>(view),
    HomeFrgContract.Presenter{

    @Inject
    lateinit var utilsApiService: UtilsApiService


    override fun homePage() {

        if (this::utilsApiService.isInitialized) {
            mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
            compositeDisposable?.add(
                utilsApiService.getHomePage()
                    .subscribeOn(appSchedulerProvider.io())
                    .unsubscribeOn(appSchedulerProvider.computation())
                    .observeOn(appSchedulerProvider.ui())
                    .subscribeWith(object :
                        SSDisposableSingleObserver<HomePageResponseModel, HomeFrgContract.View>(mView) {
                        override fun onRequestSuccess(response: HomePageResponseModel) {
                            mView?.homePageResponse(response)
                        }

                    })
            )
        }
    }
}