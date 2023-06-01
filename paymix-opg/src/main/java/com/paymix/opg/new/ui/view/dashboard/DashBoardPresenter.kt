package com.walletmix.paymixbusiness.ui.view.dashboard

import com.walletmix.paymixbusiness.data.network.api_response.HomePageResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.merchant.GetMarchentResponse
import com.walletmix.paymixbusiness.base.BasePresenter
import com.walletmix.paymixbusiness.data.network.SSDisposableSingleObserver
import com.walletmix.paymixbusiness.data.network.api_response.auth.LogoutResponseModel
import com.walletmix.paymixbusiness.data.network.api_service.UserAuthApiService
import com.walletmix.paymixbusiness.data.network.api_service.UtilsApiService
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.data.network.api_service.MerchantApiService

import javax.inject.Inject

class DashBoardPresenter  @Inject constructor(view: DashBoardContract.View) :

    BasePresenter<DashBoardContract.View>(view),
    DashBoardContract.Presenter {

    @Inject
    lateinit var merchantApiService: MerchantApiService
    @Inject
    lateinit var userAuthApiService: UserAuthApiService
    @Inject
    lateinit var utilsApiService: UtilsApiService



    override fun homePage() {
        compositeDisposable?.add(
            utilsApiService.getHomePage()
                .subscribeOn(appSchedulerProvider.io())
                .unsubscribeOn(appSchedulerProvider.computation())
                .observeOn(appSchedulerProvider.ui())
                .subscribeWith(object : SSDisposableSingleObserver<HomePageResponseModel, DashBoardContract.View>(mView) {
                    override fun onRequestSuccess(response: HomePageResponseModel) {
                        mView?.homePageResponse(response)
                    }
                })
        )
    }
    override fun getmerchantProfile() {
        if (this::userAuthApiService.isInitialized) {
            mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
            compositeDisposable?.add(
                userAuthApiService.getMerchantProfile()
                    .subscribeOn(appSchedulerProvider.io())
                    .unsubscribeOn(appSchedulerProvider.computation())
                    .observeOn(appSchedulerProvider.ui())
                    .subscribeWith(object :
                        SSDisposableSingleObserver<GetMarchentResponse, DashBoardContract.View>(mView) {
                        override fun onRequestSuccess(response: GetMarchentResponse) {
                            mView?.merchantProfileSuccess(response)
                        }

                    })
            )
        }
    }

        override fun logout() {
            if (this::userAuthApiService.isInitialized) {
                mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
                compositeDisposable?.add(userAuthApiService.doLogout()
                        .subscribeOn(appSchedulerProvider.io())
                        .unsubscribeOn(appSchedulerProvider.computation())
                        .observeOn(appSchedulerProvider.ui())
                        .subscribeWith(object :
                            SSDisposableSingleObserver<LogoutResponseModel, DashBoardContract.View>(mView) {
                            override fun onRequestSuccess(response: LogoutResponseModel) {
                                //splashApi()
                                mView?.logOutDidSucceed()
                            }
                        })
                )
            }
        }

//       fun splashApi() {
//        if (this::utilsApiService.isInitialized) {
//            mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
//            compositeDisposable?.add(
//                utilsApiService.getSplashResources()
//                    .subscribeOn(appSchedulerProvider.io())
//                    .unsubscribeOn(appSchedulerProvider.computation())
//                    .observeOn(appSchedulerProvider.ui())
//                    .subscribeWith(object :
//                        SSDisposableSingleObserver<SplashResponseModel, DashBoardContract.View>(mView) {
//                        override fun onRequestSuccess(response: SplashResponseModel) {
//                            mView?.logOutDidSucceed()
//                        }
//                    })
//            )
//        }
//    }


    }
