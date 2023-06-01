package com.walletmix.paymixbusiness.ui.view.auth.login

import com.walletmix.paymixbusiness.base.BasePresenter
import com.walletmix.paymixbusiness.data.network.SSDisposableSingleObserver
import com.walletmix.paymixbusiness.data.network.api_response.auth.LoginResponseModel
import com.walletmix.paymixbusiness.data.network.api_service.UserAuthApiService
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager
import com.walletmix.paymixbusiness.R
import javax.inject.Inject

class LoginPresenter
@Inject constructor(view: LoginContract.View) : BasePresenter<LoginContract.View>(view), LoginContract.Presenter {

    @Inject
    lateinit var userAuthApiService: UserAuthApiService

    @Inject
    lateinit var preferences: PreferenceManager

    override fun doLogin(dataMap: HashMap<String, String>) {
        mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
        compositeDisposable?.add(
            userAuthApiService.doLogin(dataMap)
                .subscribeOn(appSchedulerProvider.io())
                .unsubscribeOn(appSchedulerProvider.computation())
                .observeOn(appSchedulerProvider.ui())
                .subscribeWith(object :
                    SSDisposableSingleObserver<LoginResponseModel, LoginContract.View>(mView) {
                    override fun onRequestSuccess(response: LoginResponseModel) {

                        //set user data
                        preferences.put(PrefKeys.TOKEN, response.data.token)
                        preferences.putBoolean(PrefKeys.LOGGED_IN, true)

                        mView?.loginDidSucceed(response)
                    }
                })
        )
    }
}
