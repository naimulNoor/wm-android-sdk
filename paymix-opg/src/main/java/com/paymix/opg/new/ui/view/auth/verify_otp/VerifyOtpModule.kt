package com.walletmix.paymixbusiness.ui.view.auth.verify_otp


import com.walletmix.paymixbusiness.ui.view.auth.verify_otp.VerifyOtpActivity
import dagger.Binds
import dagger.Module


@Module
abstract class VerifyOtpModule {

    @Binds
    abstract fun provideVerifyOtpView(activity: VerifyOtpActivity): VerifyOtpContract.View
}