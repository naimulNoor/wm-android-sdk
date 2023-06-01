package com.walletmix.paymixbusiness.ui.view.auth.signup

import com.walletmix.paymixbusiness.base.BasePresenter
import com.walletmix.paymixbusiness.data.network.SSDisposableSingleObserver
import com.walletmix.paymixbusiness.data.network.api_response.auth.SignUpResponseModel
import com.walletmix.paymixbusiness.data.network.api_service.UserAuthApiService
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager
import com.walletmix.paymixbusiness.R
import javax.inject.Inject

class SignUpPresenter
@Inject constructor(view: SignUpContract.View) : BasePresenter<SignUpContract.View>(view), SignUpContract.Presenter {


    @Inject
    lateinit var userAuthApiService: UserAuthApiService

    @Inject
    lateinit var preferences: PreferenceManager


    override fun doSignUp(dataMap: HashMap<String, String>){

        mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
        compositeDisposable?.add(
            userAuthApiService.doSignUp(dataMap)
                .subscribeOn(appSchedulerProvider.io())
                .unsubscribeOn(appSchedulerProvider.computation())
                .observeOn(appSchedulerProvider.ui())
                .subscribeWith(object :
                    SSDisposableSingleObserver<SignUpResponseModel, SignUpContract.View>(mView) {
                    override fun onRequestSuccess(response: SignUpResponseModel) {


                        //set user data
                        preferences.put(PrefKeys.TOKEN, response.data.token)
//                        preferences.put(PrefKeys.USERNAME, response.data.username)
//                        preferences.put(PrefKeys.EMAIL, response.data.email)
//                        preferences.put(PrefKeys.CONTACT, response.data.contact)
//                        preferences.put(PrefKeys.PASSWORD, response.data.password)
//                        preferences.put(PrefKeys.CONFIRM_PASSWORD, response.data.confirm_password)
//                        preferences.put(PrefKeys.TERMS_AND_CONDITIONS, response.data.terms_and_condition)
                        preferences.putBoolean(PrefKeys.IS_REGISTERED, true)

                        mView?.signupDidSucceed(response)
                    }


                })
        )
    }

}