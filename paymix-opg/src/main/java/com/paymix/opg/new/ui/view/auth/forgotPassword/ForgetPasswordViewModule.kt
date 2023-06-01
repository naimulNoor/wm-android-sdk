package com.walletmix.paymixbusiness.ui.view.auth.forgotPassword

import dagger.Binds
import dagger.Module

@Module
abstract class ForgetPasswordViewModule {

    @Binds
    abstract fun provideForgetPasswordView(activity: ForgotPasswordActivity): ForgotPasswordContract.View
}