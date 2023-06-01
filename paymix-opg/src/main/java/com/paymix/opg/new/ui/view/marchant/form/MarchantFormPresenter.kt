package com.walletmix.paymixbusiness.ui.view.merchant.form

import com.walletmix.paymixbusiness.data.network.api_response.merchant.GetMarchentResponse
import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.model.MerchantBank
import com.walletmix.paymixbusiness.model.MerchantOrganization
import com.walletmix.paymixbusiness.model.MerchantProfile
import com.walletmix.paymixbusiness.ui.view.marchant.form.MerchantFormContract
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.base.BasePresenter
import com.walletmix.paymixbusiness.data.network.SSDisposableSingleObserver
import com.walletmix.paymixbusiness.data.network.api_service.UserAuthApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap
import javax.inject.Inject

class MerchantFormPresenter @Inject constructor(view: MerchantFormContract.View) : BasePresenter<MerchantFormContract.View>(view), MerchantFormContract.Presenter {


    @Inject
    lateinit var authApiService: UserAuthApiService
    @Inject
    lateinit var userAuthApiService: UserAuthApiService




    override fun getMerchantProfileDetails() {
//        mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
//        compositeDisposable?.add(
//            authApiService.getMerchantProfile()
//                .subscribeOn(appSchedulerProvider.io())
//                .unsubscribeOn(appSchedulerProvider.computation())
//                .observeOn(appSchedulerProvider.ui())
//                .subscribeWith(object :
//                    SSDisposableSingleObserver<GetMarchentResponse, MerchantFormContract.View>(mView) {
//                    override fun onRequestSuccess(response: GetMarchentResponse) {
//                        mView?.getMerchantResponse(response)
//                    }
//                })
//        )

        if (this::userAuthApiService.isInitialized) {
            mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
            compositeDisposable?.add(
                userAuthApiService.getMerchantProfile()
                    .subscribeOn(appSchedulerProvider.io())
                    .unsubscribeOn(appSchedulerProvider.computation())
                    .observeOn(appSchedulerProvider.ui())
                    .subscribeWith(object :
                        SSDisposableSingleObserver<GetMarchentResponse, MerchantFormContract.View>(mView) {
                        override fun onRequestSuccess(response: GetMarchentResponse) {
                            mView?.getMerchantResponse(response)
                        }

                    })
            )
        }

    }

    override fun setUpdateMerchantDetails(model: MerchantOrganization) {
        mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
        compositeDisposable?.add(
            authApiService.updateMerchantOrganization(model)
                .subscribeOn(appSchedulerProvider.io())
                .unsubscribeOn(appSchedulerProvider.computation())
                .observeOn(appSchedulerProvider.ui())
                .subscribeWith(object :
                    SSDisposableSingleObserver<MerchantUpdateResponse, MerchantFormContract.View>(mView) {
                    override fun onRequestSuccess(response: MerchantUpdateResponse) {
                        mView?.updateMerchantDetailsResponse(response)
                    }
                })
        )
    }

    override fun setUpdateMerchantBankDetails(model: MerchantBank) {

        mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
        compositeDisposable?.add(
            authApiService.updateMerchantBank(model)
                .subscribeOn(appSchedulerProvider.io())
                .unsubscribeOn(appSchedulerProvider.computation())
                .observeOn(appSchedulerProvider.ui())
                .subscribeWith(object :
                    SSDisposableSingleObserver<MerchantUpdateResponse, MerchantFormContract.View>(mView) {
                    override fun onRequestSuccess(response: MerchantUpdateResponse) {
                        mView?.updateMerchantBankDetailsResponse(response)
                    }
                })
        )
    }

    override fun setUpdateMerchantProfileDetails(model: MerchantProfile) {
//        mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
//        compositeDisposable?.add(
//            authApiService.updateMerchantProfile(model)
//                .subscribeOn(appSchedulerProvider.io())
//                .unsubscribeOn(appSchedulerProvider.computation())
//                .observeOn(appSchedulerProvider.ui())
//                .subscribeWith(object :
//                    SSDisposableSingleObserver<MerchantUpdateResponse, MerchantFormContract.View>(mView) {
//                    override fun onRequestSuccess(response: MerchantUpdateResponse) {
//                        mView?.updateMerchantProfileResponse(response)
//                    }
//                })
//        )
    }

    override fun setUpdateMerchantDocument(
        dataMap: HashMap<String, RequestBody>,
        attachment: MultipartBody.Part?) {
        mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
        compositeDisposable?.add(
            authApiService.updateMerchantAttatchment(dataMap,attachment)
                .subscribeOn(appSchedulerProvider.io())
                .unsubscribeOn(appSchedulerProvider.computation())
                .observeOn(appSchedulerProvider.ui())
                .subscribeWith(object :
                    SSDisposableSingleObserver<MerchantUpdateResponse, MerchantFormContract.View>(mView) {
                    override fun onRequestSuccess(response: MerchantUpdateResponse) {
                        mView?.updateMerchantDocumentResponse(response)
                    }
                })
        )
    }


}
