package com.walletmix.paymixbusiness.ui.view.tabs.profile

import com.walletmix.paymixbusiness.data.network.api_response.merchant.GetMarchentResponse
import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.base.BasePresenter
import com.walletmix.paymixbusiness.data.network.SSDisposableSingleObserver
import com.walletmix.paymixbusiness.data.network.api_service.UserAuthApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap
import javax.inject.Inject

 class ProfileFrgPresenter @Inject constructor(view: ProfileFrgContract.View) :
    BasePresenter<ProfileFrgContract.View>(view),
    ProfileFrgContract.Presenter{

     @Inject
     lateinit var userAuthApiService: UserAuthApiService




     override fun profile() {

         if (this::userAuthApiService.isInitialized) {
             mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
             compositeDisposable?.add(
                 userAuthApiService.getMerchantProfile()
                     .subscribeOn(appSchedulerProvider.io())
                     .unsubscribeOn(appSchedulerProvider.computation())
                     .observeOn(appSchedulerProvider.ui())
                     .subscribeWith(object :
                         SSDisposableSingleObserver<GetMarchentResponse, ProfileFrgContract.View>(mView) {
                         override fun onRequestSuccess(response: GetMarchentResponse) {
                             mView?.profilePageResponse(response)
                         }

                     })
             )
         }
     }

     override fun setUpdateProfileDocument(
         dataMap: HashMap<String, RequestBody>,
         attachment: MultipartBody.Part?
     ) {
         mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
         compositeDisposable?.add(
             userAuthApiService.updateMerchantAttatchment(dataMap,attachment)
                 .subscribeOn(appSchedulerProvider.io())
                 .unsubscribeOn(appSchedulerProvider.computation())
                 .observeOn(appSchedulerProvider.ui())
                 .subscribeWith(object :
                     SSDisposableSingleObserver<MerchantUpdateResponse, ProfileFrgContract.View>(mView) {
                     override fun onRequestSuccess(response: MerchantUpdateResponse) {
                         mView?.updateMerchantDocumentResponse(response)
                     }
                 })
         )
     }
 }